package cn.hsa.hsaf.generic.minio;

import cn.hsa.hsaf.core.fsstore.FSEntity;
import cn.hsa.hsaf.generic.fsstore.FSStoreGenericManagerImpl;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import io.minio.MinioClient;

import java.util.regex.Pattern;

@Deprecated
public class MINIOAndFdfsStoreGenericManagerImpl extends MINIOStoreGenericManagerImpl {

    private FSStoreGenericManagerImpl fdfsClient = null;

    private static String fdfs_match_regex = "^M[A-Z|0-9][A-Z|0-9]/.*$";
    private static Pattern fdfs_pattern = Pattern.compile(fdfs_match_regex);

    public MINIOAndFdfsStoreGenericManagerImpl(MinioClient minioClient, FastFileStorageClient fastFileStorageClient) {
        super(minioClient);
        fdfsClient = new FSStoreGenericManagerImpl();
        fdfsClient.setFastFileStorageClient(fastFileStorageClient);
    }

    @Override
    public FSEntity getObject(String bucket, String objectId) {
        if(fdfs_pattern.matcher(objectId).matches()){
            return fdfsClient.getObject(bucket, objectId);
        }
        return super.getObject(bucket, objectId);
    }

    @Override
    public FSEntity putObject(String bucket, FSEntity fsEntity) {
        return super.putObject(bucket, fsEntity);
    }

    @Override
    public boolean deleteObject(String bucket, String objectId) {
        if(fdfs_pattern.matcher(objectId).matches()){
            return fdfsClient.deleteObject(bucket, objectId);
        }
        return super.deleteObject(bucket, objectId);
    }

    public static void main(String[] args){
        System.out.println(fdfs_pattern.matcher("M00/").matches());
    }
}
