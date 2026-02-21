package cn.hsa.ims.common.dto;

import lombok.Data;

import java.util.Map;

@Data
public class RequestBodyDto {
    private String requestBody;
    private String requestParam;
    private String base64String;
    private Map<String, Object> mapBody;
}
