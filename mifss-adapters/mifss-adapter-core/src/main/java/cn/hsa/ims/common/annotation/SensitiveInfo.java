package cn.hsa.ims.common.annotation;

import cn.hsa.ims.common.enums.SensitiveTypeEnums;
import cn.hsa.ims.common.utils.SensitiveInfoSerialize;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = SensitiveInfoSerialize.class)
public @interface SensitiveInfo {

    public SensitiveTypeEnums value();
}
