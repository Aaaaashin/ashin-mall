package com.ashin.mapper;

import com.ashin.pojo.Spec;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SpecMapper extends Mapper<Spec> {

    @Select("SELECT spec_id FROM tb_category_spec WHERE category_id = #{categoryId}")
    List<Integer> findSpecIdsByCategoryId(@Param("categoryId") Integer categoryId);

}