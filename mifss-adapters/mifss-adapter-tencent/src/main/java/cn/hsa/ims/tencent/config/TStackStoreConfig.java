package cn.hsa.ims.tencent.config;

import cn.hsa.hsaf.core.fsstore.FSManager;
import cn.hsa.hsaf.tencent.fsstore.FSStoreTencentManagerImpl;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = {"fsstore.type"}, havingValue = "cos")
@Slf4j
public class TStackStoreConfig {

    @Bean
    public FSManager aliStore(@Value("${cos.endpoint}") String endpoint,
                              @Value("${cos.accessKey}") String accessKeyId,
                              @Value("${cos.secretKey}") String secretAccessKey){
        FSStoreTencentManagerImpl storeTencentManager = new FSStoreTencentManagerImpl();
        //COS客户端
        S3ClientOptions s3ClientOptions = new S3ClientOptions();
        s3ClientOptions.setPathStyleAccess(true);
        //凭据
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKeyId, secretAccessKey);
        //客户端
        AmazonS3Client aws3Client = new AmazonS3Client(awsCredentials);
        aws3Client.setEndpoint(endpoint);
        aws3Client.setS3ClientOptions(s3ClientOptions);
        storeTencentManager.setAmazonS3Client(aws3Client);
        return storeTencentManager;
    }

}
