package com.ashin.service.impl;

import com.ashin.entity.PageResult;
import com.ashin.entity.QueryPageBean;
import com.ashin.entity.Result;
import com.ashin.exception.CustomException;
import com.ashin.mapper.GoodsMapper;
import com.ashin.pojo.Goods;
import com.ashin.service.GoodsService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        //设置分页参数
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());

        //根据条件查询
        Example example = new Example(Goods.class);
        if(!StringUtils.isEmpty(queryPageBean.getQueryString())){
            Example.Criteria criteria = example.createCriteria();
            criteria.andLike("name","%"+queryPageBean.getQueryString()+"%");
        }
        List<Goods> goods = goodsMapper.selectByExample(example);

        PageInfo pageInfo = new PageInfo(goods);
        return new PageResult(pageInfo.getTotal(),pageInfo.getList());
    }

    @Override
    public Goods findById(Integer id) {
        return goodsMapper.selectByPrimaryKey(id);
    }

    @Override
    public void add(Goods goods) {
        goods.setIsMarketable("0");//默认未上架
        goods.setStatus("0");//默认未审核
        goods.setCreatetime(new Date());
        goods.setUpdatetime(new Date());
        goods.setSaleNum(0);
        goods.setCommentNum(0);
        String images = goods.getImages();
        if(images != null){
            String[] imgArr = images.split(",");
            if(imgArr != null && imgArr.length>0){
                goods.setImage(imgArr[0]);
            }
        }
        goodsMapper.insert(goods);//执行插入
    }
    @Override
    public void upGoods(Integer id) throws Exception {
        //查询商品的状态是否是已审核
        Goods goods = goodsMapper.selectByPrimaryKey(id);
        if(!"1".equals(goods.getStatus())){
            throw new CustomException("当前商品状态异常,不能进行上架操作");
        }
        goods.setIsMarketable("1");
        goods.setUpdatetime(new Date());
        goodsMapper.updateByPrimaryKey(goods); //更新数据
    }

    @Override
    public void downGoods(Integer id) throws Exception{
        //查询商品的状态是否是已审核
        Goods goods = goodsMapper.selectByPrimaryKey(id);
        if(!"1".equals(goods.getIsMarketable())){
            throw new CustomException("当前商品无需下架操作");
        }
        goods.setIsMarketable("0");
        goods.setUpdatetime(new Date());
        goodsMapper.updateByPrimaryKey(goods); //更新数据
    }
    @Override
    public void delete(Integer id) {
        //查询商品的上架状态 ;
        Goods goods = goodsMapper.selectByPrimaryKey(id);
        if("1".equals(goods.getIsMarketable())){
            throw new CustomException("当前商品处于上架状态, 请先下架该商品, 再删除");
        }
        goodsMapper.deleteByPrimaryKey(id);//执行删除
    }
    @Override
    public void updateStatus(Integer id ,  String status) {
        Goods goods = new Goods();
        goods.setId(id);
        goods.setStatus(status);
        goods.setUpdatetime(new Date());

        goodsMapper.updateByPrimaryKeySelective(goods);//修改商品的审核状态
    }
    @Override
    public List<Goods> findAll() {
        return goodsMapper.selectAll();
    }




    @Override
    public Result buy(Integer goodId, Integer buyNum) {
        // 查询商品
        Goods goods = goodsMapper.selectByPrimaryKey(goodId);
        if (goods == null) return new Result(false,"该商品不存在");
        // 检查库存
        if(goods.getNum() < buyNum){
            return new Result(false,"商品库存不足");
        }
        // 扣减库存
        goods.setNum(goods.getNum() - 1);
        goodsMapper.updateByPrimaryKey(goods);
        // 返回
        return new Result(true,"商品扣减库存成功");
    }
}