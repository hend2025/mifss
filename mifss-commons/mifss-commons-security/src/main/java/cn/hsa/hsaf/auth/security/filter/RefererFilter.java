package cn.hsa.hsaf.auth.security.filter;

import cn.hsa.hsaf.auth.security.utils.AuthPropertiesUtil;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;

@Primary
public class RefererFilter implements Filter {
    private static Logger log = LoggerFactory.getLogger(ApiAuthorizationFilter.class);

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httprequest = (HttpServletRequest)request;
        HttpServletResponse httpresponse = (HttpServletResponse)response;

        try {
            String referer = httprequest.getHeader("Referer");
            if (StringUtils.isEmpty(referer)) {
                chain.doFilter(request, response);
            } else {
                boolean permitsFlag = false;
                String permits = "/api/**,/*/api/**," + AuthPropertiesUtil.referePermits;
                String requestUrl = httprequest.getServletPath().toString();
                PathMatcher matcher = new AntPathMatcher();
                if (!StringUtils.isEmpty(permits)) {
                    for(int i = 0; i < permits.split(",").length; ++i) {
                        String exclusions = permits.split(",")[i];
                        if (!"".equals(exclusions) && matcher.match(exclusions, requestUrl)) {
                            permitsFlag = true;
                            break;
                        }
                    }
                }

                String activeReferer = AuthPropertiesUtil.activeReferer;
                String refereDomain = this.getDomain(referer);
                if (!permitsFlag && !StringUtils.isEmpty(activeReferer) && refereDomain.indexOf(activeReferer) < 0) {
                    httpresponse.sendError(403, "不被允许的Referer请求!");
                } else {
                    chain.doFilter(request, response);
                }
            }
        } catch (Exception ex) {
            log.error(ex.toString(), ex);
            httpresponse.sendError(403, "不被允许的Referer请求!");
        }

    }

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void destroy() {
    }

    private String getDomain(String urlStr) {
        String result = "";

        try {
            URL url = new URL(urlStr);
            result = url.getHost();
        } catch (MalformedURLException e) {
            log.error(e.toString(), e);
        }

        return result;
    }

}
