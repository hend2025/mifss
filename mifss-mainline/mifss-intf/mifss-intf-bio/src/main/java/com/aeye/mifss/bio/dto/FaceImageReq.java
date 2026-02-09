package com.aeye.mifss.bio.dto;

import java.io.Serializable;

/**
 * Face Image Request DTO
 */
public class FaceImageReq implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Base64 Image Data
     */
    private String imageData;

    public String getImageData() {
        return imageData;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }
}
