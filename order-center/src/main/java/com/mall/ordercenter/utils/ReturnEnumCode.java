package com.mall.ordercenter.utils;

public enum ReturnEnumCode {
    MOBILE_CHECKCODE_NOT_EXPIRED(700, "手机验证码未过期"),
    MOBILE_CHECKCODE_EXPIRED(701, "手机验证码已过期"),
    MOBILE_NOTEXIST(702, "手机号码不存在"),
    MOBILE_CHECKCODE_ERROR(703, "手机号码不存在"),

    SAME_USERNAME(710, "该用户名已被占用"),
    RESET_PASSWORD_SUCCESS(711, "密码重置成功"),
    USER_REGISTER_SUCCESS(712, "用户注册成功"),

    VERTIFY_CODE_INVALID(720, "验证码无效"),
    VERTIFY_CODE_ERROR(721, "验证码无效"),
    GOODS_IS_NOT_ONSALE(723, "商品已下架或不存在"),
    GOODS_IS_NOT_ENOUGH_STOCK(724, "商品库存不足"),

    COUPON_IS_EMPTY(800, "优惠券已领完"),
    COUPON_IS_LIMITED(801, "优惠券已领取"),
    IS_USER_COUPON(802, "新用户优惠券注册成功自动发放"),
    COUPON_IS_OUT(803, "优惠券已下架"),
    COUPON_IS_EXPIRED(804, "优惠券已过期"),

    BUY_NOW_FAILED(900, "立即购买失败"),
    CART_LIST_FAILED(901, "购买车清单失败"),
    RECEIVE_ADDRESS_EMPTY(902, "收货地址为空"),
    GROUPON_RULE_INVALID(903, "团购无效"),
    EXIST_GROUPN_EXPIRED_GOODS(904, "团购过期的商品"),
    INVALID_COUPON(905, "无效优惠券"),
    COUPON_CODE_IS_CHECKED(906, "优惠券已兑换"),
    COUPON_IS_NOT_EXIST(907, "优惠券不存在"),
    ORDER_NOT_EXIST(1000, "订单不存在"),
    ORDER_INVALID_EXIST(1001, "用户无效订单");

    private int code = 0;
    private String message = "";

    ReturnEnumCode(int code, String msg) {
        this.code = code;
        this.message = msg;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return message;
    }
}
