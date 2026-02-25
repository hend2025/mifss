package cn.hsa.ims.common.utils.gm;

import lombok.Data;

@Data
public class GenKeyDto {
    private byte[] privateKey;
    private byte[] publicKey;
}
