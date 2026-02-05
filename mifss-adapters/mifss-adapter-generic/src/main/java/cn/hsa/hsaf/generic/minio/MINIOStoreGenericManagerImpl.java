package cn.hsa.hsaf.generic.minio;

import cn.hsa.hsaf.core.fsstore.FSEntity;
import cn.hsa.hsaf.core.fsstore.FSManager;
import io.minio.MinioClient;
import io.minio.PutObjectOptions;

import java.io.InputStream;

public class MINIOStoreGenericManagerImpl implements FSManager {

    private MinioClient minioClient;

    public MINIOStoreGenericManagerImpl(MinioClient minioClient){
        this.minioClient = minioClient;
    }

    /**
     * 注意外部用完了需要关闭流
     * @param bucket
     * @param objectId
     * @return
     */
    @Override
    public FSEntity getObject(String bucket, String objectId) {
        FSEntity entity = new FSEntity();
        InputStream inputStream = null;
        try {
            inputStream = minioClient.getObject(bucket, objectId);
            entity.setKeyId(objectId);
            entity.setName(objectId);
            entity.setInputstream(inputStream);
            entity.setSize(inputStream.available());
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
        return entity;
    }

    /**
     * 注意外部用完需要关闭流
     * @param bucket
     * @param fsEntity
     * @return
     */
    @Override
    public FSEntity putObject(String bucket, FSEntity fsEntity) {
        PutObjectOptions options = new PutObjectOptions(fsEntity.getSize(), -1);
        options.setContentType(fsEntity.getContentType() != null ? fsEntity.getContentType() : "application/octet-stream");
        try {
            minioClient.putObject(bucket, fsEntity.getName(), fsEntity.getInputstream(), options);
            fsEntity.setKeyId(fsEntity.getName());
            return fsEntity;
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean deleteObject(String bucket, String objectId) {
        try{
            minioClient.removeObject(bucket, objectId);
            return true;
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

}
