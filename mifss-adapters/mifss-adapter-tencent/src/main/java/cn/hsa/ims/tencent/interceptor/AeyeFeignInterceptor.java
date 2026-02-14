package cn.hsa.ims.tencent.interceptor;

import cn.hsa.hsaf.core.framework.context.HsafContext;
import cn.hsa.hsaf.core.framework.context.HsafContextHolder;
import cn.hsa.hsaf.core.framework.util.SerialUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import cn.hutool.core.util.StrUtil;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @author shenxingping
 * @date 2021/11/28
 */
public class AeyeFeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            Enumeration<String> headerNames = request.getHeaderNames();
            String userInfo;
            if (headerNames != null) {
                while(headerNames.hasMoreElements()) {
                    userInfo = (String)headerNames.nextElement();
                    String values = request.getHeader(userInfo);
                    // 排除表单数据的请求头协议，会影响接收端
                    if(!values.startsWith(MediaType.MULTIPART_FORM_DATA_VALUE)){
                        template.header(userInfo, new String[]{values});
                    }
                }
            }

            userInfo = request.getHeader("HsafContext");
            SerialUtil<HsafContext> serialUtil = new SerialUtil();
            if (StrUtil.isBlank(userInfo)) {
                HsafContext hsafContext = HsafContextHolder.getContext();
                String userInfoBase64 = serialUtil.serialToBase64(hsafContext);
                template.header("HsafFeignSign", new String[]{"HsafFeignSign"});
                template.header("HsafContext", new String[]{userInfoBase64});
            }
        }

    }
}
