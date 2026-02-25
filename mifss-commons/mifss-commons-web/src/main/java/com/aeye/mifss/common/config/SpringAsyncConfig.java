package com.aeye.mifss.common.config;

import cn.hsa.ims.common.utils.AeyeSpringContextUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@DependsOn("aeyeSpringContextUtils")
@Configuration
@EnableAsync
public class SpringAsyncConfig {

    @Autowired
    ThreadPoolProperties poolProperties;

    public AsyncTaskExecutor buildExecutor(String beanName, ThreadPoolAdapter pro) {
        Map<String, Object> params = new HashMap<>();

        params.put("corePoolSize", pro.getCorePoolSize());//设置核心线程数,当线程数超过核心线程数，则超过的线程则进入任务队列
        params.put("maxPoolSize", pro.getMaxPoolSize());//最大线程数,只有在任务队列满了之后才会申请超过核心线程数的线程。不能小于核心线程数,相当于突发情况扩增
        params.put("queueCapacity", pro.getQueueCapacity());//任务队列大小
        params.put("keepAliveSeconds", pro.getKeepAliveSeconds());//线程的空闲时间
        params.put("threadNamePrefix", pro.getThreadNamePrefix());//线程前缀名
        params.put("rejectedExecutionHandler", new ThreadPoolExecutor.AbortPolicy());//拒绝策略
        params.put("waitForTasksToCompleteOnShutdown", true);

        return AeyeSpringContextUtils.registerByProperties(beanName, ThreadPoolTaskExecutor.class, params);

//        核心线程数：
//          线程池创建时候初始化的线程数。当线程数超过核心线程数，则超过的线程则进入任务队列。

//        最大线程数：
//          只有在任务队列满了之后才会申请超过核心线程数的线程。不能小于核心线程数。

//        任务队列：线程数大于核心线程数的部分进入任务队列。如果任务队列足够大，超出核心线程数的线程不会被创建，
//          它会等待核心线程执行完它们自己的任务后再执行任务队列的任务，而不会再额外地创建线程。举例：如果有20个任务要执行，
//          核心线程数：10，最大线程数：20，任务队列大小：2。则系统会创建18个线程。这18个线程有执行完任务的，再执行任务队列中的任务。

//        线程的空闲时间：当 线程池中的线程数量 大于 核心线程数 时，如果某线程空闲时间超过 keepAliveTime ，线程将被终止。这样，
//          线程池可以动态的调整池中的线程数。

//        拒绝策略：如果（总任务数 - 核心线程数 - 任务队列数）-（最大线程数 - 核心线程数）> 0 的话，则会出现线程拒绝。
//          举例：( 12 - 5 - 2 ) - ( 8 - 5 ) > 0，会出现线程拒绝。线程拒绝又分为 4 种策略，分别为：
//          CallerRunsPolicy()：交由调用方线程运行，比如 main 线程。
//          AbortPolicy()：直接抛出异常。
//          DiscardPolicy()：直接丢弃。
//          DiscardOldestPolicy()：丢弃队列中最老的任务。

    }

    @PostConstruct
    public void postProcessBeanFactory() throws BeansException {
        Map<String, ThreadPoolAdapter> poolAdapterMap = poolProperties.getThreadPool();
        poolAdapterMap.forEach((beanName, poolAdapter) -> {
            AsyncTaskExecutor pool = buildExecutor(beanName, poolAdapter);
            if(pool == null){
                throw new BeanCreationException("beanName:["+beanName+"] 创建失败！");
            }
        });
    }

    public static List<Map<String, Object>> showAllThreadPoolInfo(){
        List<Map<String, Object>> result = new ArrayList<>();

        ThreadPoolProperties poolProperties = AeyeSpringContextUtils.getBean(ThreadPoolProperties.class);
        Map<String, ThreadPoolAdapter> poolAdapterMap = poolProperties.getThreadPool();

        poolAdapterMap.forEach((beanName, threadPoolAdapter) -> {
            ThreadPoolTaskExecutor threadPoolExecutor = AeyeSpringContextUtils.getBean(beanName);

            if(null==threadPoolExecutor){
                return;
            }
            Map<String, Object> data = getInfo(beanName, threadPoolExecutor, poolAdapterMap);

            result.add(data);
        });
        return result;
    }

    public static Map<String, Object> showThreadPoolInfo(String beanName){
        ThreadPoolTaskExecutor threadPoolExecutor = AeyeSpringContextUtils.getBean(beanName);
        ThreadPoolProperties poolProperties = AeyeSpringContextUtils.getBean(ThreadPoolProperties.class);
        Map<String, ThreadPoolAdapter> poolAdapterMap = poolProperties.getThreadPool();
        if(null==threadPoolExecutor){
            return null;
        }
        Map<String, Object> data = getInfo(beanName, threadPoolExecutor, poolAdapterMap);
        return data;
    }

    private static Map getInfo(String name, ThreadPoolTaskExecutor threadPoolExecutor, Map<String, ThreadPoolAdapter> poolAdapterMap){

        Map data = new HashMap();
        //线程池名
        data.put("poolName","线程池名称="+name);
        //线程池名
        data.put("desc","线程池描述="+poolAdapterMap.get(name).getDesc());
        //最大线程数
        data.put("maxPoolSize","最大线程数="+poolAdapterMap.get(name).getMaxPoolSize());
        //任务队列大小
        data.put("queueCapacity","任务队列大小="+poolAdapterMap.get(name).getQueueCapacity());
        //活跃线总数
        data.put("activeCount","激活线程="+threadPoolExecutor.getThreadPoolExecutor().getActiveCount());
        //任务总数
        data.put("taskCount","任务总数="+threadPoolExecutor.getThreadPoolExecutor().getTaskCount());
        //等待队列大小
        data.put("queue","等待队列="+threadPoolExecutor.getThreadPoolExecutor().getQueue());
        //完成任务总数
        data.put("completedTaskCount","已经完成任务="+threadPoolExecutor.getThreadPoolExecutor().getCompletedTaskCount());
        //当前线程池数
        data.put("poolSize","当前线程池数="+threadPoolExecutor.getThreadPoolExecutor().getPoolSize());
        //核心线程数
        data.put("corePoolSize","核心线程数="+threadPoolExecutor.getThreadPoolExecutor().getCorePoolSize());
        //线程的空闲时间
        data.put("keepAliveTime","线程的空闲时间(秒)="+threadPoolExecutor.getThreadPoolExecutor().getKeepAliveTime(TimeUnit.SECONDS));
        //线程池曾经创建过的最大线程数量
        data.put("largestPoolSize","线程池曾经创建过的最大线程数量="+threadPoolExecutor.getThreadPoolExecutor().getLargestPoolSize());
        return data;
    }
}