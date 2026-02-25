package cn.hsa.ims.generic.config;

import com.github.tobato.fastdfs.FdfsClientConstants;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = FdfsClientConstants.ROOT_CONFIG_PREFIX)
public class AeyeTrackerConfig{


    /**
     * storage地址映射列表
     */
    private Map<String, String> storageIpMapping = new HashMap<>(16);

}

