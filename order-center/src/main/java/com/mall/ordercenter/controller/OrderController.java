package com.mall.ordercenter.controller;

import com.mall.ordercenter.annotion.LoginUser;
import com.mall.ordercenter.annotion.Order;
import com.mall.ordercenter.annotion.Sort;
import com.mall.ordercenter.domain.ChardancemallOrder;
import com.mall.ordercenter.domain.ChardancemallOrderGoods;
import com.mall.ordercenter.model.OrderGoodsModel;
import com.mall.ordercenter.model.OrderListModel;
import com.mall.ordercenter.service.OrderGoodsService;
import com.mall.ordercenter.service.OrderService;

import com.mall.ordercenter.utils.OrderEnum;
import com.mall.ordercenter.utils.ResponseUtil;
import com.mall.ordercenter.utils.ReturnEnumCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 订单
 */
@RestController
@RequestMapping("/api/order")
public class OrderController {

    private Logger logger = LoggerFactory.getLogger(OrderController.class);


    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderGoodsService orderGoodsService;


    /**
     * 订单列表
     *
     * @param userId
     * @param showType 订单信息：
     *                 0，全部订单；
     *                 1，待付款；
     *                 2，待发货；
     *                 3，待收货；
     *                 4，待评价；
     * @param page
     * @param limit
     * @param sort
     * @param order
     * @return
     */
    @GetMapping("getOrderlist")
    public Object getOrderlist(@LoginUser Integer userId,
                               @RequestParam(defaultValue = "0") Integer showType,
                               @RequestParam(defaultValue = "1") Integer page,
                               @RequestParam(defaultValue = "10") Integer limit,
                               @Sort @RequestParam(defaultValue = "add_time") String sort,
                               @Order @RequestParam(defaultValue = "desc") String order) {
        try {

            List<ChardancemallOrder> orderList = orderService.getOrderList(userId, showType - 1, page, limit, sort, order);
            List<Map<String, Object>> datalist = new ArrayList<>();
            datalist.clear();

            List<OrderListModel> orders = new ArrayList<>();
            orders.clear();
            Map<String, Object> mapdata = new HashMap<>();
            Map<String, Object> goodsmap = new HashMap<>();
            for (ChardancemallOrder orderitem : orderList) {
                OrderListModel model = new OrderListModel();
                model.setId(orderitem.getId());
                model.setOrderSn(orderitem.getOrderSn());
                model.setOrderStatus(orderitem.getOrderStatus().intValue());
                model.setOrderStatusText(OrderEnum.getMessageByCode(orderitem.getOrderStatus().intValue()));
                model.setOrderOp(OrderEnum.build(orderitem.getOrderStatus().intValue()));
                model.setAftersaleStatus(orderitem.getAftersaleStatus().intValue());
                model.setActualPrice(orderitem.getActualPrice());

                //调用微服务
//                ChardancemallGroupon groupon = grouponService.getByOrderId(orderitem.getId());
//                if (groupon != null) {
//                    //mapdata.put("IsGroupon",true);
//                    model.setGroupon(true);
//                } else {
//                    //mapdata.put("IsGroupon",false);
//                    model.setGroupon(false);
//                }

                List<ChardancemallOrderGoods> orderGoods = orderGoodsService.getByOrderId(orderitem.getOrderSn());
                List<Map<String, Object>> goodsdata = new ArrayList<>();
                goodsdata.clear();
                for (ChardancemallOrderGoods ordergooditem : orderGoods) {
                    OrderGoodsModel goodsModel = new OrderGoodsModel();
                    goodsModel.setId(ordergooditem.getId());
                    goodsModel.setGoodsName(ordergooditem.getGoodsName());
                    goodsModel.setNumber(ordergooditem.getNumber().intValue());
                    goodsModel.setPicUrl(ordergooditem.getPicUrl());
                    goodsModel.setSpecifications(ordergooditem.getSpecifications());
                    goodsModel.setPrice(ordergooditem.getPrice());

                    model.orderGoods.add(goodsModel);
                }

                orders.add(model);
            }

            return ResponseUtil.ok(orders);
        } catch (Exception ex) {
            logger.error("订单列表错误" + ex.getMessage());
        }
        return ResponseUtil.fail();
    }

