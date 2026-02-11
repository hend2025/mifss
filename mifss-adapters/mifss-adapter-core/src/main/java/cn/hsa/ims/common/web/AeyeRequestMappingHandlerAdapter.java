package cn.hsa.ims.common.web;

import cn.hsa.hsaf.core.framework.context.HsafContext;
import cn.hsa.hsaf.core.framework.context.HsafContextHolder;
import cn.hsa.hsaf.core.framework.util.SerialUtil;
import cn.hsa.hsaf.core.framework.web.HsafRestPath;
import cn.hsa.hsaf.core.framework.web.WrapperResponse;
import cn.hsa.hsaf.core.framework.web.exception.ExceptionSeq;
import cn.hsa.hsaf.core.framework.web.exception.ParamException;
import cn.hsa.ims.common.utils.AeyeJdk8DateUtil;
import cn.hsa.ims.common.utils.AeyeReflectionUtil;
import cn.hsa.ims.common.utils.AeyeStringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.*;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author shenxingping
 * @date 2021/04/05
 */
public class AeyeRequestMappingHandlerAdapter extends RequestMappingHandlerAdapter {
    private Logger log = LoggerFactory.getLogger(AeyeRequestMappingHandlerAdapter.class);
    static int SUCCESS = 0;
    static int FAIL = -1;
    static int PARAM_VALID_EXCEPTION = -2;
    static String MSG_SUCCESS = "成功";
    static String MSG_FAIL = "未知的异常";
    private String LOG_CONTEXT_KEY = "_logContext";
    private static final LocalVariableTableParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    @Autowired(required = false)
    ExceptionSeq exceptionSeq;

    public AeyeRequestMappingHandlerAdapter() {
    }

    @Override
    protected boolean supportsInternal(HandlerMethod handlerMethod) {
        this.log.debug("[hsaf]调用HsafRequestMappingHandlerAdapter.supportsInternal()方法，handlerMethod=" + handlerMethod);
        return AnnotatedElementUtils.hasAnnotation(handlerMethod.getMethod(), HsafRestPath.class);
    }

