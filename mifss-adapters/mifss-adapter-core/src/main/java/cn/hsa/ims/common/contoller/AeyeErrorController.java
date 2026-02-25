package cn.hsa.ims.common.contoller;

import cn.hsa.hsaf.core.framework.web.WrapperResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class AeyeErrorController implements ErrorController {

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping("/error")
    @ResponseBody
    public WrapperResponse handlerError(HttpServletRequest request, HttpServletResponse response){
        WrapperResponse errRes = WrapperResponse.fail(null);
        errRes.setCode(response.getStatus());
        if(errRes.getCode() == HttpStatus.NOT_FOUND.value()){
            errRes.setMessage("资源不存在，请检查请求地址。");
        }else{
            errRes.setMessage("服务器异常，请联系运维人员排查日志。" + request.getAttribute("org.springframework.web.servlet.DispatcherServlet.EXCEPTION"));
        }
        return errRes;
    }

}
