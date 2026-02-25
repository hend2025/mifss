package cn.hsa.ims.generic.config;

import com.github.tobato.fastdfs.domain.conn.TrackerConnectionManager;
import com.github.tobato.fastdfs.domain.fdfs.StorageNode;
import com.github.tobato.fastdfs.domain.fdfs.StorageNodeInfo;
import com.github.tobato.fastdfs.domain.proto.tracker.TrackerGetFetchStorageCommand;
import com.github.tobato.fastdfs.domain.proto.tracker.TrackerGetStoreStorageCommand;
import com.github.tobato.fastdfs.service.DefaultTrackerClient;
import com.github.tobato.fastdfs.service.TrackerClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
public class AeyeTrackerClient  extends DefaultTrackerClient implements TrackerClient {

    @Autowired
    private TrackerConnectionManager trackerConnectionManager;

    @Autowired
    private AeyeTrackerConfig aeyeTrackerConfig;

    /**
     * 获取存储节点
     */
    @Override
    public StorageNode getStoreStorage() {
        TrackerGetStoreStorageCommand command = new TrackerGetStoreStorageCommand();
        StorageNode node = trackerConnectionManager.executeFdfsTrackerCmd(command);
        mappingIp(node);
        return node;
    }

    /**
     * 按组获取存储节点
     */
    @Override
    public StorageNode getStoreStorage(String groupName) {
        TrackerGetStoreStorageCommand command;
        if (StringUtils.isBlank(groupName)) {
            command = new TrackerGetStoreStorageCommand();
        } else {
            command = new TrackerGetStoreStorageCommand(groupName);
        }
        StorageNode node = trackerConnectionManager.executeFdfsTrackerCmd(command);
        mappingIp(node);
        return node;
    }

    @Override
    public StorageNodeInfo getFetchStorage(String groupName, String filename) {
        TrackerGetFetchStorageCommand command = new TrackerGetFetchStorageCommand(groupName, filename, false);
        StorageNodeInfo nodeInfo = trackerConnectionManager.executeFdfsTrackerCmd(command);
        String mappingIp = aeyeTrackerConfig.getStorageIpMapping().get(nodeInfo.getIp()+":"+nodeInfo.getPort());
        if(StringUtils.isNotBlank(mappingIp)){
            nodeInfo.setIp(mappingIp.split(":")[0]);
            nodeInfo.setPort(Integer.parseInt(mappingIp.split(":")[1]));
        }
        return nodeInfo;
    }

    private void mappingIp(StorageNode node){
        String mappingIp = aeyeTrackerConfig.getStorageIpMapping().get(node.getIp()+":"+node.getPort());
        if(StringUtils.isNotBlank(mappingIp)){
            node.setIp(mappingIp.split(":")[0]);
            node.setPort(Integer.parseInt(mappingIp.split(":")[1]));
        }
    }
}
