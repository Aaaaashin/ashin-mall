package com.ashin.service.impl;

import com.ashin.entity.PageResult;
import com.ashin.entity.QueryPageBean;
import com.ashin.mapper.BrandMapper;
import com.ashin.pojo.Brand;
import com.ashin.service.BrandService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandMapper brandMapper;

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {

        //设置分页参数
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());

        //根据条件查询
        Brand brand = new Brand();
        if(!StringUtils.isEmpty(queryPageBean.getQueryString())){
            brand.setName(queryPageBean.getQueryString());
        }
        List<Brand> brands = brandMapper.select(brand);

        PageInfo pageInfo = new PageInfo(brands);

        return new PageResult(pageInfo.getTotal(),pageInfo.getList());
    }

    @Override
    public List<Brand> findAll() {
        return brandMapper.selectAll();
    }

    @Override
    public Brand findById(Integer id) {
        return brandMapper.selectByPrimaryKey(id);
    }

}