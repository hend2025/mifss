package cn.hsa.ims.generic.config;

import cn.hsa.hsaf.core.fsstore.FSManager;
import cn.hsa.hsaf.generic.minio.MINIOAndFdfsStoreGenericManagerImpl;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * minio兼容fdfs
 */
@Configuration
@ConditionalOnProperty(
        name = {"fsstore.type"},
        havingValue = "minioAndFdfs"
)
@Deprecated
@Slf4j
public class MINIOAndFdfsStoreConfig {

    @Bean
    public FSManager minIoStore(@Value("${mio.endpoint}") String endpoint,
                                @Value("${mio.accessKeyId}") String accessKeyId,
                                @Value("${mio.secretAccessKey}") String secretAccessKey,
                                @Value("${fsstore.bucket:group1}") String bucket,
                                FastFileStorageClient fileStorageClient) throws Exception {
        MinioClient minioClient = new MinioClient(endpoint, accessKeyId, secretAccessKey);
        if(!minioClient.bucketExists(bucket)){
            minioClient.makeBucket(bucket);
        }
        MINIOAndFdfsStoreGenericManagerImpl storeGenericManager = new MINIOAndFdfsStoreGenericManagerImpl(minioClient, fileStorageClient);
        log.warn("开源版初始化分布式文件存储技术：MinIOAndFdfs 兼容模式!!!");
        return storeGenericManager;
    }
}
