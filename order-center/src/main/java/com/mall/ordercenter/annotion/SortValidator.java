package com.mall.ordercenter.annotion;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class SortValidator implements ConstraintValidator<Sort, String> {

    private List<String> vList;

    @Override
    public void initialize(Sort sort) {
        vList = new ArrayList<>();
        vList.clear();
        for (String val : sort.accepts()) {
            vList.add(val.toUpperCase());
        }
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (vList.contains(value.toUpperCase())) {
            return true;
        }

        return false;
    }
}
