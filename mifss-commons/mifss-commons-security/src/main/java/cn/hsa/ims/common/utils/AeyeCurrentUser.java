package cn.hsa.ims.common.utils;

import cn.hsa.hsaf.core.framework.util.CurrentUser;
import lombok.Data;

@Data
public class AeyeCurrentUser extends CurrentUser {

    private int userType;

    private int domainId;

    public String getUserName() {
        return getUserAcct();
    }

    @Deprecated
    public Long getUserId(){
        return Long.parseLong(getUserAcctID());
    }
    @Deprecated
    public Long getOrgId(){
        return Long.parseLong(getOrgUntID());
    }

    public int getDomainId() {
        return domainId;
    }

    public void setDomainId(int domainId) {
        this.domainId = domainId;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }
}
