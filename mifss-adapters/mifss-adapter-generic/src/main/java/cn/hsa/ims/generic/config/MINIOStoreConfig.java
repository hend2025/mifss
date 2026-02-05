package cn.hsa.ims.generic.config;

import cn.hsa.hsaf.core.fsstore.FSManager;
import cn.hsa.hsaf.generic.minio.MINIOStoreGenericManagerImpl;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = {"fsstore.type"}, havingValue = "minio")
@Slf4j
public class MINIOStoreConfig {

    @Bean
    public FSManager minIoStore(@Value("${mio.endpoint}") String endpoint,
                                @Value("${mio.accessKeyId}") String accessKeyId,
                                @Value("${mio.secretAccessKey}") String secretAccessKey,
                                @Value("${fsstore.bucket:group1}") String bucket) throws Exception {
        FSManager storeGenericManager = null;
        try {
            MinioClient minioClient = new MinioClient(endpoint, accessKeyId, secretAccessKey);
            if (!minioClient.bucketExists(bucket)) {
                minioClient.makeBucket(bucket);
            }
            storeGenericManager = new MINIOStoreGenericManagerImpl(minioClient);
        }catch (Exception e){
            log.error("minio 初始化失败：", e);
        }
        return storeGenericManager;
    }

}
