//package cn.hsa.hsaf.auth.security.sso;
//
//import cn.hsa.hsaf.auth.security.entity.PortalUserDetails;
//import cn.hsa.hsaf.auth.security.utils.ApiVerifyUtil;
//import cn.hsa.hsaf.auth.security.utils.AuthPropertiesUtil;
//import cn.hsa.hsaf.auth.security.utils.SessionToUserUtil;
//import cn.hsa.hsaf.core.framework.web.WrapperResponse.ResponseType;
//import com.alibaba.fastjson.JSONObject;
//import java.io.IOException;
//import java.io.PrintWriter;
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.FilterConfig;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//
//public class SSOUserContextFilter implements Filter {
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        String securityType = AuthPropertiesUtil.securityType;
//        if (securityType != null && !securityType.equals("hsa-sso-session")) {
//            SessionToUserUtil sessToUser = new SessionToUserUtil();
//            sessToUser.toMockUser();
//            chain.doFilter(request, response);
//        } else {
//            HttpServletRequest httprequest = (HttpServletRequest)request;
//            HttpServletResponse httpresponse = (HttpServletResponse)response;
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            if (authentication != null) {
//                if (httprequest.getSession().getAttribute("CurrentUser") != null) {
//                    PortalUserDetails userDetalis = (PortalUserDetails)httprequest.getSession().getAttribute("CurrentUser");
//                    SessionToUserUtil sessToUser = new SessionToUserUtil();
//                    sessToUser.toCurrentUser(userDetalis);
//                    chain.doFilter(request, response);
//                } else {
//                    ApiVerifyUtil apiVerifyUtil = new ApiVerifyUtil();
//                    if (apiVerifyUtil.whiteListVerify(httprequest.getServletPath().toString())) {
//                        chain.doFilter(request, response);
//                    } else {
//                        httpresponse.setCharacterEncoding("UTF-8");
//                        httpresponse.setContentType("application/json; charset=utf-8");
//                        PrintWriter out = httpresponse.getWriter();
//                        JSONObject res = new JSONObject();
//                        res.put("code", -4);
//                        res.put("type", ResponseType.TYPE_ERROR);
//                        res.put("message", "调用非白名单，且无法获取用户信息!");
//                        res.put("data", "");
//                        out.append(res.toString());
//                        out.flush();
//                        out.close();
//                    }
//                }
//            }
//        }
//
//    }
//
//    public void init(FilterConfig filterConfig) throws ServletException {
//    }
//
//    public void destroy() {
//    }
//}
