package cn.hsa.ims.constants;

/**
 * 文件存储路径前缀-强制性全局前缀ZHY/[month]/
 * 不要以/开头，最终完整示例：ZHY/202202/recog/08
 */
public class OssRouteConstants {

    /**
     * 通用文件-路径前缀
     */
    public static final String COMM_FILE_PREFIX = "commonFile/";

    /**
     * 通用视频-路径前缀
     */
    public static final String COMM_VIDEO_PREFIX = "commonVideo/";

    /**
     * 视频下载-路径前缀
     */
    public static final String VOLA_VIDEO_PREFIX = "volaVideo/[day]/";

    /**
     * 抓拍照-路径前缀
     */
    public static final String CAPTURE_PREFIX = "capture/";
    /**
     * 活体临时文件路径
     */
    public static final String LIVE_TEMP_PREFIX = "live/temp/[day]/";
    /**
     * 视频结构化人脸-路径前缀
     */
    public static final String VIDEO_STR_FACE_PREFIX = "videoImage/";
    /**
     * 认证照-路径前缀
     */
    public static final String RECOG_PREFIX = "recog/[day]/";
    /**
     * 模板照-路径前缀
     */
    public static final String MODEL_PREFIX = "model/";
    /**
     * 其它分类归集-路径前缀
     */
    public static final String OTHER_PREFIX = "other/[day]/";

    /**
     * 人脸检测收集-路径前缀
     */
    public static final String FACE_CHECK_COLLECT_PREFIX = "faceCheckCollect/[day]/";

    /**
     * 人脸活体收集-路径前缀
     */
    public static final String FACE_ALIVE_COLLECT_PREFIX = "faceAliveCollect/[day]/";


    /**
     * 表格导出
     */
    public static final String EXCEL_PREFIX = "excel/[day]/";


}
