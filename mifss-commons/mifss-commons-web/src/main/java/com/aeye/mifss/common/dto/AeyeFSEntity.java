package com.aeye.mifss.common.dto;

import cn.hsa.hsaf.core.fsstore.FSEntity;
import lombok.Data;

@Data
public class AeyeFSEntity extends FSEntity {
    private byte[] data;
}
