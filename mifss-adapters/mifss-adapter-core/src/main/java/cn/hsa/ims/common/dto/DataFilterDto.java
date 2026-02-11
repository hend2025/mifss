package cn.hsa.ims.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public class DataFilterDto implements Serializable {
    private Map<String, String> columnSql = new HashMap<>();
    private static final String EMPTY = "()";

    public String buildSql(){
        StringBuffer sqlFilterBuf = new StringBuffer("(");
        columnSql.forEach((columnKey, sqlStr) -> {
            if (sqlFilterBuf.length() > 1) {
                sqlFilterBuf.append(" and ");
            }
            sqlFilterBuf.append(sqlStr);
        });
        sqlFilterBuf.append(")");
        if(!sqlFilterBuf.toString().equals(EMPTY)){
            return sqlFilterBuf.toString();
        }
        return null;
    }
}
