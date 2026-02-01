package cn.hsa.ims.common.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component("aeyeSpringContextUtils")
public class AeyeSpringContextUtils implements ApplicationContextAware {
    private static ApplicationContext applicationContext;
    private static final Log log = LogFactory.getLog(AeyeSpringContextUtils.class);
    private static String serverPort;

    public static void setContext(ApplicationContext context) {
        applicationContext = context;
    }

    public void setApplicationContext(ApplicationContext context) {
        if (log.isDebugEnabled()) {
            log.debug("SpringContextUtil注入applicaitonContext");
        }

        setContext(context);
    }

    public static ApplicationContext getApplicationContext() {
        return hasLoadContext() ? applicationContext : null;
    }

    private static boolean hasLoadContext() {
        if (applicationContext == null) {
            throw new IllegalStateException("SpringContextUtil初始化失败,请在applicationContext.xml中定义SpringContextUtil");
        } else {
            return true;
        }
    }

    @Value("${server.port}")
    public void setServerPort(String port) {
        serverPort = port;
    }

    public static <T> T getBean(Class<T> requiredType) {
        return (T)getApplicationContext().getBean(requiredType);
    }

    public static <T> T getBean(String name) {
        T bean = null;

        try {
            bean = (T)(hasLoadContext() ? applicationContext.getBean(name) : null);
        } catch (NoSuchBeanDefinitionException ex) {
            log.error(ex.getMessage(), ex);
        }

        return bean;
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> type) {
        return getApplicationContext().getBeansOfType(type);
    }

    public static boolean containsBean(String name) {
        return getApplicationContext().containsBean(name);
    }

    public static boolean isSingleton(String name) {
        return getApplicationContext().isSingleton(name);
    }

    public static Class<? extends Object> getType(String name) {
        return getApplicationContext().getType(name);
    }

    public static <T> T registerByConstructor(String name, Class<T> clazz, Object... args) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        if (args.length > 0) {
            for(Object arg : args) {
                beanDefinitionBuilder.addConstructorArgValue(arg);
            }
        }

        BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
        BeanDefinitionRegistry beanFactory = (BeanDefinitionRegistry)getApplicationContext();
        beanFactory.registerBeanDefinition(name, beanDefinition);
        return (T)applicationContext.getBean(name, clazz);
    }

    public static <T> T registerByProperties(String name, Class<T> clazz, Map<String, Object> params) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        if (params != null && params.size() > 0) {
            params.forEach((s, o) -> beanDefinitionBuilder.addPropertyValue(s, o));
        }

        BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
        BeanDefinitionRegistry beanFactory = (BeanDefinitionRegistry)getApplicationContext();
        beanFactory.registerBeanDefinition(name, beanDefinition);
        return (T)applicationContext.getBean(name, clazz);
    }

    public static String getHostAddress() {
        Enumeration<NetworkInterface> netInterfaces = null;

        try {
            netInterfaces = NetworkInterface.getNetworkInterfaces();

            while(netInterfaces.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface)netInterfaces.nextElement();
                Enumeration<InetAddress> ips = ni.getInetAddresses();

                while(ips.hasMoreElements()) {
                    InetAddress ip = (InetAddress)ips.nextElement();
                    if (ip.isSiteLocalAddress()) {
                        return ip.getHostAddress();
                    }
                }
            }

            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getServerPort() {
        return serverPort;
    }
}
