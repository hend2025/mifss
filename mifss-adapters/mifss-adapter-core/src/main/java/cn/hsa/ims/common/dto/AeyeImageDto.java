package cn.hsa.ims.common.dto;

import lombok.Data;

@Data
public class AeyeImageDto {
    /**
     * 默认旋转角度
     */
    private Integer degree = 35;

    /**
     * 是否铺满
     */
    private boolean full = false;

    /**
     * 水印之间的间隔
     */
    private int xMove = 50;

    /**
     * 水印之间的间隔
     */
    private int yMove = 60;
}