    /**
     * 订单详情
     *
     * @param userId  用户ID
     * @param orderId 订单ID
     * @return 订单详情
     */
    @GetMapping("getOrderDetail")
    public Object getOrderDetail(@LoginUser Integer userId, Integer orderId) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }

        // 订单信息
        ChardancemallOrder order = orderService.getOrderDetailById(userId, orderId);
        if (null == order) {
            return ResponseUtil.fail(ReturnEnumCode.ORDER_NOT_EXIST.getCode(),
                    ReturnEnumCode.ORDER_NOT_EXIST.getMessage());
        }
        if (!order.getUserId().equals(userId)) {
            return ResponseUtil.fail(ReturnEnumCode.ORDER_INVALID_EXIST.getCode(),
                    ReturnEnumCode.ORDER_INVALID_EXIST.getMessage());
        }
        Map<String, Object> orderVo = new HashMap<String, Object>();
        orderVo.put("id", order.getId());
        orderVo.put("orderSn", order.getOrderSn());
        orderVo.put("message", order.getMessage());
        orderVo.put("addTime", order.getAddTime());
        orderVo.put("consignee", order.getConsignee());
        orderVo.put("mobile", order.getMobile());
        orderVo.put("address", order.getAddress());
        orderVo.put("goodsPrice", order.getGoodsPrice());
        orderVo.put("couponPrice", order.getCouponPrice());
        orderVo.put("freightPrice", order.getFreightPrice());
        orderVo.put("actualPrice", order.getActualPrice());
        orderVo.put("orderStatusText", OrderEnum.getMessageByCode(order.getOrderStatus().intValue()));
        orderVo.put("orderOp", OrderEnum.build(order.getOrderStatus().intValue()));
        orderVo.put("aftersaleStatus", order.getAftersaleStatus());
        orderVo.put("expCode", order.getShipChannel());
//        orderVo.put("expName", expressService.getVendorName(order.getShipChannel()));
        orderVo.put("expNo", order.getShipSn());

        List<ChardancemallOrderGoods> orderGoodsList = orderGoodsService.getByOrderId(order.getOrderSn());

        Map<String, Object> result = new HashMap<>();
        result.put("orderInfo", orderVo);
        result.put("orderGoods", orderGoodsList);

        // 订单状态为已发货且物流信息不为空
        //"YTO", "800669400640887922"
        //调用微服务
