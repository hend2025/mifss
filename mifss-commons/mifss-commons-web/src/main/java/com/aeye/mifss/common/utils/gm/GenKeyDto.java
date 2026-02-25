package com.aeye.mifss.common.utils.gm;

import lombok.Data;

@Data
public class GenKeyDto {
    private byte[] privateKey;
    private byte[] publicKey;
}
