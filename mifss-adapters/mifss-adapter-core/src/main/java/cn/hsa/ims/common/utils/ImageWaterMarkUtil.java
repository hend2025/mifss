package cn.hsa.ims.common.utils;

import cn.hsa.hsaf.core.framework.web.exception.BusinessException;
import cn.hsa.ims.common.dto.AeyeImageDto;
import cn.hutool.core.text.CharSequenceUtil;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class ImageWaterMarkUtil {
    static Logger log = LoggerFactory.getLogger(ImageWaterMarkUtil.class);
    /**
     * 水印文字字体
     */
    private static Font font = null;

    static {
        ClassPathResource classPathResource = new ClassPathResource("fonts/msyhl.ttc");
        try{
            InputStream inputStream =classPathResource.getInputStream();
            font = Font.createFont(Font.PLAIN, inputStream);
            font = font.deriveFont(15f);
            log.warn("全局加载字体：{}", font.getName());
        }catch (Exception ex){
            log.error(ex.getMessage(), ex);
        }finally {
            try{
                IOUtils.closeQuietly(classPathResource.getInputStream());
            }catch (Exception ex){
                log.error(ex.getMessage(), ex);
            }
        }
    }

    /**
     * 水印透明度 - 数字越小越透明
     */
    private static float alpha = 0.5f;

    /**
     * 水印横向位置
     */
    private static int positionWidth = -20;

    /**
     * 水印纵向位置
     */
    private static int positionHeight = 80;

    /**
     * 水印文字颜色
     */
    private static Color color = Color.WHITE;

    /**
     * 给图片添加水印文字
     * 只能处理jpg图片
     * @param text   水印文字
     * @param srcImgPath 源图片路径
     */
    public static byte[] markImage(String text, String srcImgPath) throws Exception{
        return markImage(text, new FileInputStream(srcImgPath));
    }

    public static byte[] markImage(String text, InputStream imageStream) throws Exception{
        AeyeImageDto imageDto = new AeyeImageDto();
        return markImage(text, ".jpg", imageStream, imageDto);
    }

    public static byte[] markImageFull(String text, String srcImgPath) throws Exception{
        return markImageFull(text, new FileInputStream(srcImgPath));
    }

    public static byte[] markImageFull(String text, byte[] bytes) throws Exception{
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        byte[] data = markImageFull(text, inputStream);
        return data == null || data.length == 0 ? bytes : data;
    }

    public static byte[] markImageFull(String text, InputStream imageStream) throws Exception{
        AeyeImageDto imageDto = new AeyeImageDto();
        imageDto.setFull(true);
        return markImage(text, ".jpg", imageStream, imageDto);
    }

    public static byte[] markImage(String text, String fileName,InputStream imageStream, AeyeImageDto imageDto){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try{
            String type = fileName.substring(fileName.indexOf(".") + 1, fileName.length());

            Image srcImg = ImageIO.read(imageStream);
            int imgWidth = srcImg.getWidth(null);
            int imgHeight = srcImg.getHeight(null);

            BufferedImage buffImg = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
            // 2、得到画笔对象
            Graphics2D g = buffImg.createGraphics();
            // 3、设置对线段的锯齿状边缘处理
            RenderingHints rh=new RenderingHints(RenderingHints. KEY_TEXT_ANTIALIASING, RenderingHints. VALUE_TEXT_ANTIALIAS_ON);
            rh.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            rh.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            g.setRenderingHints(rh);

            g.drawImage(srcImg.getScaledInstance(imgWidth, imgHeight, Image.SCALE_SMOOTH), 0, 0, null);
            // 4、设置水印旋转
            if (null != imageDto.getDegree()) {
                g.rotate(Math.toRadians(imageDto.getDegree()), (double) buffImg.getWidth() / 2, (double) buffImg.getHeight() / 2);
            }
            // 5、设置水印文字颜色
            g.setColor(color);
            g.setBackground(Color.red);
            // 6、设置水印文字Font
            if(font != null){
                g.setFont(font);
            }
            // 7、设置水印文字透明度
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            // 8、第一参数->设置的内容，后面两个参数->文字在图片上的坐标位置(x,y)

            int markWidth = 15 * getLength(text);
            int markHeight = 15;

            if(imageDto.isFull()){
                // 循环添加水印
                int x = -imgWidth / 2;
                int y = -imgHeight / 2;

                while (x < imgWidth * 1.5) {
                    y = -imgHeight / 2;
                    while (y < imgHeight * 1.5) {
                        g.drawString (text, x, y);

                        y += markHeight + imageDto.getYMove();
                    }
                    x += markWidth + imageDto.getXMove();
                }
            }else{
                int widthDiff = imgWidth - markWidth;
                int heightDiff = imgHeight - markHeight;
                if(positionWidth < 0){
                    positionWidth = widthDiff / 2;
                }else if(positionWidth > widthDiff){
                    positionWidth = widthDiff;
                }
                if(positionHeight < 0){
                    positionHeight = heightDiff / 2;
                }else if(positionHeight > heightDiff){
                    positionHeight = heightDiff;
                }
                g.drawString(text, positionWidth, positionHeight + markHeight);
            }

            // 9、释放资源
            g.dispose();
            // 10、生成图片
            ImageIO.write(buffImg, type.toUpperCase(), outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            log.error("水印字体异常，可能服务器需要安装字体【yum install fontconfig】，异常信息："+e.getMessage());
        } finally {
            try {
                if (null != imageStream){
                    IOUtils.closeQuietly(imageStream);
                }
                if (null != outputStream){
                    IOUtils.closeQuietly(outputStream);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return null;
    }

    public static int getLength(String text) {
        int textLength = text.length();
        int length = textLength;
        for (int i = 0; i < textLength; i++) {
            if (String.valueOf(text.charAt(i)).getBytes().length > 1) {
                length++;
            }
        }
        return (length % 2 == 0) ? length / 2 : length / 2 + 1;
    }

    public static void main(String[] args) throws Exception{
        String srcImgPath = "D:\\temp\\1\\1111.jpg";
        String text = "智慧眼";



        System.out.println("给图片添加水印文字开始...");
        byte[] bb = markImageFull("医保监管", srcImgPath);
        OutputStream outputStream1 = new FileOutputStream("D:\\temp\\1\\xxx.jpg");
        outputStream1.write(bb);
        outputStream1.close();

        System.out.println("给图片添加水印文字结束...");

    }

    public static boolean isValidImage(MultipartFile face) throws BusinessException,IOException {
        if (face == null) {
            return true;
        }
        if(CharSequenceUtil.isBlank(face.getOriginalFilename())){
            throw new BusinessException("文件名不能为空");
        }
        // 文件后缀校验
        if (!face.getOriginalFilename().endsWith(".jpg") &&
                !face.getOriginalFilename().endsWith(".jpeg") &&
                !face.getOriginalFilename().endsWith(".png")) {
            throw new BusinessException("人脸照格式错误，仅支持jpg/jpeg/png格式!");
        }

        // MIME类型校验
        String contentType = face.getContentType();
        if (!"image/jpeg".equals(contentType) && !"image/png".equals(contentType)) {
            throw new BusinessException("人脸照MIME类型错误!");
        }

        // 文件头信息校验
        byte[] faceBytes = face.getBytes();
        if (!isValidImageHeader(faceBytes)) {
            throw new BusinessException("文件头信息与后缀不匹配!");
        }
        return true;
    }

    private static boolean isValidImageHeader(byte[] fileBytes) {
        if (fileBytes.length < 4) return false;

        // JPEG文件头: FF D8 FF
        if (fileBytes[0] == (byte) 0xFF && fileBytes[1] == (byte) 0xD8) {
            return true;
        }

        // PNG文件头: 89 50 4E 47
        if (fileBytes[0] == (byte) 0x89 && fileBytes[1] == (byte) 0x50 &&
                fileBytes[2] == (byte) 0x4E && fileBytes[3] == (byte) 0x47) {
            return true;
        }

        return false;
    }
}