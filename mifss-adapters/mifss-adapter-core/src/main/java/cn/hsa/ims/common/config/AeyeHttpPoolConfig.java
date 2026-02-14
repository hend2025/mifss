package cn.hsa.ims.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class AeyeHttpPoolConfig {
    /**
     * 连接池里的最大连接数
     */
    @Value("${httpclient.pool.maxTotal:500}")
    private int maxTotal;
    /**
     * 单个服务接口每次能并行接收的请求数量
     */
    @Value("${httpclient.pool.maxPerRoute:100}")
    private int maxPerRoute;
    /**
     * socket超时时间-数据传输过程中等待数据的超时时间
     */
    @Value("${httpclient.pool.socketTimeout:10000}")
    private int socketTimeout;
    /**
     * 连接超时时间-发起请求前的等待时间
     */
    @Value("${httpclient.pool.connectTimeout:10000}")
    private int connectTimeout;
    /**
     * 从连接池获取连接的超时时间
     */
    @Value("${httpclient.pool.connectionRequestTimeout:10000}")
    private int connectionRequestTimeout;
}