    @Override
    protected ModelAndView handleInternal(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod) throws Exception {
        this.log.debug("[hsaf]调用HsafRequestMappingHandlerAdapter.handleInternal()方法，handlerMethod=" + handlerMethod);
        ModelAndView mv = new ModelAndView(new MappingJackson2JsonView());
        Method m = handlerMethod.getMethod();
        MethodParameter[] methodParameters = handlerMethod.getMethodParameters();
        String[] paramNames = parameterNameDiscoverer.getParameterNames(m);
        Object hostBean = handlerMethod.getBean();
        String methodType = request.getMethod();
        if (methodType != null && !methodType.equals("") && (methodType.equalsIgnoreCase("GET") || methodType.equalsIgnoreCase("POST"))) {
            this.handleHsafContext(request);
            Object returnObj = null;

            try {
                Parameter[] ps = m.getParameters();
                if (ps != null && ps.length != 0) {
                    String jsonStr;
                    String paramString;
                    if (ps.length == 1) {
                        if (methodType.equalsIgnoreCase("GET")) {
                            if (!AeyeReflectionUtil.isBasicType(ps[0].getType())) {
                                mv.addObject("code", FAIL);
                                mv.addObject("type", WrapperResponse.ResponseType.TYPE_ERROR.getType());
                                mv.addObject("message", "非基本类型的参数无法通过GET请求");
                                return mv;
                            }

                            Object[] arglist = new Object[1];
                            String queryString = request.getQueryString();
                            if (queryString != null && !queryString.equals("") && !queryString.equals("null")) {
                                if (queryString.length() > 0) {
                                    try {
                                        queryString = URLDecoder.decode(queryString, "utf-8");
                                    } catch (Exception var24) {
                                        this.log.error("请求串转码异常");
                                    }
                                }

                                this.log.debug("queryString=" + queryString);
                                paramString = queryString.substring(queryString.indexOf("=") + 1);
                                arglist[0] = this.getObject(ps[0].getType(), paramString);
                                returnObj = m.invoke(hostBean, arglist);
                            }else{
                                returnObj = m.invoke(hostBean, queryString);
                            }

                            if (returnObj instanceof WrapperResponse) {
                                mv.addObject("code", ((WrapperResponse)returnObj).getCode());
                                mv.addObject("type", ((WrapperResponse)returnObj).getType());
                                mv.addObject("message", ((WrapperResponse)returnObj).getMessage());
                                mv.addObject("data", ((WrapperResponse)returnObj).getData());
                            } else {
                                mv.addObject("code", SUCCESS);
                                mv.addObject("type", WrapperResponse.ResponseType.TYPE_SUCCESS.getType());
                                mv.addObject("message", MSG_SUCCESS);
                                mv.addObject("data", returnObj);
                            }

//                            mv.addObject("code", PARAM_VALID_EXCEPTION);
//                            mv.addObject("type", WrapperResponse.ResponseType.TYPE_ERROR.getType());
//                            mv.addObject("message", "请求参数个数不匹配");
                            return mv;
                        }

                        jsonStr = this.getJsonStrFromRequest(request);
                        this.log.debug("jsonStr=" + jsonStr);
                        Class typeClass = ps[0].getType();
                        this.log.debug("typeClass=" + typeClass + ";isBasicType=" + AeyeReflectionUtil.isBasicType(typeClass) + ";isArray=" + typeClass.isArray() + ";isList=" + typeClass.isAssignableFrom(List.class) + ";isMap=" + typeClass.isAssignableFrom(Map.class));
                        if (AeyeReflectionUtil.isBasicType(typeClass)) {
                            Object paramObj = convertToInvokeParam(jsonStr, request.getQueryString(), methodParameters, paramNames)[0];
                            returnObj = m.invoke(hostBean, paramObj);
                        } else if (typeClass.isArray()) {
                            JSONArray jsonArray = JSONArray.parseArray(jsonStr);
                            Object[] arglist = new Object[1];
                            Object array;
                            if (AeyeReflectionUtil.isBasicType(typeClass.getComponentType())) {
                                array = Array.newInstance(typeClass.getComponentType(), jsonArray.size());

                                for(int i = 0; i < jsonArray.size(); ++i) {
                                    Array.set(array, i, jsonArray.get(i));
                                }

                                arglist[0] = array;
                                returnObj = m.invoke(hostBean, arglist);
                            } else {
                                array = Array.newInstance(typeClass.getComponentType(), jsonArray.size());

                                for(int i = 0; i < jsonArray.size(); ++i) {
                                    JSONObject jsonObj = jsonArray.getJSONObject(i);
                                    Object paramObj = JSON.toJavaObject(jsonObj, typeClass.getComponentType());
                                    Array.set(array, i, paramObj);
                                }

                                arglist[0] = array;
                                returnObj = m.invoke(hostBean, arglist);
                            }
                        } else if (typeClass instanceof Object) {
                            MethodParameter parameters = methodParameters[0];
                            Object[] arglist = new Object[]{JSONObject.parseObject(jsonStr, parameters.getGenericParameterType())};
                            returnObj = m.invoke(hostBean, arglist);
                        } else {
                            ParameterizedType pt;
                            Type t;
                            if (typeClass.isAssignableFrom(List.class)) {
                                Type type = ps[0].getParameterizedType();
                                if (type instanceof ParameterizedType) {
                                    pt = (ParameterizedType)type;
                                    t = pt.getActualTypeArguments()[0];
                                    Class clz = Class.forName(t.getTypeName());
                                    JSONArray jsonArray = JSONArray.parseArray(jsonStr);
                                    List list = new ArrayList();

                                    for(int i = 0; i < jsonArray.size(); ++i) {
                                        Object paramObj;
                                        if (AeyeReflectionUtil.isBasicType(clz)) {
                                            paramObj = jsonArray.getObject(i, t);
                                        } else {
                                            JSONObject jsonObj = jsonArray.getJSONObject(i);
                                            paramObj = jsonObj.toJavaObject(t);
                                        }

                                        list.add(paramObj);
                                    }

                                    returnObj = m.invoke(hostBean, list);
                                }
                            } else if (typeClass.isAssignableFrom(Map.class)) {
                                Type type = ps[0].getParameterizedType();
                                if (type instanceof ParameterizedType) {
                                    pt = (ParameterizedType)type;
                                    t = pt.getActualTypeArguments()[0];
                                    Type t1 = pt.getActualTypeArguments()[1];
                                    Class clz0 = Class.forName(t.getTypeName());
                                    Class clz1 = Class.forName(t1.getTypeName());
                                    Map map = (Map)JSONObject.parseObject(jsonStr, typeClass);
                                    Map jmap = new HashMap();

                                    Object kobj;
                                    Object rvobj;
                                    for(Iterator it = map.keySet().iterator(); it.hasNext(); jmap.put(clz0.getConstructor(String.class).newInstance(kobj), rvobj)) {
                                        kobj = it.next();
                                        if (AeyeReflectionUtil.isBasicType(clz1)) {
                                            rvobj = map.get(kobj);
                                        } else {
                                            JSONObject vobj = (JSONObject)map.get(kobj);
                                            rvobj = vobj.toJavaObject(t1);
                                        }
                                    }

                                    returnObj = m.invoke(hostBean, jmap);
                                }
                            } else {
                                Object[] arglist = new Object[1];
                                Object paramObj = null;

                                try {
                                    JSONObject jsonobj = JSONObject.parseObject(jsonStr);
                                    if (jsonobj != null && jsonobj.containsKey("_class")) {
                                        String clz = jsonobj.getString("_class");
                                        if (clz != null && clz.length() > 1) {
                                            Type type = ((TypeReference)Class.forName(clz).newInstance()).getType();
                                            paramObj = JSONObject.parseObject(jsonStr, type, new Feature[0]);
                                        }
                                    }
                                } catch (Exception var26) {
                                    this.log.error("_class参数非法");
                                    var26.printStackTrace();
                                }

                                if (paramObj == null) {
                                    if (m.getGenericParameterTypes()[0].getTypeName().length() == 1) {
                                        paramObj = JSONObject.parseObject(jsonStr, typeClass);
                                    } else {
                                        paramObj = JSONObject.parseObject(jsonStr, m.getGenericParameterTypes()[0], new Feature[0]);
                                    }
                                }

                                arglist[0] = paramObj;
                                returnObj = m.invoke(hostBean, arglist);
                            }
                        }
                    } else {
                        int i;
                        Iterator var14;
                        if (methodType.equalsIgnoreCase("GET")) {
                            jsonStr = request.getQueryString();
                            if (jsonStr != null && jsonStr.length() > 0) {
                                try {
                                    jsonStr = URLDecoder.decode(jsonStr, "utf-8");
                                } catch (Exception var25) {
                                    this.log.error("GET请求串转码异常");
                                }
                            }

                            this.log.debug("queryString=" + jsonStr);
                            Object[] params = convertToInvokeParam(null, jsonStr, methodParameters, paramNames);

                            returnObj = m.invoke(hostBean, params);
                        } else {
                            jsonStr = this.getJsonStrFromRequest(request);

                            paramString = request.getQueryString();
                            if (paramString != null && paramString.length() > 0) {
                                try {
                                    paramString = URLDecoder.decode(paramString, "utf-8");
                                } catch (Exception var25) {
                                    this.log.error("POST请求串转码异常");
                                }
                            }
                            this.log.debug("queryString=" + paramString);
                            Object[] params = convertToInvokeParam(jsonStr, paramString, methodParameters, paramNames);
                            returnObj = m.invoke(hostBean, params);
                        }
                    }
                } else {
                    returnObj = m.invoke(hostBean);
                }
            } catch (Exception var27) {
                if (var27 instanceof InvocationTargetException) {
                    Throwable target = ((InvocationTargetException)var27).getTargetException();
                    if (target != null && target instanceof UndeclaredThrowableException) {
                        Throwable undeclaredThrowable = ((UndeclaredThrowableException)target).getUndeclaredThrowable();
                        if (undeclaredThrowable != null && undeclaredThrowable instanceof ParamException) {
                            mv.addObject("code", PARAM_VALID_EXCEPTION);
                            mv.addObject("type", WrapperResponse.ResponseType.TYPE_ERROR.getType());
                            mv.addObject("message", undeclaredThrowable.getMessage());
                            return mv;
                        }
                    }
                } else {
                    if (var27 instanceof ParamException) {
                        mv.addObject("code", PARAM_VALID_EXCEPTION);
                        mv.addObject("type", WrapperResponse.ResponseType.TYPE_ERROR.getType());
                        mv.addObject("message", var27.getMessage());
                        return mv;
                    }

                    if (var27.getCause() != null && var27.getCause() instanceof ParamException) {
                        mv.addObject("code", PARAM_VALID_EXCEPTION);
                        mv.addObject("type", WrapperResponse.ResponseType.TYPE_ERROR.getType());
                        mv.addObject("message", var27.getCause().getMessage());
                        return mv;
                    }
                }

                throw var27;
            }

            if (returnObj instanceof WrapperResponse) {
                mv.addObject("code", ((WrapperResponse)returnObj).getCode());
                mv.addObject("type", ((WrapperResponse)returnObj).getType());
                mv.addObject("message", ((WrapperResponse)returnObj).getMessage());
                mv.addObject("data", ((WrapperResponse)returnObj).getData());
            } else {
                mv.addObject("code", SUCCESS);
                mv.addObject("type", WrapperResponse.ResponseType.TYPE_SUCCESS.getType());
                mv.addObject("message", MSG_SUCCESS);
                mv.addObject("data", returnObj);
            }
            return mv;
        } else {
            mv.addObject("code", FAIL);
            mv.addObject("type", WrapperResponse.ResponseType.TYPE_ERROR.getType());
            mv.addObject("message", "请求参数类型异常，当前版本仅支持GET、POST。");
            return mv;
        }
    }

