package cn.hsa.ims.common.contoller;

import cn.hsa.hsaf.core.framework.web.WrapperResponse;
import cn.hsa.hsaf.core.framework.web.exception.BusinessException;
import cn.hsa.ims.common.dto.AeyeFSEntity;
import cn.hsa.ims.common.dto.RequestBodyDto;
import cn.hsa.ims.common.utils.*;
import cn.hsa.ims.constants.OssRouteConstants;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/file")
@Api(description = "文件存储", tags = "文件存储")
public class OssFileController {

	@Value("${comm.oss.waterMarkText:医保局}")
	private String waterMarkText;

	public static final int MAX_BATCH_ITEM = 500;

	protected String getDefaultOssDir(){
		return OssRouteConstants.COMM_FILE_PREFIX;
	}


    /**
     * 上传文件
     */
    @PostMapping(value = "/upload", consumes = "multipart/*", headers = "content-type=multipart/form-data")
    @ApiOperation(value = "文件上传")
    @ResponseBody
    public WrapperResponse upload(
            @ApiParam(value = "是否加水印", type = "boolean", defaultValue = "false") @RequestParam(required = false) boolean watermark,
            @ApiParam(value = "上传的文件", required = true) @RequestParam("file") MultipartFile file) throws Exception {
        AeyeFSEntity entity = new AeyeFSEntity();

        entity.setName(AeyeFileReadWriteUtil.renameSplitUUID(getDefaultOssDir() + file.getOriginalFilename()));
        entity.setUpdatedate(new Date());
        if(watermark){
            byte[] imageByte = ImageWaterMarkUtil.markImage(waterMarkText, file.getInputStream());
            InputStream input = new ByteArrayInputStream(imageByte);
            entity.setInputstream(input);
            entity.setSize(imageByte.length);
        }else{
            entity.setInputstream(file.getInputStream());
            entity.setSize(file.getSize());
        }
        entity = AeyeFSManager.putObject(AeyeFSManager.defaultBucket, entity);
        return WrapperResponse.success(entity.getKeyId());
    }


    /**
     * 上传文件Base64图片
     */
    @PostMapping(value = "/upload/base")
    @ApiOperation(value = "上传Base64图片")
    @ResponseBody
    public WrapperResponse upload(@ApiParam(value = "上传的文件，key为base64String", required = true) @RequestBody RequestBodyDto bodyDto) throws Exception {
        return WrapperResponse.success(AeyeFSManager.putObject(OssRouteConstants.RECOG_PREFIX, Base64.decodeBase64(bodyDto.getBase64String())).getKeyId());
    }


    /**
	 * 下载文件
	 */
	@GetMapping(value = "/download/{keyId}")
	@ApiOperation(value = "文件下载")
	@ResponseBody
	public void download(
			@ApiParam(required = true, value = "文件ID", type = "path") @PathVariable String keyId, @ApiParam(required = false, value = "下载显示文件名")
			@RequestParam(required = false) String fileName, @ApiParam(value = "是否加水印", type = "boolean", defaultValue = "false")
            @RequestParam(required = false) boolean isWatermark, @ApiParam(required = false, value = "水印")
			@RequestParam(required = false) String watermark, @ApiParam(required = false, value = "缩略图")
			@RequestParam(required = false) Boolean scale,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		AeyeFSEntity fsEntity = AeyeFSManager.getObjectEntity(AeyeFSManager.defaultBucket, keyId);
		if(fsEntity != null){
			if(StrUtil.isNotBlank(fileName)){
				fsEntity.setName(fileName);
			}

			// 水印处理
			if(isWatermark){
				if(StrUtil.isBlank(watermark)){
					watermark = waterMarkText;
				}
				byte[] markImage = ImageWaterMarkUtil.markImageFull(watermark, fsEntity.getData());
				fsEntity.setData(markImage != null ? markImage : fsEntity.getData());
				fsEntity.setSize(fsEntity.getData().length);
			}

			response.setContentType(AeyeContentTypeUtil.contentType(fsEntity.getName()));
			// 支持断点续传
			response.setHeader("Accept-Ranges", "bytes");
			String range = request.getHeader("Range");
			byte[] data = fsEntity.getData();
			int length = data.length;

			if (range != null && range.startsWith("bytes=")) {
				String[] ranges = range.substring(6).split("-");
				int start = Integer.parseInt(ranges[0]);
				int end = ranges.length > 1 ? Integer.parseInt(ranges[1]) : length - 1;

				response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
				response.setHeader("Content-Range", "bytes " + start + "-" + end + "/" + length);
				response.setContentLength(end - start + 1);
				response.getOutputStream().write(data, start, end - start + 1);
			} else {
				response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fsEntity.getName(), "UTF-8"));
				if(scale != null && scale){
					BufferedImage sourceImage = ImgUtil.toImage(fsEntity.getData());
					double scaleFactor = (100*1.0) / sourceImage.getWidth();
					int targetWidth = (int)(sourceImage.getWidth() * scaleFactor);
					int targetHeight = (int)(sourceImage.getHeight() * scaleFactor);
					Image scaleImage = ImgUtil.scale(sourceImage, targetWidth, targetHeight);
					ImgUtil.writeJpg(scaleImage, response.getOutputStream());
				}else{
					response.setContentLength(length);
					response.getOutputStream().write(data);
				}
			}
		}
	}

	/**
	 * 下载文件
	 */
	@GetMapping(value = "/download")
	@ApiOperation(value = "文件下载")
	@ResponseBody
	public void downloadNormal(
			@ApiParam(required = true, value = "文件ID", type = "path") @RequestParam String keyId,
			@ApiParam(required = false, value = "下载显示文件名") @RequestParam(required = false) String fileName,
			@ApiParam(value = "是否加水印", type = "boolean", defaultValue = "false") @RequestParam(required = false) boolean isWatermark,
			@ApiParam(required = false, value = "水印") @RequestParam(required = false) String watermark,
			@ApiParam(required = false, value = "缩略图") @RequestParam(required = false) Boolean scale,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		download(keyId, fileName, isWatermark, watermark, scale, request, response);
	}


	/**
	 * 下载文件
	 */
	@PostMapping(value = "/downloadBatch")
	@ApiOperation(value = "文件下载",produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseBody
	public void downloadBatch(
			@ApiParam(required = true, value = "文件ID", type = "path") @RequestBody String[] keyIds,
			@ApiParam(required = false, value = "下载显示文件名") @RequestParam(required = false) String fileName,
			HttpServletResponse response) throws Exception {

		if(StrUtil.isBlank(fileName)){
			fileName = "downloadBatch.zip";
		}

		response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
		response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));

		if(keyIds.length > MAX_BATCH_ITEM){
			throw new BusinessException("批量下载数量超过：" + MAX_BATCH_ITEM);
		}
		Map<String, String> keyMap = new HashMap<>((int)NumberUtil.div(new Float(keyIds.length).floatValue(), 0.75f, 0));
		for(String key : keyIds){
			keyMap.put(key, AeyeFSManager.renameFileName(new String(Base64Utils.urlDecode(key))));
		}
		byte[] data = AeyeFSManager.packZipFiles(fileName, keyMap);
		response.setContentLength((int)data.length);
		response.getOutputStream().write(data);
	}

	/**
	 * 删除文件
	 */
	@DeleteMapping("/delete")
	@ApiOperation(value = "文件删除")
	@ResponseBody
	public WrapperResponse delete(@RequestBody String[] keys){
		for(String key : keys){
			AeyeFSManager.deleteObject(AeyeFSManager.defaultBucket, key);
		}
		return WrapperResponse.success(null);
	}

}
