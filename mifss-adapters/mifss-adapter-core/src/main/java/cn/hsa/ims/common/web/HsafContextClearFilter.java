package cn.hsa.ims.common.web;

import cn.hsa.hsaf.core.framework.context.HsafContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author shenxingping
 * @TODO 描述
 * @date 2022/04/07
 */
@Component
@Order(-999)
@Slf4j
public class HsafContextClearFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.warn("启动HsafContext ThreadLocal线程清理过滤器!");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        /**
         * 解决ThreadLocal 线程共享导致Hsaf上下文信息共享，用户信息混乱
         */
        HsafContextHolder.setContext(null);
        log.debug("进入清理上下文");
        filterChain.doFilter(servletRequest, servletResponse);
        HsafContextHolder.setContext(null);
        log.debug("离开清理上下文");
    }

    @Override
    public void destroy() {

    }
}
