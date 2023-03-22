package com.aimer.seckill.vo;

import com.aimer.seckill.utils.ValidatorUtils;
import com.aimer.seckill.utils.validator.IsMobile;
import org.thymeleaf.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author :覃玉锦
 * @create :2023-03-20 14:09:00
 */
public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {
    private boolean required = true;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (required) {
            return ValidatorUtils.isMobile(value);
        } else {
            if (StringUtils.isEmpty(value)) return true;
            return ValidatorUtils.isMobile(value);
        }
    }

    @Override
    public void initialize(IsMobile constraintAnnotation) {
        required = constraintAnnotation.required();
    }
}
