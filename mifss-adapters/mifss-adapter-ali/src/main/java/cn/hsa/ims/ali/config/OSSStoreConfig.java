package cn.hsa.ims.ali.config;

import cn.hsa.hsaf.ali.fsstore.FSStoreAliManagerImpl;
import cn.hsa.hsaf.core.fsstore.FSManager;
import com.aliyun.oss.OSSClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = {"fsstore.type"}, havingValue = "oss")
@Slf4j
public class OSSStoreConfig {

    @Bean
    public FSManager aliStore(@Value("${oss.endpoint}") String endpoint,
                              @Value("${oss.accessKeyId}") String accessKeyId,
                              @Value("${oss.secretAccessKey}") String secretAccessKey){
        FSStoreAliManagerImpl storeAliManager = new FSStoreAliManagerImpl();
        OSSClient client = new OSSClient(endpoint, accessKeyId, secretAccessKey);
        storeAliManager.setOssClient(client);
        return storeAliManager;
    }

}
