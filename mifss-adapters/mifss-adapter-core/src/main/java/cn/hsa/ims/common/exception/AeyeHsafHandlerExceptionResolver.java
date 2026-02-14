package cn.hsa.ims.common.exception;

import cn.hsa.hsaf.core.framework.context.HsafContextHolder;
import cn.hsa.hsaf.core.framework.web.WrapperResponse;
import cn.hsa.hsaf.core.framework.web.exception.AppException;
import cn.hsa.hsaf.core.framework.web.exception.ExceptionSeq;
import cn.hsa.hsaf.core.log.HsafLogHandler;
import cn.hsa.hsaf.core.log.LogContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class AeyeHsafHandlerExceptionResolver extends AbstractHandlerExceptionResolver {
    private Logger log = LoggerFactory.getLogger(AeyeHsafHandlerExceptionResolver.class);
    private String viewType = "json";
    private String defaultErrorView;
    private String defaultErrorMessage;
    private Map<Integer, String> exceptionMappings = new HashMap();
    private Map<String, String> userDefinedExceptions = new HashMap();
    private Map<String, Integer> ude_errCode = new HashMap();
    private Map<String, String> ude_errMsg = new HashMap();
    private String LOG_CONTEXT_KEY = "_logContext";
    @Autowired
    ExceptionSeq exceptionSeq;
    @Autowired
    HsafLogHandler hsafLogHandler;

    public AeyeHsafHandlerExceptionResolver() {
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ModelAndView mv = null;
        Integer errorCode = WrapperResponse.FAIL;
        LogContext logContext = (LogContext) HsafContextHolder.getContext().getProperty(this.LOG_CONTEXT_KEY);
        String traceID = "";
        String traceInfo = "[hsafException]";
        if (logContext != null && logContext.getTraceID() != null) {
            traceID = logContext.getTraceID();
            traceInfo = traceInfo + "[traceID:" + traceID + "]";
        }

        Throwable realException = getRealException(ex);
        String exseq = this.exceptionSeq.getExceptionSeq();
        String exmsg = "";
        String ex_clz = this.getExceptionClz(realException);
        this.log.debug("ex=" + realException + ";ex.getCause()=" + realException.getCause());
        boolean ex_match = false;
        String viewName;
        if (realException.getCause() != null) {
            viewName = realException.getCause().getClass().getName();
            if (this.ude_errCode.containsKey(viewName)) {
                errorCode = (Integer)this.ude_errCode.get(viewName);
                exmsg = (String)this.ude_errMsg.get(viewName);
                this.log.error(this.hsafLogHandler.getBaseLogString() + traceInfo + ",异常流水号exseq=" + exseq + ",错误码=" + errorCode + ",错误信息:" + exmsg, realException.getCause());
                ex_match = true;
            }
        }

        if (!ex_match && this.ude_errCode.containsKey(ex_clz)) {
            errorCode = (Integer)this.ude_errCode.get(ex_clz);
            exmsg = (String)this.ude_errMsg.get(ex_clz);
            this.log.error(this.hsafLogHandler.getBaseLogString() + traceInfo + ",异常流水号exseq=" + exseq + ",错误码=" + errorCode + ",错误信息:" + exmsg, realException);
            ex_match = true;
        }

        if (!ex_match) {
            if (realException instanceof UndeclaredThrowableException) {
                Throwable undeclaredThrowable = ((UndeclaredThrowableException)realException).getUndeclaredThrowable();
                if (undeclaredThrowable != null && undeclaredThrowable instanceof AppException) {
                    int code = ((AppException)undeclaredThrowable).getCode();
                    exmsg = ((AppException)undeclaredThrowable).getMessage();
                    errorCode = code;
                    this.log.error(this.hsafLogHandler.getBaseLogString() + traceInfo + ",异常流水号exseq=" + exseq + ",错误码=" + errorCode + ",错误信息:" + exmsg, undeclaredThrowable);
                }
            } else {
                int code;
                if (realException instanceof AppException) {
                    code = ((AppException)realException).getCode();
                    exmsg = ((AppException)realException).getMessage();
                    errorCode = code;
                    if(errorCode == -1){
                        this.log.error(this.hsafLogHandler.getBaseLogString() + traceInfo + ",异常流水号exseq=" + exseq + ",错误码=" + errorCode + ",错误信息:" + exmsg, realException);
                    }else if(errorCode == - 99){
                        this.log.info(this.hsafLogHandler.getBaseLogString() + traceInfo + ",异常流水号exseq=" + exseq + ",错误码=" + errorCode + ",错误信息:" + exmsg, realException);
                    }else{
                        this.log.error(this.hsafLogHandler.getBaseLogString() + traceInfo + ",异常流水号exseq=" + exseq + ",错误码=" + errorCode + ",错误信息:" + exmsg);
                    }
                } else if (realException.getCause() == null) {
                    this.log.error(this.hsafLogHandler.getBaseLogString() + traceInfo + ",异常流水号exseq=" + exseq + ",错误码=" + errorCode + ",错误信息:" + realException.getMessage(), realException);
                } else if (realException.getCause() instanceof AppException) {
                    code = ((AppException)realException.getCause()).getCode();
                    exmsg = ((AppException)realException.getCause()).getMessage();
                    errorCode = code;
                    if(errorCode == -1){
                        this.log.error(this.hsafLogHandler.getBaseLogString() + traceInfo + ",异常流水号exseq=" + exseq + ",错误码=" + errorCode + ",错误信息:" + exmsg, realException.getCause());
                    }else if(errorCode == -99){
                        this.log.info(this.hsafLogHandler.getBaseLogString() + traceInfo + ",异常流水号exseq=" + exseq + ",错误码=" + errorCode + ",错误信息:" + exmsg, realException.getCause());
                    }else{
                        this.log.error(this.hsafLogHandler.getBaseLogString() + traceInfo + ",异常流水号exseq=" + exseq + ",错误码=" + errorCode + ",错误信息:" + exmsg);
                    }
                } else if (realException.getCause() instanceof AeyeCloudFeignException) {
                    code = ((AeyeCloudFeignException)realException.getCause()).getCode();
                    exmsg = ((AeyeCloudFeignException)realException.getCause()).getMessage();
                    errorCode = code;
                    if(errorCode == -1){
                        this.log.error(this.hsafLogHandler.getBaseLogString() + traceInfo + ",异常流水号exseq=" + exseq + ",错误码=" + errorCode + ",错误信息:" + exmsg, realException.getCause());
                    }else if(errorCode == -99){
                        this.log.info(this.hsafLogHandler.getBaseLogString() + traceInfo + ",异常流水号exseq=" + exseq + ",错误码=" + errorCode + ",错误信息:" + exmsg, realException.getCause());
                    }else{
                        this.log.error(this.hsafLogHandler.getBaseLogString() + traceInfo + ",异常流水号exseq=" + exseq + ",错误码=" + errorCode + ",错误信息:" + exmsg);
                    }
                } else {
                    this.log.error(this.hsafLogHandler.getBaseLogString() + traceInfo + ",异常流水号exseq=" + exseq + ",错误码=" + errorCode + ",错误信息:" + realException.getMessage(), realException.getCause());
                }
            }
        }

        if (this.viewType.equalsIgnoreCase("page")) {
            viewName = this.defaultErrorView;
            if (this.exceptionMappings != null && this.exceptionMappings.containsKey(errorCode)) {
                viewName = (String)this.exceptionMappings.get(errorCode);
            }

            mv = new ModelAndView();
            mv.setViewName(viewName);
        } else {
            mv = new ModelAndView(new MappingJackson2JsonView());
        }

        mv.addObject("code", errorCode);
        mv.addObject("type", WrapperResponse.ResponseType.TYPE_ERROR.getType());
        if (exmsg.length() > 0) {
            mv.addObject("message", exmsg + ",异常流水号:" + exseq);
        } else if (this.defaultErrorMessage != null && !this.defaultErrorMessage.equals("")) {
            mv.addObject("message", this.defaultErrorMessage + ",异常流水号:" + exseq);
        } else {
            mv.addObject("message", realException.getMessage() + ",异常流水号:" + exseq);
        }

        return mv;
    }

    private String getExceptionClz(Throwable ex) {
        return ex.getClass().getName();
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    public void setDefaultErrorView(String defaultErrorView) {
        this.defaultErrorView = defaultErrorView;
    }

    public void setExceptionMappings(Map<Integer, String> exceptionMappings) {
        this.exceptionMappings.putAll(exceptionMappings);
    }

    public void addExceptionMappings(Integer errorCode, String errorView) {
        this.exceptionMappings.put(errorCode, errorView);
    }

    public void setUserDefinedExceptions(Map<String, String> userDefinedExceptions) {
        this.userDefinedExceptions.putAll(userDefinedExceptions);
        if (userDefinedExceptions != null && !userDefinedExceptions.isEmpty()) {
            Iterator it = userDefinedExceptions.keySet().iterator();

            while(it.hasNext()) {
                String k = (String)it.next();
                String v = (String)userDefinedExceptions.get(k);
                if (v.indexOf("|") == -1) {
                    this.log.error("无效的自定义异常拦截配置，ex_clz=" + k + ";ex_msg=" + v);
                } else {
                    this.ude_errCode.put(k, Integer.parseInt(v.substring(0, v.indexOf("|"))));
                    this.ude_errMsg.put(k, v.substring(v.indexOf("|") + 1));
                }
            }
        }

    }

    public void addUserDefinedExceptions(String ex_clz, String ex_msg) {
        this.userDefinedExceptions.put(ex_clz, ex_msg);
        if (ex_msg.indexOf("|") == -1) {
            this.log.error("无效的自定义异常拦截配置，ex_clz=" + ex_clz + ";ex_msg=" + ex_msg);
        } else {
            this.ude_errCode.put(ex_clz, Integer.parseInt(ex_msg.substring(0, ex_msg.indexOf("|"))));
            this.ude_errMsg.put(ex_clz, ex_msg.substring(ex_msg.indexOf("|") + 1));
        }

    }

    public static Throwable getRealException(Exception ex){
        if(ex instanceof InvocationTargetException){
            return ((InvocationTargetException) ex).getTargetException();
        }
        return ex;
    }

    public void setDefaultErrorMessage(String defaultErrorMessage) {
        this.defaultErrorMessage = defaultErrorMessage;
    }
}