    /**
     * 转换为java反射方法
     * @return
     */
    private Object[] convertToInvokeParam(String requestBodyStr, String queryJsonStr, MethodParameter[] methodParameters,String[] paramNames) throws Exception{
        Object[] invokeParams = new Object[methodParameters.length];
        Map<String, String> urlQueryStr = convertToMap(queryJsonStr);

        for(int i=0; i<methodParameters.length; i++){
            Parameter methodParams = methodParameters[i].getParameter();
            Object bodyParam = methodParams.getDeclaredAnnotation(RequestBody.class);
            if(bodyParam != null){
                if(methodParams.getType().equals(String.class)){
                    invokeParams[i] = requestBodyStr;
                }else if(AeyeStringUtils.isNotBlank(requestBodyStr)) {
                    invokeParams[i] = JSON.parseObject(requestBodyStr, methodParams.getType());
                }else{
                    invokeParams[i] = null;
                }
            }else{
                invokeParams[i] = this.getObject(methodParams.getType(), urlQueryStr.get(paramNames[i]));
            }
        }
        return invokeParams;
    }

    private Object getObject(Class clazz, String paramString) throws Exception {
        if(paramString == null){
            return null;
        }
        if (clazz.isPrimitive()) {
            if (clazz.getSimpleName().equalsIgnoreCase("boolean")) {
                return Boolean.valueOf(paramString);
            }

            if (clazz.getSimpleName().equalsIgnoreCase("char")) {
                return paramString.charAt(0);
            }

            if (clazz.getSimpleName().equalsIgnoreCase("date")) {
                return AeyeJdk8DateUtil.convertToDate(LocalDateTime.parse(paramString, AeyeJdk8DateUtil.DATETIME));
            }

            if (clazz.getSimpleName().equalsIgnoreCase("byte")) {
                return Byte.valueOf(paramString);
            }

            if (clazz.getSimpleName().equalsIgnoreCase("short")) {
                return Short.valueOf(paramString);
            }

            if (clazz.getSimpleName().equalsIgnoreCase("int")) {
                return Integer.valueOf(paramString);
            }

            if (clazz.getSimpleName().equalsIgnoreCase("long")) {
                return Long.valueOf(paramString);
            }

            if (clazz.getSimpleName().equalsIgnoreCase("float")) {
                return Float.valueOf(paramString);
            }

            if (clazz.getSimpleName().equalsIgnoreCase("double")) {
                return Double.valueOf(paramString);
            }

            if (clazz.getSimpleName().equalsIgnoreCase("void")) {
                return Void.TYPE;
            }
        }

        if (clazz.getSimpleName().equalsIgnoreCase("Character")) {
            return paramString.charAt(0);
        }else if(clazz.getSimpleName().equalsIgnoreCase("date")){
            return AeyeJdk8DateUtil.convertToDate(LocalDateTime.parse(paramString, AeyeJdk8DateUtil.DATETIME));
        } else if (clazz.getSimpleName().equalsIgnoreCase("void")) {
            return Void.TYPE;
        } else {
            Constructor c1 = clazz.getConstructor(String.class);
            return c1.newInstance(paramString);
        }
    }

