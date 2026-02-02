package cn.hsa.ims.generic.config;

import cn.hsa.hsaf.core.fsstore.FSManager;
import cn.hsa.hsaf.generic.fsstore.FSStoreGenericManagerImpl;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = {"fsstore.type"}, havingValue = "fdfs", matchIfMissing = true)
@Slf4j
public class FDFSStoreConfig {

    @Bean
    public FSManager fastDfsStore(FastFileStorageClient fastFileStorageClient){
        FSStoreGenericManagerImpl storeGenericManager = new FSStoreGenericManagerImpl();
        storeGenericManager.setFastFileStorageClient(fastFileStorageClient);
        log.warn("开源版初始化分布式文件存储技术：fdfs");
        return storeGenericManager;
    }

}
