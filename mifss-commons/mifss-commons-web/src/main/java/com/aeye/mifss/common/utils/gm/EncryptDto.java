package com.aeye.mifss.common.utils.gm;

import lombok.Data;

@Data
public class EncryptDto {

    private String appId;

    private String accessToken;

    private Long timestamp;

    private String data;
}
