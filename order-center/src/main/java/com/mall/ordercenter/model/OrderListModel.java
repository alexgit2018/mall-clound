package com.mall.ordercenter.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
/**
 * 订单信息
 */
public class OrderListModel {

    private Integer id;
    private String orderSn;
    private Integer orderStatus;
    private BigDecimal actualPrice;
    private String orderStatusText;
    private OrderOP orderOp;
    private Integer aftersaleStatus;
    private Boolean IsGroupon;
    public List<OrderGoodsModel> orderGoods = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public BigDecimal getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(BigDecimal actualPrice) {
        this.actualPrice = actualPrice;
    }

    public String getOrderStatusText() {
        return orderStatusText;
    }

    public void setOrderStatusText(String orderStatusText) {
        this.orderStatusText = orderStatusText;
    }

    public OrderOP getOrderOp() {
        return orderOp;
    }

    public void setOrderOp(OrderOP orderOp) {
        this.orderOp = orderOp;
    }

    public Integer getAftersaleStatus() {
        return aftersaleStatus;
    }

    public void setAftersaleStatus(Integer aftersaleStatus) {
        this.aftersaleStatus = aftersaleStatus;
    }

    public Boolean getGroupon() {
        return IsGroupon;
    }

    public void setGroupon(Boolean groupon) {
        IsGroupon = groupon;
    }
}
