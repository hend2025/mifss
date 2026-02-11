package cn.hsa.ims.common.web;

import cn.hsa.ims.common.utils.AeyeHttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author shenxingping
 * @TODO 描述
 * @date 2022/09/06
 */
@Configuration
@Slf4j
public class AeyeHandlerExceptionResolver extends DefaultHandlerExceptionResolver implements HandlerExceptionResolver {

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        return super.doResolveException(request, response, handler, ex);
    }

    @Override
    protected ModelAndView handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        log.info("客户端IP={}；请求地址={}；参数缺失={}", AeyeHttpClientUtil.getIpAddr(request), request.getRequestURL()+"?"+request.getQueryString(), ex.getMessage());
        return super.handleMissingServletRequestParameter(ex, request, response, handler);
    }
}
