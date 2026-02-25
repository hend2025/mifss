package com.aeye.mifss.common.validator;

import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.StrUtil;
import com.aeye.mifss.common.annotation.IdCard;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IdCardValidator implements ConstraintValidator<IdCard, String> {

    private boolean allowBlank;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(allowBlank && StrUtil.isBlank(value)){
            return true;
        }
        return IdcardUtil.isValidCard(value);
    }

    @Override
    public void initialize(IdCard constraintAnnotation) {
        allowBlank = constraintAnnotation.allowBlank();
    }
}
