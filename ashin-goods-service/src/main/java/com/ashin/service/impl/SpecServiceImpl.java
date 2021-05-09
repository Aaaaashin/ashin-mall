package com.ashin.service.impl;

import com.ashin.entity.PageResult;
import com.ashin.entity.QueryPageBean;
import com.ashin.mapper.SpecMapper;
import com.ashin.pojo.Spec;
import com.ashin.service.SpecService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SpecServiceImpl implements SpecService {

    @Autowired
    private SpecMapper specMapper;

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        //设置分页参数
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());

        //根据条件查询
        Spec spec = new Spec();
        if(!StringUtils.isEmpty(queryPageBean.getQueryString())){
            spec.setName(queryPageBean.getQueryString());
        }
        List<Spec> brands = specMapper.select(spec);

        PageInfo pageInfo = new PageInfo(brands);
        return new PageResult(pageInfo.getTotal(),pageInfo.getList());
    }

    @Override
    public List<Map> findAllSpecWithOptions(Integer category3id) {
        List<Map> resultList = new ArrayList<Map>();

        List<Integer> specIds = specMapper.findSpecIdsByCategoryId(category3id);

        if(specIds != null && specIds.size()>0){
            Example example = new Example(Spec.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andIn("id",specIds);

            List<Spec> specs = specMapper.selectByExample(example);

            if(specs != null){
                for (Spec spec : specs) {
                    Map map = new HashMap();
                    map.put(spec.getName(), spec.getOptions().split(","));

                    resultList.add(map);
                }
            }
        }


        return resultList;
    }
}