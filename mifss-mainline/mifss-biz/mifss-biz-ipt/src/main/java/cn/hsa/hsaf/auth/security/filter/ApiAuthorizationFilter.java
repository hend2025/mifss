package cn.hsa.hsaf.auth.security.filter;

import cn.hsa.hsaf.auth.security.utils.ApiVerifyUtil;
import cn.hsa.hsaf.core.framework.web.WrapperResponse;
import cn.hsa.ims.common.utils.AeyeSpringContextUtils;
import cn.hutool.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ApiAuthorizationFilter implements Filter {
    private static Logger log = LoggerFactory.getLogger(ApiAuthorizationFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Boolean isPrivilege = true;
        try {
            ApplicationContext ctx = AeyeSpringContextUtils.getApplicationContext();
            if (ctx != null) {
                isPrivilege = ctx.getEnvironment().getProperty("security.isPrivilege", Boolean.class, false);
            }
        } catch (Exception e) {
            isPrivilege = true;
        }

        if (isPrivilege == null || !isPrivilege) {
            doHsaFilter(request, response, chain);
        } else {
            chain.doFilter(request, response);
        }
    }

    public void doHsaFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httprequest = (HttpServletRequest)request;
        HttpServletResponse httpresponse = (HttpServletResponse)response;
        ApiVerifyUtil apiVerifyUtil = new ApiVerifyUtil();

        try {
            if (apiVerifyUtil.whiteListVerify(httprequest.getServletPath().toString())) {
                chain.doFilter(request, response);
            } else if (apiVerifyUtil.access(httprequest)) {
                chain.doFilter(request, response);
            } else {
                log.info("callpath:" + httprequest.getServletPath().toString());
                httpresponse.setCharacterEncoding("UTF-8");
                httpresponse.setContentType("application/json; charset=utf-8");
                PrintWriter out = httpresponse.getWriter();
                JSONObject res = new JSONObject();
                res.put("code", 110001);
                res.put("type", WrapperResponse.ResponseType.TYPE_ERROR);
                res.put("message", "API没有访问权限!");
                res.put("data", "");
                out.append(res.toString());
                out.flush();
                out.close();
            }
        } catch (Exception ex) {
            log.error(ex.toString(), ex);
            httpresponse.setCharacterEncoding("UTF-8");
            httpresponse.setContentType("application/json; charset=utf-8");
            PrintWriter out = httpresponse.getWriter();
            JSONObject res = new JSONObject();
            res.put("code", 110001);
            res.put("type", WrapperResponse.ResponseType.TYPE_ERROR);
            res.put("message", "API没有访问权限!");
            res.put("data", "");
            out.append(res.toString());
            out.flush();
            out.close();
        }

    }

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void destroy() {

    }

}