    private Map<String, String> convertToMap(String queryString) {
        Map<String, String> list = new HashMap<>(50);
        if(AeyeStringUtils.isEmpty(queryString)){
            return list;
        }
        queryString = queryString.replaceAll("&amp;", "&");
        String[] qs = queryString.split("&");

        for(int i = 0; i < qs.length; i++) {
            String s = qs[i];
            list.put(s.substring(0, s.indexOf("=")), s.substring(s.indexOf("=") + 1));
        }

        return list;
    }
    private List<String> convertToList(String queryString) {
        List<String> list = new ArrayList();
        queryString = queryString.replaceAll("&amp;", "&");
        String[] qs = queryString.split("&");
        String[] var4 = qs;
        int var5 = qs.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            String s = var4[var6];
            list.add(s.substring(s.indexOf("=") + 1));
        }

        return list;
    }

    private String getJsonStrFromRequest(HttpServletRequest request) {
        try {
            InputStream in = request.getInputStream();
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];

            int length;
            while((length = in.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }

            in.close();
            String jsonStr = result.toString("UTF-8");
            result.close();
            return jsonStr;
        } catch (Exception var7) {
            this.log.error("从request请求输入流中获取数据异常，异常信息如下:");
            var7.printStackTrace();
            return null;
        }
    }

    private void handleHsafContext(HttpServletRequest request) {
        HsafContext hsafContext = HsafContextHolder.getContext();
        if (hsafContext == null || hsafContext.getCurrentUser() == null || hsafContext.getCurrentUser().getUactID() == null && hsafContext.getProperties().isEmpty()) {
            String strcontext = (String)request.getAttribute("HsafContext");
            if (!StringUtils.isEmpty(strcontext)) {
                SerialUtil<HsafContext> serialUtil = new SerialUtil();
                hsafContext = (HsafContext)serialUtil.derialFromBase64(strcontext);
                HsafContextHolder.setContext(hsafContext);
            }
        }

    }
}