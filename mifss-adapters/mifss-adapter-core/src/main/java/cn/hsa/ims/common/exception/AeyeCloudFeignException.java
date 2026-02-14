package cn.hsa.ims.common.exception;

import lombok.Data;

import java.io.IOException;

@Data
public class AeyeCloudFeignException extends IOException {

    int code = -1;

    public AeyeCloudFeignException() {
    }

    public AeyeCloudFeignException(int code) {
        this.code = code;
    }

    public AeyeCloudFeignException(String message) {
        super(message);
    }

    public AeyeCloudFeignException(int code, String message) {
        super(message);
        this.code = code;
    }

    public AeyeCloudFeignException(Throwable cause) {
        super(cause);
    }

    public AeyeCloudFeignException(String message, Throwable cause) {
        super(message, cause);
    }

    public AeyeCloudFeignException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
