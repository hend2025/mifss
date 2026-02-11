package cn.hsa.ims.common.utils;


import cn.hsa.hsaf.core.framework.context.HsafContextHolder;
import cn.hsa.hsaf.core.framework.util.CurrentUser;
import cn.hutool.core.util.StrUtil;
import org.springframework.core.env.Environment;

import java.util.List;

public class AeyeLoginUtil {

    public static final String SPLIT_ORG_CODE_1 = "|";
    public static final String SPLIT_ORG_CODE_2 = "||";
    public static final String SPLIT_ORG_CODE_3 = "|||";

    /**
     * 获取当前登录的用户
     * @return
     */
    @Deprecated
    public static AeyeCurrentUser getCurrentUser(){

        CurrentUser currentUser = getHsafCurrentUser();
        AeyeCurrentUser aeyeCurrentUser = new AeyeCurrentUser();
        aeyeCurrentUser.setAdmDvs(currentUser.getAdmDvs());
        aeyeCurrentUser.setName(currentUser.getName());
        aeyeCurrentUser.setDeptName(currentUser.getDeptName());
        aeyeCurrentUser.setDeptID(currentUser.getDeptID());
        aeyeCurrentUser.setOrgCodg(convertCode(currentUser.getOrgCodg()));
        aeyeCurrentUser.setPoolAreaCodg(currentUser.getPoolAreaCodg());
        aeyeCurrentUser.setUactID(currentUser.getUactID());
        aeyeCurrentUser.setOrgName(currentUser.getOrgName());
        aeyeCurrentUser.setPrntOrgID(currentUser.getPrntOrgID());
        aeyeCurrentUser.setUserAcctID(currentUser.getUserAcctID());
        aeyeCurrentUser.setOrgUntID(currentUser.getOrgUntID());
        aeyeCurrentUser.setUserAcct(currentUser.getUserAcct());
        aeyeCurrentUser.InsuTypePoolAreaMap = currentUser.InsuTypePoolAreaMap;
        convertAdmdvs(aeyeCurrentUser);
        return aeyeCurrentUser;
    }

    public static CurrentUser getHsafCurrentUser(){
        CurrentUser user = HsafContextHolder.getContext().getCurrentUser();
        user.setOrgCodg(convertCode(user.getOrgCodg()));
        convertAdmdvs(user);
        return user;
    }

    private static void convertAdmdvs(CurrentUser user){
        if(converAdmDvs && StrUtil.isNotBlank(user.getAdmDvs())){
            if(user.getAdmDvs().endsWith("9900")){
                user.setAdmDvs(StrUtil.replace(user.getAdmDvs(), 2, 4, '0'));
            }
            if(user.getAdmDvs().endsWith("99")){
                user.setAdmDvs(StrUtil.replace(user.getAdmDvs(), 4, 6, '0'));
            }
        }
    }

    public static String getDeptId() {
        return getHsafCurrentUser().getDeptID();
    }


    public static String getDeptName() {
        return getHsafCurrentUser().getDeptName();
    }

    public static String getOrgCodg() {
        return convertCode(getHsafCurrentUser().getOrgCodg());
    }
    public static String getOrgName() {
        return getHsafCurrentUser().getOrgName();
    }

    public static String getUserId() {
        return getHsafCurrentUser().getUserAcctID();
    }

    public static String getUserName(){
        return getHsafCurrentUser().getUserAcct();
    }

    public static String getName(){
        return getHsafCurrentUser().getName();
    }

    public static String getOrgId(){
        return getHsafCurrentUser().getOrgUntID();
    }

    public static String getAdmDvs(){
        return getHsafCurrentUser().getAdmDvs();
    }

    private static String convertCode(String code){
        if(StrUtil.isNotBlank(code) && code.contains(SPLIT_ORG_CODE_1)){
            List<String> stt = StrUtil.split(code, SPLIT_ORG_CODE_1);
            return stt.size() > 0 ? stt.get(0) : code;
        }else if(StrUtil.isNotBlank(code) && code.contains(SPLIT_ORG_CODE_2)){
            List<String> stt = StrUtil.split(code, SPLIT_ORG_CODE_2);
            return stt.size() > 0 ? stt.get(0) : code;
        }else if(StrUtil.isNotBlank(code) && code.contains(SPLIT_ORG_CODE_3)){
            List<String> stt = StrUtil.split(code, SPLIT_ORG_CODE_3);
            return stt.size() > 0 ? stt.get(0) : code;
        }
        return code;
    }

    public static String[] getDeptNames(){
        return StrUtil.isNotBlank(getDeptName()) ? getDeptName().split(",") : null;
    }

    public static String[] getDeptIds(){
        return StrUtil.isNotBlank(getDeptId()) ? getDeptId().split(",") : null;
    }

    private static Boolean converAdmDvs;
    static {
        Environment env = AeyeSpringContextUtils.getBean(Environment.class);
        converAdmDvs = env.getProperty("spring.application.admdvsConvert", Boolean.class, false);
    }
}
