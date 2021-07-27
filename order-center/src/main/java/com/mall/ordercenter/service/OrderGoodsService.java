package com.mall.ordercenter.service;

import com.mall.ordercenter.dao.ChardancemallOrderGoodsMapper;
import com.mall.ordercenter.domain.ChardancemallOrderGoods;
import com.mall.ordercenter.domain.ChardancemallOrderGoodsExample;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单商品DB操作类
 */
@Service
public class OrderGoodsService {

    @Resource
    private ChardancemallOrderGoodsMapper orderGoodsMapper;

    @Autowired
    private SqlSessionFactory factory;

    public int add(ChardancemallOrderGoods orderGoods) {
        orderGoods.setAddTime(LocalDateTime.now());
        orderGoods.setUpdateTime(LocalDateTime.now());
        return orderGoodsMapper.insertSelective(orderGoods);
    }

    @Transactional
    public void addBatch(List<ChardancemallOrderGoods> orderGoodList) {
        SqlSession sqlSession = factory.openSession(ExecutorType.BATCH);
        ChardancemallOrderGoodsMapper mapper = sqlSession.getMapper(ChardancemallOrderGoodsMapper.class);
        for (ChardancemallOrderGoods item : orderGoodList) {
            mapper.insertSelective(item);
        }
        sqlSession.flushStatements();
    }

    public List<ChardancemallOrderGoods> getByOrderId(String ordersn) {
        ChardancemallOrderGoodsExample example = new ChardancemallOrderGoodsExample();
        example.or().andOrderSnEqualTo(ordersn).andDeletedEqualTo(false);
        return orderGoodsMapper.selectByExample(example);
    }


    public List<ChardancemallOrderGoods> getByOrderId(Integer id) {
        ChardancemallOrderGoodsExample example = new ChardancemallOrderGoodsExample();
        example.or().andIdEqualTo(id).andDeletedEqualTo(false);
        return orderGoodsMapper.selectByExample(example);
    }

    public List<ChardancemallOrderGoods> getByOrderIdAndGId(String ordersn, Integer goodsId) {
        ChardancemallOrderGoodsExample example = new ChardancemallOrderGoodsExample();
        example.or().andOrderSnEqualTo(ordersn).andGoodsIdEqualTo(goodsId).andDeletedEqualTo(false);
        return orderGoodsMapper.selectByExample(example);
    }

    public ChardancemallOrderGoods findById(Integer id) {
        return orderGoodsMapper.selectByPrimaryKey(id);
    }

    public void updateById(ChardancemallOrderGoods orderGoods) {
        orderGoods.setUpdateTime(LocalDateTime.now());
        orderGoodsMapper.updateByPrimaryKeySelective(orderGoods);
    }

    public Short getComments(String ordersn) {
        ChardancemallOrderGoodsExample example = new ChardancemallOrderGoodsExample();
        example.or().andOrderSnEqualTo(ordersn).andDeletedEqualTo(false);
        long count = orderGoodsMapper.countByExample(example);
        return (short) count;
    }

    public boolean checkExist(Integer goodsId) {
        ChardancemallOrderGoodsExample example = new ChardancemallOrderGoodsExample();
        example.or().andGoodsIdEqualTo(goodsId).andDeletedEqualTo(false);
        return orderGoodsMapper.countByExample(example) != 0;
    }

    public void deleteByOrderId(String ordersn) {
        ChardancemallOrderGoodsExample example = new ChardancemallOrderGoodsExample();
        example.or().andOrderSnEqualTo(ordersn).andDeletedEqualTo(false);
        orderGoodsMapper.logicalDeleteByExample(example);
    }
}
