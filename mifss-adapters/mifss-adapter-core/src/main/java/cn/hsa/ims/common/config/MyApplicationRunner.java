package cn.hsa.ims.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.stereotype.Component;

@Component
public class MyApplicationRunner implements ApplicationRunner {

    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired(required = false)
    UserInfoTokenServices tokenServices;

    @Override
    public void run(ApplicationArguments var1) {
        if(tokenServices != null){
            logger.debug("****************** OAuth2：初始化自定义权限对象获取规则*******************");
            tokenServices.setPrincipalExtractor(new AeyeFixedPrincipalExtractor());
        }
    }
}
