package cn.hsa.ims.common.config;

import cn.hsa.hsaf.core.framework.web.exception.BusinessException;
import cn.hsa.ims.common.utils.AeyeSpringContextUtils;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Future;

public abstract class AbstractThreadAdapter<Param> {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    public boolean start(String poolName,Param params) throws Exception{
        Future future = submit(poolName, params);
        return future == null ? false : true;
    }

    public Future submit(String poolName,Param params) throws Exception{
        try{
            ThreadPoolTaskExecutor threadPoolExecutor = AeyeSpringContextUtils.getBean(poolName);
            if(threadPoolExecutor != null){
                Future future = threadPoolExecutor.submit(new Runnable() {
                    @Override
                    public void run() {
                        excute(params);
                    }
                });
                return future;
            }else{
                throw new BusinessException("线程池未配置："+poolName);
            }

        }catch (TaskRejectedException e){
            logger.error(new StringBuffer("超出服务器资源限制，拒绝处理!AeyeThreadAdapter::start")
                    .append("；当前线程池：").append(JSON.toJSONString(SpringAsyncConfig.showThreadPoolInfo(poolName))).toString()
            );
        }
        return null;
    }


    /**
     * 批量线程并发实现逻辑-当前抽象类继承对象实现的方法excute会被提交到线程池并发访问，但是实例对象只有一个
     * 因此可以申明成员变量回写执行结果集合或者自己再Param参数定义回写结果
     * starMore会阻塞直到全部结束，注意这里不会设置超时，关注代码自身excute代码逻辑
     * @param poolName
     * @param params
     * @throws Exception
     */
    public void startMore(String poolName, List<Param> params) throws Exception{
        try{
            List<Future> futures = new ArrayList<>();
            for(Param param : params){
                Future future = submit(poolName, param);
                if(future == null){
                    logger.error("批量启动线程执行异常!!!-poolName:[{}]-params:[{}]", poolName, JSON.toJSONString(param));
                    break;
                }
                futures.add(future);
            }
            while(true){
                Iterator<Future> iterator = futures.iterator();
                while (iterator.hasNext()) {
                    Future future = iterator.next();
                    if (future.isDone()) {
                        iterator.remove();
                    }
                }

                if(futures.size() == 0){
                    break;
                }
                Thread.sleep(2000);
            }
        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }
    }

    protected abstract void excute(Param params);
}