//        if (order.getOrderStatus().equals(OrderEnum.IS_DELIVERIED.getCode())) {
//            String expressInfo = expressService.getExpressInfo(order.getShipChannel(), order.getShipSn());
//            if (expressInfo == null) {
//                result.put("expressInfo", new ArrayList<>());
//            } else {
//                result.put("expressInfo", expressInfo);
//            }
//        } else {
//            result.put("expressInfo", new ArrayList<>());
//        }

        return ResponseUtil.ok(result);

    }

    /**
     * @param userId
     * @param ordersn
     * @return
     */
    @GetMapping("getOrderSampleDetail")
    public Object getOrderSampleDetail(@LoginUser Integer userId, String ordersn) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }

        // 订单信息
        ChardancemallOrder order = orderService.getOrderDetailByOrdersn(userId, ordersn);
        if (null == order) {
            return ResponseUtil.fail(ReturnEnumCode.ORDER_NOT_EXIST.getCode(),
                    ReturnEnumCode.ORDER_NOT_EXIST.getMessage());
        }
        if (!order.getUserId().equals(userId)) {
            return ResponseUtil.fail(ReturnEnumCode.ORDER_INVALID_EXIST.getCode(),
                    ReturnEnumCode.ORDER_INVALID_EXIST.getMessage());
        }

        Map<String, Object> orderVo = new HashMap<String, Object>();
        orderVo.put("actualPrice", order.getActualPrice());
        return ResponseUtil.ok(orderVo);

    }


    /**
     * 提交订单
     *
     * @param userId
     * @param productIds 商品ID 集合
     * @param addressId
     * @param couponId
     * @param integral
     * @return
     */
    @PostMapping("submitorder")
    public Object submitorder(@LoginUser Integer userId, String productIds, Integer deliveryType, Integer addressId,
                              Integer couponId, double integral, String message) {
        try {
            if (userId == null) {
                return ResponseUtil.unlogin();
            }

            if (addressId == null || addressId.equals(0)) {
                return ResponseUtil.fail(ReturnEnumCode.RECEIVE_ADDRESS_EMPTY.getCode(),
                        ReturnEnumCode.RECEIVE_ADDRESS_EMPTY.getMessage());
            }
            //调用微服务
//            ChardancemallAddress address = adressService.getAddressById(addressId);
//            if (address == null) {
//                return ResponseUtil.fail(ReturnEnumCode.RECEIVE_ADDRESS_EMPTY.getCode(),
//                        ReturnEnumCode.RECEIVE_ADDRESS_EMPTY.getMessage());
//            }
//
//            //订单中的商品
//            List<String> productlist = Arrays.asList(productIds.split(","));
//            if (productlist == null || productlist.size() == 0) {
//                return ResponseUtil.badArgument();
//            }
//
//            BigDecimal total = new BigDecimal(0.00);
//            BigDecimal groupprice = new BigDecimal(0.00);
//            List<Integer> idints = productlist.stream().map(Integer::parseInt).collect(Collectors.toList());
//            List<ChardancemallCart> checkedGoodsList = cartService.getCartProductsByUserIdAndIds(userId, idints);
//            //商品或商品品类ID 集合
//            List<Integer> ids = new ArrayList<>();
//            ids.clear();
//
//            List<ChardancemallOrderGoods> orderGoodsList = new ArrayList<>();
//            orderGoodsList.clear();
//
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//            //订单金额
//            BigDecimal orderTotalPrice = new BigDecimal(0.00);
//            //订单中有团购商品
//            BigDecimal checkedGoodsPrice = new BigDecimal(0.00);
//            for (ChardancemallCart cart : checkedGoodsList) {
//
//                ChardancemallGrouponRules grouponRules = grouponRuleService.getGrouponGoodsById(cart.getGoodsId());
//                if (grouponRules != null) {
//                    if (grouponRules.getStatus() == 1 && grouponRules.getExpireTime().isAfter(LocalDateTime.now())) {
//                        return ResponseUtil.fail(ReturnEnumCode.EXIST_GROUPN_EXPIRED_GOODS.getCode(),
//                                ReturnEnumCode.EXIST_GROUPN_EXPIRED_GOODS.getMessage());
//                    }
//                }
//                //只有当团购规格商品ID符合才进行团购优惠
//                if (grouponRules != null && grouponRules.getGoodsId().equals(cart.getGoodsId())) {
//                    groupprice = groupprice.add(grouponRules.getDiscount());
//                    checkedGoodsPrice = checkedGoodsPrice.add(cart.getPrice().subtract(grouponRules.getDiscount()).multiply(new BigDecimal(cart.getNumber())));
//                } else {
//                    checkedGoodsPrice = checkedGoodsPrice.add(cart.getPrice().multiply(new BigDecimal(cart.getNumber())));
//                }
//                ids.add(cart.getGoodsId());
//                //orderTotalPrice = orderTotalPrice.add(cart.getPrice());
//            }
//
//            //优惠券金额
//            BigDecimal plusTotalPrice = new BigDecimal(0.00);
//            orderTotalPrice = checkedGoodsPrice;
//
//            BigDecimal couponPrice = new BigDecimal(0.00);
//
//            //计算优惠券可用情况
//            BigDecimal tmpCouponPrice = new BigDecimal(0.00);
//            Integer tmpCouponId = 0;
//            Integer tmpUserCouponId = 0;
//            int tmpCouponLength = 0;
//            if (!StringUtils.isEmpty(couponId)) {
//                ChardancemallCouponUser usecoupon = userCouponService.getCouponByUserAndCId(userId, couponId);
//                if (usecoupon == null) {
//                    return ResponseUtil.fail(ReturnEnumCode.INVALID_COUPON.getCode(),
//                            ReturnEnumCode.INVALID_COUPON.getMessage());
//                }
//                ChardancemallCoupon coupon = couponService.getCouponById(usecoupon.getCouponId());
//                if (coupon == null) {
//                    return ResponseUtil.fail(ReturnEnumCode.INVALID_COUPON.getCode(),
//                            ReturnEnumCode.INVALID_COUPON.getMessage());
//                }
//
//                if (coupon.getGoodsType() == 1) {
//                    ids = goodsService.getCategoryIdsByGoodsId(ids);
//                }
//
//                //优惠券
//                couponPrice = couponService.checkCouponIsValid(couponId, ids, usecoupon.getAddTime(), checkedGoodsPrice);
//                plusTotalPrice.add(couponPrice);
//            }
//
//            //运费
//            BigDecimal deliveryprice = new BigDecimal(0.00);
//            if (deliveryType != 5) {
//                ChardancemallDeliverycost deliverycost = deliveryCostService.getDeliveryCost(checkedGoodsPrice);
//                if (deliverycost != null) {
//                    deliveryprice = deliverycost.getDeliverycost();
//                }
//            }
//            // 可以使用的其他钱，例如用户积分
//            BigDecimal integralPrice = new BigDecimal(0.00);
//            //ChardancemallIntegralLog integralLog = userIntegralLogService.getUsingIntegralLogByUserId(userId,0);
//            //BigDecimal actualPrice = orderTotalPrice.subtract(plusTotalPrice).add(deliveryprice).subtract(integralPrice);
//            BigDecimal actualPrice = orderTotalPrice.subtract(plusTotalPrice).add(deliveryprice);
//
            DateTimeFormatter df = DateTimeFormatter.ofPattern("YYYMMddHHmmssSSS");
            LocalDateTime dt = LocalDateTime.now();
            String ordersn = dt.format(df);
//            ChardancemallOrder order = new ChardancemallOrder();
//            order.setUserId(userId);
//            order.setOrderSn(ordersn);
//            order.setActualPrice(actualPrice);
//            order.setConsignee(address.getName());
//            order.setMobile(address.getTel());
//            order.setAddress(address.getAddressDetail());
//            order.setMessage(message);
//            order.setGoodsPrice(orderTotalPrice);
//            order.setFreightPrice(deliveryprice);
//            order.setCouponPrice(couponPrice);
//            order.setIntegralPrice(integralPrice);
//            order.setGrouponPrice(groupprice);
//            order.setOrderStatus(OrderEnum.IS_NOT_PAY.getCode().shortValue());
//            order.setOrderPrice(orderTotalPrice.subtract(couponPrice).add(deliveryprice));
//            Integer nret = orderService.addOrder(order);
//            if (nret > 0) {
//                for (ChardancemallCart cart : checkedGoodsList) {
//                    ChardancemallOrderGoods goods = new ChardancemallOrderGoods();
//                    goods.setOrderSn(ordersn);
//                    goods.setGoodsName(cart.getGoodsName());
//                    goods.setGoodsId(cart.getGoodsId());
//                    goods.setGoodsSn(cart.getGoodsSn());
//                    goods.setProductId(cart.getProductId());
//                    goods.setNumber(cart.getNumber());
//                    goods.setPrice(cart.getPrice());
//                    goods.setSpecifications(cart.getSpecifications());
//                    goods.setPicUrl(cart.getPicUrl());
//                    goods.setAddTime(LocalDateTime.now());
//                    goods.setUpdateTime(LocalDateTime.now());
//                    cart.setDeleted(true);
//                    orderGoodsList.add(goods);
//                }
//                orderGoodsService.addBatch(orderGoodsList);
//
//                cartService.updateCartGoodsStatus(true, checkedGoodsList);
                return ResponseUtil.ok(ordersn);
//            }
        } catch (Exception ex) {
            logger.error("提交订单错误" + ex.getMessage());
        }

        return ResponseUtil.fail();
    }


    /**
     * 删除订单
     *
     * @param userId
     * @param orderId
     * @return
     */
    @PostMapping("deleteOrder")
    public Object deleteOrder(@LoginUser Integer userId, Integer orderId) {
        try {
            Integer nret = orderService.deleteOrderByUserIdAndId(userId, orderId);
            if (nret > 0) {
                return ResponseUtil.ok();
            }
        } catch (Exception ex) {
            logger.error("删除订单失败" + ex.getMessage());
        }
        return ResponseUtil.fail();
    }

    /**
     * 取消订单
     *
     * @param userId
     * @param orderId
     * @return
     */
    @PostMapping("cancelOrder")
    public Object cancelOrder(@LoginUser Integer userId, Integer orderId) {
        try {
            Short status = 1;
            Integer nret = updateOrderStatus(userId, orderId, status);
            if (nret > 0) {
                return ResponseUtil.ok();
            }
        } catch (Exception ex) {
            logger.error("取消订单失败" + ex.getMessage());
        }
        return ResponseUtil.fail();
    }

    /**
     * 确认收货
     *
     * @param userId
     * @param orderId
     * @return
     */
    @PostMapping("confirmReceiveOrder")
    public Object confirmReceiveOrder(@LoginUser Integer userId, Integer orderId) {
        try {
            Short status = 1;
            Integer nret = updateOrderStatus(userId, orderId, status);
            if (nret > 0) {
                return ResponseUtil.ok();
            }
        } catch (Exception ex) {
            logger.error("确定收货订单失败" + ex.getMessage());
        }
        return ResponseUtil.fail();
    }

    private int updateOrderStatus(Integer userId, Integer orderId, Short status) {
        return orderService.updateOrderByUserIdAndId(userId, orderId, status);
    }

    /**
     * 订单数量
     *
     * @param userId
     * @return
     */
    @GetMapping("getOrderNum")
    public Object getOrderNum(Integer userId) {
        try {
            List<ChardancemallOrder> orders = orderService.getOrders(userId);
            //未付款
            List<ChardancemallOrder> nopaidOrders = orders.stream().filter(t -> t.getOrderStatus() == OrderEnum.IS_NOT_PAY.getCode().shortValue()).collect(Collectors.toList());
            //待发货
            List<ChardancemallOrder> payOrders = orders.stream().filter(t -> t.getOrderStatus() == OrderEnum.IS_PAID.getCode().shortValue()).collect(Collectors.toList());
            //待收货
            List<ChardancemallOrder> shipOrders = orders.stream().filter(t -> t.getOrderStatus() == OrderEnum.IS_DELIVERIED.getCode().shortValue()).collect(Collectors.toList());
            //已收货
            List<ChardancemallOrder> confiirmOrders = orders.stream().filter(t -> t.getOrderStatus() == OrderEnum.IS_CONFIRMED.getCode().shortValue()).collect(Collectors.toList());

            Map<String, Integer> data = new HashMap<>();
            data.clear();
            data.put("unpaid", nopaidOrders.size());
            data.put("unship", payOrders.size());
            data.put("unrecv", shipOrders.size());
            data.put("uncomment", confiirmOrders.size());

            return ResponseUtil.ok(data);
        } catch (Exception ex) {
            logger.error("订单数量" + ex.getMessage());
        }

        return ResponseUtil.fail();
    }
}
