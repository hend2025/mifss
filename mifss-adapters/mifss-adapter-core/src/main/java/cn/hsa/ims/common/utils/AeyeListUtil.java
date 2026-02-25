package cn.hsa.ims.common.utils;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

public class AeyeListUtil {
    /**
     * 拆分集合-每个集合数量不超过splitSize
     * @param datas
     * @param splitSize
     * @param <T>
     * @return
     */
    public static  <T> List<List<T>> spliceArrays(List<T> datas, int splitSize) {
        if (datas == null || splitSize < 1) {
            return  null;
        }
        int totalSize = datas.size();
        //获取要拆分子数组个数
        int count = (totalSize % splitSize == 0) ?
                (totalSize / splitSize) : (totalSize/splitSize+1);

        System.out.println("split count = " +count);

        List<List<T>> rows = new ArrayList();
        for (int i = 0;i < count;i ++) {

            int index = i * splitSize;
            List<T> cols = new ArrayList();
            int j = 0;
            while (j < splitSize && index < totalSize) {
                cols.add(datas.get(index++));
                j ++;
            }
            rows.add(cols);
        }
        return rows;
    }

    public static void main(String[] args){
        List<String> execItem = new ArrayList<>();
        execItem.add("1");
        execItem.add("2");
        execItem.add("3");
        execItem.add("4");
        execItem.add("5");
        Object obj = AeyeListUtil.spliceArrays(execItem, 2);
        System.out.println(JSON.toJSON(obj));
    }
}
