package cn.hsa.ims.common.exception;

import lombok.Data;

@Data
public class ExceptionItem{

    private int code;
    private String msg;

    private ExceptionItem(){

    }

    private ExceptionItem(String msg, int code){
            this.msg=msg;
            this.code=code;
    }

    public static ExceptionItem build(int code, String msg){
        return new ExceptionItem(msg, code);
    }

    @Override
    public String toString() {
        return this.msg;
    }
}
