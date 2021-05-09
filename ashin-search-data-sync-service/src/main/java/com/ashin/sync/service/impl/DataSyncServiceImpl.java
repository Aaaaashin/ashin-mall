package com.ashin.sync.service.impl;

import com.alibaba.fastjson.JSON;
import com.ashin.pojo.Goods;
import com.ashin.pojo.GoodsInfo;
import com.ashin.sync.mapper.GoodsMapper;
import com.ashin.sync.service.DataSyncService;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class DataSyncServiceImpl implements DataSyncService {
    private final GoodsMapper goodsMapper;
    private final RestHighLevelClient client;
    public DataSyncServiceImpl(GoodsMapper goodsMapper,RestHighLevelClient client) {
        this.goodsMapper = goodsMapper;
        this.client = client;
    }
    // 索引库名称
    private final static String INDEX_NAME = "goodsinfo";

    @Override
    public void importAll() {
        //1.获取全部商品数据 ;  -----> Goods
        List<Goods> goodsList = goodsMapper.selectList(null);
        //2.判断索引库是否存在
        if(isExist()){
            //2.1 存在，删除索引库
            deleteIndex();
        }
        //3.创建索引库
        createIndex();
        //4.数据导入索引库中
        if(goodsList!=null && !goodsList.isEmpty()){
            importIndex(goodsList);
        }
    }
    private boolean isExist(){
        GetIndexRequest request = new GetIndexRequest(INDEX_NAME);
        try {
            return client.indices().exists(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    private void deleteIndex(){
        DeleteIndexRequest request = new DeleteIndexRequest(INDEX_NAME);
        try {
            client.indices().delete(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void createIndex(){
        CreateIndexRequest request = new CreateIndexRequest(INDEX_NAME);
        try {
            request.mapping("{" +
                    "\"properties\": {" +
                    "\"id\": {" +
                    "\"type\": \"integer\"" +
                    "}," +
                    "\"image\": {" +
                    "\"index\": false," +
                    "\"type\": \"keyword\"" +
                    "}," +
                    "\"brandId\": {" +
                    "\"type\": \"integer\"" +
                    "}," +
                    "\"categoryId\": {" +
                    "\"type\": \"integer\"" +
                    "}," +
                    "\"brandName\": {" +
                    "\"type\": \"keyword\"" +
                    "}," +
                    "\"categoryName\": {" +
                    "\"type\": \"keyword\"" +
                    "}," +
                    "\"num\": {" +
                    "\"type\": \"integer\"" +
                    "}," +
                    "\"specMap\": {" +
                    "\"type\": \"object\"" +
                    "}," +
                    "\"updateTime\": {" +
                    "\"type\": \"date\"" +
                    "}," +
                    "\"spec\": {" +
                    "\"type\": \"keyword\"" +
                    "}," +
                    "\"createTime\": {" +
                    "\"type\": \"date\"" +
                    "}," +
                    "\"price\": {" +
                    "\"type\": \"integer\"" +
                    "}," +
                    "\"name\": {" +
                    "\"analyzer\": \"ik_max_word\"," +
                    "\"type\": \"text\"" +
                    "}," +
                    "\"status\": {" +
                    "\"type\": \"keyword\"" +
                    "}" +
                    "}" +
                    "}",XContentType.JSON);
            client.indices().create(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void importIndex(List<Goods> goodsList) {
        BulkRequest bulkRequest = new BulkRequest(INDEX_NAME);
        goodsList.stream()
                .map(this::convertGoodsToGoodsInfo)
                .map(goodInfo->{
                    IndexRequest indexRequest = new IndexRequest();
                    indexRequest.id(goodInfo.getId()+"");
                    indexRequest.source(JSON.toJSONString(goodInfo),XContentType.JSON);
                    return indexRequest;
                })
                .forEach(bulkRequest::add);
        try {
            client.bulk(bulkRequest,RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("批量添加索引失败");
        }
    }
    private GoodsInfo convertGoodsToGoodsInfo(Goods goods) {
        String goodsStr = JSON.toJSONString(goods);
        GoodsInfo goodsInfo = JSON.parseObject(goodsStr, GoodsInfo.class); //名称相同的属性会自动封装;
        //组装品牌及名称
        goodsInfo.setBrandName(goods.getBrand());
        //组装分类数据
        goodsInfo.setCategoryName(goods.getCategory());
        //组装
        String spec = goods.getSpec();
        Map sepcMap = JSON.parseObject(spec, Map.class);
        goodsInfo.setSpecMap(sepcMap);
        goodsInfo.setCreateTime(goods.getCreatetime());
        goodsInfo.setUpdateTime(goods.getUpdatetime());
        return goodsInfo;
    }
}