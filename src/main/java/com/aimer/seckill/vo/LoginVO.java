package com.aimer.seckill.vo;

import com.aimer.seckill.utils.validator.IsMobile;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @author :覃玉锦
 * @create :2023-03-14 15:53:00
 */
@Data
public class LoginVO {
    @NotNull
    @IsMobile
    private String mobile;

    @NotNull
    @Length(min = 2)
    private String password;
}
