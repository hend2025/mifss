package com.aeye.mifss.common.config;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AeyeApplicationRunner implements ApplicationRunner {

    @Value("${spring.application.name:}")
    private String appName;
    @Value("${server.port:}")
    private String port;
    @Value("${server.httpPort:}")
    private String httpPort;
    @Value("${server.servlet.context-path:}")
    private String contextPath;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(StrUtil.isNotBlank(httpPort)){
            log.warn("当前SpringBoot【{}】服务已启动完成!!!http端口【{}】https端口【{}】，访问路径【{}】",appName, httpPort, port, contextPath);
        }else{
            log.warn("当前SpringBoot【{}】服务已启动完成!!!http端口【{}】，访问路径【{}】",appName, port, contextPath);
        }
    }

}
