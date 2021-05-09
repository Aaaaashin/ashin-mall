package com.ashin.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.ashin.pojo.GoodsInfo;
import com.ashin.search.service.SearchService;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public Map search(Map<String, String> searchMap) throws IOException {
        Map resultMap = new HashMap();
        if (ObjectUtils.isEmpty(searchMap)) {
            return resultMap;
        }

        String keyword = searchMap.get("keyword");
//        if (StringUtils.isEmpty(keyword)) {
//            return resultMap;
//        }

        //和关键字相关联的所有 品牌 和 分类
        Map brandsAndCategories = this.selectBrandAndCategoty(keyword);
        resultMap.putAll(brandsAndCategories);

        //查询相关数据
        Map goods = this.selectGoods(searchMap);
        resultMap.putAll(goods);

        return resultMap;
    }

    private Map selectGoods(Map<String, String> searchMap) throws IOException {
        HashMap resultMap = new HashMap();

        //查询相关数据
        //1.构建查询请求对象
        SearchRequest request = new SearchRequest("goodsinfo");
        SearchSourceBuilder builder = new SearchSourceBuilder();
        //组装条件，涉及多个条件时使用布尔查询
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        //a.关键字搜索
        boolQueryBuilder.must(QueryBuilders.matchQuery("name", searchMap.get("keyword")));
        //b.品牌搜索
        String brand = searchMap.get("brand");
        if (!(StringUtils.isEmpty(brand))) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("brandName", brand));
        }
        //c.分类搜索
        String category = searchMap.get("category");
        if (!(StringUtils.isEmpty(category))) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("categoryName", category));
        }
        //d.价格区间搜索
        String price = searchMap.get("price");
        if (!(StringUtils.isEmpty(price))) {
            String[] priceRange = searchMap.get("price").split("-");
            boolQueryBuilder.filter(QueryBuilders.rangeQuery("price")
                    .gte(Integer.parseInt(priceRange[0])));
            if (priceRange.length == 2) {
                boolQueryBuilder.filter(QueryBuilders.rangeQuery("price")
                        .lte(Integer.parseInt(priceRange[1])));
            }
        }
        builder.query(boolQueryBuilder);

        //排序
        if (!(StringUtils.isEmpty(searchMap.get("sortField"))
                && !(StringUtils.isEmpty(searchMap.get("sortRule"))))) {
            if ("ASC".equals(searchMap.get("sortRule"))) {
                //升序
                builder.sort(SortBuilders.fieldSort(
                        searchMap.get("sortField")).order(SortOrder.ASC));
                //降序
                builder.sort(SortBuilders.fieldSort(
                        searchMap.get("sortField")).order(SortOrder.DESC));
            }
        }

        //添加分页信息
        //当前页
        Integer pageNum = Integer.parseInt(String.valueOf(searchMap.get("pageNum")));
        //每页显示条数
        Integer pageSize = Integer.parseInt(String.valueOf(searchMap.get("pageSize")));
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 10;
        }
        builder.from((pageNum - 1) * pageSize);
        builder.size(pageSize);

        //设置高亮域以及高亮的样式
        builder.highlighter(new HighlightBuilder()
                .field("name")
                .preTags("<span style='color:red'>")
                .postTags("</span>")
        );
        request.source(builder);

        //执行查询，获取查询结果
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        //解析结果
        long totalHits = response.getHits().getTotalHits().value;
        System.out.println("totalHits = " + totalHits);

        ArrayList<GoodsInfo> goodsInfos = new ArrayList<>();
        SearchHit[] hits = response.getHits().getHits();
        for (SearchHit hit : hits) {
            //获取原始结果
            String _source = hit.getSourceAsString();
            GoodsInfo goodsInfo = JSON.parseObject(_source, GoodsInfo.class);
            //获取高亮结果
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            if (!(ObjectUtils.isEmpty(highlightFields))) {
                HighlightField highlightField = highlightFields.get("name");
                if (!(ObjectUtils.isEmpty(highlightField))) {
                    Text[] fragments = highlightField.getFragments();
                    StringBuilder stringBuilder = new StringBuilder();
                    if (!(ObjectUtils.isEmpty(fragments))) {
                        for (Text fragment : fragments) {
                            stringBuilder.append(fragment);
                        }
                        goodsInfo.setName(stringBuilder.toString());
                    }
                }
            }
            goodsInfos.add(goodsInfo);
        }

        resultMap.put("pageNum", pageNum);
        resultMap.put("pageSize", pageSize);
        resultMap.put("total", totalHits);
        resultMap.put("goodList", goodsInfos);
        if (totalHits % pageSize == 0) {
            resultMap.put("pages", totalHits / pageSize);
        } else {
            resultMap.put("pages", totalHits / pageSize + 1);
        }
        return resultMap;
    }

    //聚合查询 和此关键字相关的 品牌 和 分类
    private Map selectBrandAndCategoty(String keyword) throws IOException {
        HashMap map = new HashMap();
        SearchRequest request = new SearchRequest("goodsinfo");
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.matchQuery("name", keyword));
        builder.size(0);

        //按照品牌进行分组（聚合）查询 -- 设置size，不设置，默认查询数：10
        String _brand = "brand";
        builder.aggregation(AggregationBuilders.terms(_brand)
                .field("brandName").size(Integer.MAX_VALUE));

        //分类聚合
        String _category = "category";
        builder.aggregation(AggregationBuilders.terms(_category)
                .field("categoryName").size(Integer.MAX_VALUE));

        request.source(builder);

        //执行索引库查询，获取查询结果
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        //解析分组结果
        HashMap<String, List<String>> aggMap = new HashMap<>();
        Aggregations aggregations = response.getAggregations();
        for (Aggregation aggregation : aggregations) {
            Terms terms = (Terms) aggregation;
            System.out.println("terms.getName() = " + terms.getName());

            ArrayList<String> results = new ArrayList<>();
            List<? extends Terms.Bucket> buckets = terms.getBuckets();
            if (!(ObjectUtils.isEmpty(buckets))) {
                for (Terms.Bucket bucket : buckets) {
                    results.add(bucket.getKeyAsString());
                }
            }
            aggMap.put(terms.getName(), results);
        }
        //组装需要返回的结果
        map.put("brandList", aggMap.get("brand"));
        map.put("categoryList", aggMap.get("category"));
        return map;

    }
}
