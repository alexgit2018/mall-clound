package com.mall.ordercenter.annotion;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class OrderValidator implements ConstraintValidator<Order, String> {

    private List<String> vlist;

    @Override
    public void initialize(Order order) {
        vlist = new ArrayList<>();
        vlist.clear();
        for (String val : order.accepts()) {
            vlist.add(val.toUpperCase());
        }
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (vlist.contains(value.toUpperCase())) {
            return true;
        }

        return false;
    }
}
