package com.xdurxs.rxs.auth.model.vo.verificationcode;

import com.xdurxs.framework.common.validator.PhoneNumber;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SendVerificationCodeReqVO {
    @NotBlank(message = "手机号不能为空")
    @PhoneNumber
    private String phone;
}
