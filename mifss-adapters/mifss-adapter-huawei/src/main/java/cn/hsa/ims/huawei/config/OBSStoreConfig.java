package cn.hsa.ims.huawei.config;

import cn.hsa.hsaf.core.fsstore.FSManager;
import com.huawei.hsaf.fsstore.FSStoreHuaweiManagerImpl;
import com.obs.services.ObsClient;
import com.obs.services.ObsConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = {"fsstore.type"}, havingValue = "obs")
@Slf4j
public class OBSStoreConfig {

    @Bean
    public FSManager huaweiStore(@Value("${obs.endpoint}") String endpoint,
                                 @Value("${obs.accessKeyId}") String accessKeyId,
                                 @Value("${obs.secretAccessKey}") String secretAccessKey){
        FSStoreHuaweiManagerImpl huaweiManager = new FSStoreHuaweiManagerImpl();

        ObsConfiguration obsConfiguration = new ObsConfiguration();
        obsConfiguration.setPathStyle(true);
        obsConfiguration.setSocketTimeout(30000);
        obsConfiguration.setConnectionTimeout(10000);
        obsConfiguration.setEndPoint(endpoint);

        ObsClient client = new ObsClient(accessKeyId, secretAccessKey, obsConfiguration);
        huaweiManager.setObsClient(client);
        return huaweiManager;
    }

}
