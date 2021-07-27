package com.mall.ordercenter.utils;

import com.mall.ordercenter.model.OrderOP;

public enum OrderEnum {

    IS_NOT_PAY(0, "未支付"),
    IS_PAID(1, "已支付未发货"),
    IS_DELIVERIED(2, "已发货"),
    IS_CONFIRMED(3, "确认收货"),
    IS_REFUNDING(4, "退款中"),
    IS_REFUNDED(6, "已退款"),
    IS_RETURNING_GOODS(7, "退货中"),
    IS_RETURNED_GOODS(8, "已退货"),
    IS_RETURNING_REFUND_GOODSING(9, "退款货中"),
    IS_RETURNED_REFUND_GOODSING(10, "已退款货"),
    IS_CLOSED(11, "订单关闭"),
    IS_RECEIVED(12, "已收货"),
    IS_CANCELED(13, "已取消");
    private Integer code = 0;
    private String message = "";

    OrderEnum(int code, String msg) {
        this.code = code;
        this.message = msg;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMessage() {
        return message;
    }

    public static String getMessageByCode(Integer code) {

        OrderEnum[] orderEnums = values();
        for (OrderEnum orderEnum : orderEnums) {
            if (orderEnum.getCode().equals(code)) {
                return orderEnum.getMessage();
            }
        }
        return "";
    }

    public static Integer getCodeByMessage(String message) {

        OrderEnum[] orderEnums = values();
        for (OrderEnum orderEnum : orderEnums) {
            if (orderEnum.getMessage().equals(message)) {
                return orderEnum.getCode();
            }
        }

        return -1;
    }

    public static OrderOP build(Integer status) {
        OrderOP handleOption = new OrderOP();

        if (status == 0) {
            // 如果订单没有被取消，且没有支付，则可支付，可取消
            handleOption.setCancel(true);
            handleOption.setPay(true);
        } else if (status == 13 || status == 11) {
            // 如果订单已经取消或是已关闭，则可删除
            handleOption.setDelete(true);
        } else if (status == 1) {
            // 如果订单已付款，没有发货，则可退款
            handleOption.setRefund(true);
        } else if (status == 7 || status == 9) {
            // 如果订单申请退款中或退款退货，没有相关操作
        } else if (status == 6 || status == 7) {
            // 如果订单已经退款,退货，则可删除
            handleOption.setDelete(true);
        } else if (status == 2) {
            // 如果订单已经发货，没有收货，则可收货操作,
            // 此时不能取消订单
            handleOption.setConfirm(true);
        } else if (status == 12) {
            // 如果订单已经支付，且已经收货，则可删除、去评论、申请售后和再次购买
            handleOption.setDelete(true);
            handleOption.setComment(true);
            handleOption.setRebuy(true);
            handleOption.setAftersale(true);
        } else {
            throw new IllegalStateException("status不支持");
        }

        return handleOption;
    }

}
