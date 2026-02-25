package cn.hsa.ims.common.utils.gm;

import lombok.Data;

@Data
public class EncryptDto {

    private String appId;

    private String accessToken;

    private Long timestamp;

    private String data;
}
