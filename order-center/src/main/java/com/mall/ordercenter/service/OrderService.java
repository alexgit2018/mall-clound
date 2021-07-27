package com.mall.ordercenter.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall.ordercenter.dao.ChardancemallOrderMapper;
import com.mall.ordercenter.domain.ChardancemallOrder;
import com.mall.ordercenter.domain.ChardancemallOrderExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单DB操作类
 */
@Service
public class OrderService {

    @Resource
    private ChardancemallOrderMapper orderMapper;

    /**
     * 获取订单列表
     *
     * @param userId
     * @param orderStatus 0，全部订单；
     *                    1，待付款；
     *                    2，待发货；
     *                    3，待收货；
     *                    4，待评价。
     * @param pageIndex
     * @param pageSize
     * @param order
     * @param sort
     * @return
     */
    public List<ChardancemallOrder> getOrderList(Integer userId, Integer orderStatus, Integer pageIndex, Integer pageSize, String order, String sort) {
        ChardancemallOrderExample example = new ChardancemallOrderExample();
        ChardancemallOrderExample.Criteria criteria = example.or();
        if (orderStatus == -1) {
            criteria.andUserIdEqualTo(userId).andDeletedEqualTo(false);
        } else {
            criteria.andUserIdEqualTo(userId).andOrderStatusEqualTo(orderStatus.shortValue()).andDeletedEqualTo(false);
        }
        if (!StringUtils.isEmpty(order) && !StringUtils.isEmpty(sort)) {
            example.setOrderByClause(order + " " + sort);
        }
        PageHelper.startPage(pageIndex, pageSize);
        return orderMapper.selectByExample(example);
    }

    public int count() {
        ChardancemallOrderExample example = new ChardancemallOrderExample();
        example.or().andDeletedEqualTo(false);
        return (int) orderMapper.countByExample(example);
    }

    /**
     * 订单集合
     *
     * @return
     */
    public List<ChardancemallOrder> getOrders(Integer userId) {
        ChardancemallOrderExample example = new ChardancemallOrderExample();
        example.or().andUserIdEqualTo(userId).andDeletedEqualTo(false);
        return orderMapper.selectByExample(example);
    }

    public List<ChardancemallOrder> getOrdersNoConfirm(Short status) {
        ChardancemallOrderExample example = new ChardancemallOrderExample();
        example.or().andDeletedEqualTo(false).andOrderStatusEqualTo(status);
        return orderMapper.selectByExample(example);
    }

    /**
     * 订单详情
     *
     * @param adId
     * @return
     */
    public ChardancemallOrder getOrderById(Integer adId) {
        return orderMapper.selectByPrimaryKey(adId);
    }

    public ChardancemallOrder getOrderDetailById(Integer userId, Integer orderId) {
        ChardancemallOrderExample example = new ChardancemallOrderExample();
        example.or().andUserIdEqualTo(userId).andIdEqualTo(orderId).andDeletedEqualTo(false);
        return orderMapper.selectOneByExample(example);
    }

    /**
     * 订单详情通过订单号
     *
     * @param userId
     * @param ordersn
     * @return
     */
    public ChardancemallOrder getOrderDetailByOrdersn(Integer userId, String ordersn) {
        ChardancemallOrderExample example = new ChardancemallOrderExample();
        example.or().andUserIdEqualTo(userId).andOrderSnEqualTo(ordersn).andDeletedEqualTo(false);
        return orderMapper.selectOneByExample(example);
    }

    public PageInfo<ChardancemallOrder>  getOrderDetailByOrdersn(String userId, String ordersn, LocalDateTime start, LocalDateTime end,
                                                               List<Short> statusArr, Integer page, Integer limit, String order, String sort) {
        ChardancemallOrderExample example = new ChardancemallOrderExample();
        ChardancemallOrderExample.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(userId)){
            criteria.andUserIdEqualTo(Integer.parseInt(userId));
        }

        if(!StringUtils.isEmpty(ordersn)){
            criteria.andOrderSnEqualTo(ordersn);
        }

        if(statusArr !=null &&  statusArr.size() > 0){
            criteria.andOrderStatusIn(statusArr);
        }

        if(!StringUtils.isEmpty(start) && !StringUtils.isEmpty(end)){
            criteria.andAddTimeBetween(start,end);
        }
        criteria.andDeletedEqualTo(false);

        if(!StringUtils.isEmpty(order) && !StringUtils.isEmpty(sort)){
            example.setOrderByClause(order +" " + sort);
        }

        List<ChardancemallOrder> list = orderMapper.selectByExampleSelective(example);
        PageInfo<ChardancemallOrder> pageInfo = new PageInfo<>(list);
        return  pageInfo;
    }

    public int addOrder(ChardancemallOrder ad) {
        ad.setAddTime(LocalDateTime.now());
        return orderMapper.insertSelective(ad);
    }

    public int updateOrder(ChardancemallOrder ad) {
        ad.setUpdateTime(LocalDateTime.now());
        return orderMapper.updateByPrimaryKey(ad);
    }

    public int delOrderById(Integer adId) {
        return orderMapper.deleteByPrimaryKey(adId);
    }

    public int deleteOrderByUserIdAndId(Integer userId, Integer orderId) {

        ChardancemallOrderExample example = new ChardancemallOrderExample();
        example.or().andUserIdEqualTo(userId).andIdEqualTo(orderId).andDeletedEqualTo(false);
        return orderMapper.deleteByExample(example);
    }

    public int updateOrderByUserIdAndId(Integer userId, Integer orderId, Short status) {

        ChardancemallOrderExample example = new ChardancemallOrderExample();
        example.or().andUserIdEqualTo(userId).andIdEqualTo(orderId);

        ChardancemallOrder order = orderMapper.selectOneByExample(example);
        order.setOrderStatus(status);
        return orderMapper.updateByPrimaryKeySelective(order);
    }

    public int updateOrderByUserIdAndId(Integer orderId, Short status) {

        ChardancemallOrderExample example = new ChardancemallOrderExample();
        example.or().andIdEqualTo(orderId);

        ChardancemallOrder order = orderMapper.selectOneByExample(example);
        order.setOrderStatus(status);
        return orderMapper.updateByPrimaryKeySelective(order);
    }
}
