package cn.hsa.hsaf.generic.gateway;

import cn.hsa.hsaf.core.framework.util.CurrentUser;
import cn.hsa.hsaf.core.framework.util.SerialUtil;
import cn.hsa.hsaf.core.framework.web.WrapperResponse;
import cn.hsa.hsaf.core.gateway.ApiCaller;
import cn.hsa.hsaf.core.gateway.ApiCallerException;
import cn.hsa.hsaf.core.gateway.ContentBody;
import cn.hsa.hsaf.core.gateway.HttpParameters;
import lombok.Data;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.SocketConfig;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map;

/**
 * 和宇云网关适配
 */
@Data
public class VenusApiCaller implements ApiCaller {
    private static final Logger logger = LoggerFactory.getLogger(VenusApiCaller.class);
    private static String HTTP_HEAD_GATEWAY_USER = "EagleEye-UserData";
    private String name;
    private String gatewayUrl;
    private String accessKey;
    private String securityKey;
    private int socketTimeout = 5000;
    private int connectTimeout = 5000;
    private int soTimeout = 5000;
    private int connectionRequestTimeout = 5000;
    private int maxConnTotal = 10;
    private int maxConnPerRoute = 3;

    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    private CloseableHttpClient httpClient = null;
    private SerialUtil<CurrentUser> currentUserUtil = null;

    public void init() {
        this.httpClient = this.getCloseableHttpClient();
        this.currentUserUtil = new SerialUtil();
    }

    public void close() {
        if (this.httpClient != null) {
            try {
                this.httpClient.close();
            } catch (IOException var2) {
                logger.error("Error", var2);
            }
        }

    }

    @Override
    public String invoke(HttpParameters httpParameters, Map<String, String> map) throws ApiCallerException {
        String httpMethod = httpParameters.getMethod();
        Map<String, String> headerParamsMap = httpParameters.getHeaderParamsMap();
        ContentBody contentBody = httpParameters.getContentBody();
        // 构建和宇云网关请求头参数
        buildHuNanCsbSign(httpParameters);
        String queryString = this.buildQueryString(httpParameters);
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(this.gatewayUrl);
        urlBuilder.append(httpParameters.getApi());
        urlBuilder.append(queryString);
        String requestUrl = urlBuilder.toString();
        logger.debug("requestUrl:{}", requestUrl);
        HttpUriRequest request = null;
        HttpPost postRequest;
        if (HttpMethod.POST.name().equalsIgnoreCase(httpMethod)) {
            postRequest = new HttpPost(requestUrl);
            HttpEntity entity = null;
            if (ContentBody.Type.JSON == contentBody.getContentType()) {
                entity = new StringEntity((String)contentBody.getContentBody(), ContentType.DEFAULT_TEXT);
            } else if (ContentBody.Type.BINARY == contentBody.getContentType()) {
                entity = new ByteArrayEntity((byte[])((byte[])contentBody.getContentBody()));
            }

            postRequest.setEntity((HttpEntity)entity);
            request = postRequest;
        } else {
            if (!HttpMethod.GET.name().equalsIgnoreCase(httpMethod)) {
                logger.error("UnSupported Http Method");
                return null;
            }

            request = new HttpGet(requestUrl);
        }

        this.doHeader((HttpUriRequest)request, headerParamsMap);
        postRequest = null;
        CloseableHttpResponse response = null;

        String result;
        try {
            response = this.httpClient.execute((HttpUriRequest)request);
            logger.debug("Http status code:{}", response.getStatusLine());
            result = EntityUtils.toString(response.getEntity());
        } catch (Exception var21) {
            logger.error("Error while executing http request", var21);
            throw new ApiCallerException("网关访问失败", var21);
        } finally {
            try {
                response.close();
            } catch (Exception var20) {
                logger.warn("Warning while close http request", var20);
            }

        }

        return result;
    }

    private void doHeader(HttpUriRequest request, Map<String, String> headerParamsMap) {
        Iterator iter = headerParamsMap.keySet().iterator();

        String user;
        while(iter.hasNext()) {
            user = (String)iter.next();
            String value = String.valueOf(headerParamsMap.get(user));
            request.addHeader(user, value);
        }
    }

    private String buildQueryString(HttpParameters httpParameters) {
        StringBuilder sb = new StringBuilder("?");
        Map<String, String> urlMap = httpParameters.getParamsMap();
        Iterator iter = urlMap.keySet().iterator();

        while(iter.hasNext()) {
            String key = (String)iter.next();
            String value = String.valueOf(urlMap.get(key));
            if (sb.length() > 1) {
                sb.append("&");
            }

            sb.append(key);
            sb.append("=");
            sb.append(value);
        }
        return sb.toString();
    }

    private CloseableHttpClient getCloseableHttpClient() {
        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
        requestConfigBuilder.setSocketTimeout(this.socketTimeout);
        requestConfigBuilder.setConnectTimeout(this.connectTimeout);
        requestConfigBuilder.setConnectionRequestTimeout(this.connectionRequestTimeout);
        HttpClientBuilder clientBuilder = HttpClientBuilder.create();
        clientBuilder.setDefaultRequestConfig(requestConfigBuilder.build());
        clientBuilder.setDefaultSocketConfig(SocketConfig.custom().setSoTimeout(this.soTimeout).build());
        clientBuilder.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy());
        clientBuilder.setMaxConnTotal(this.maxConnTotal);
        clientBuilder.setMaxConnPerRoute(this.maxConnPerRoute);
        return clientBuilder.build();
    }

    @Override
    public WrapperResponse invokeWR(HttpParameters httpParameters, Map<String, String> map) throws ApiCallerException {
        return null;
    }

    private void buildHuNanCsbSign(HttpParameters httpParameters){
        Map<String, String> headerMap = httpParameters.getHeaderParamsMap();

        Long apiTimestamp = System.currentTimeMillis();

        StringBuffer buffer = new StringBuffer();
        buffer.append("_api_access_key=").append(this.accessKey).append("&")
                .append("_api_name=").append(httpParameters.getApi()).append("&")
                .append("_api_timestamp=").append(apiTimestamp).append("&")
                .append("_api_version=").append(httpParameters.getVersion());

        logger.debug("待签名参数：param={}", buffer.toString());
        String sign = getHmacSHA1(buffer.toString(), this.securityKey);
        logger.debug("生成签名参数：sign={}", sign);
        headerMap.put("_api_name", httpParameters.getApi());
        headerMap.put("_api_version", httpParameters.getVersion());
        headerMap.put("_api_timestamp", apiTimestamp.toString());
        headerMap.put("_api_access_key", this.accessKey);
        headerMap.put("_api_signature", sign);
    }

    private static String getHmacSHA1(String data,String apiSecreKey){
        byte[] result =null;

        try{
            //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
            SecretKeySpec signinKey =new SecretKeySpec(apiSecreKey.getBytes(),HMAC_SHA1_ALGORITHM);
            //生成一个指定 Mac 算法 的 Mac 对象
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            //用给定密钥初始化 Mac 对象
            mac.init(signinKey);
            //完成 Mac 操作
            byte[] rawHmac = mac.doFinal(data.getBytes());
            result = Base64.encodeBase64(rawHmac);
        }catch(NoSuchAlgorithmException e) {
            logger.error(e.getMessage(), e);
        }catch(InvalidKeyException e) {
            logger.error(e.getMessage(), e);
        }

        if(null!= result) {
            return new String(result);
        }else{
            return null;
        }
    }


    /**
     * 16进制加密
     * @param a
     * @return
     */
    private static String byteArrayToHex(byte [] a) {
        int hn, ln, cx;
        String hexDigitChars = "0123456789abcdef";
        StringBuffer buf = new StringBuffer(a.length * 2);
        for(cx = 0; cx < a.length; cx++) {
            hn = ((int)(a[cx]) & 0x00ff) /16 ;
            ln = ((int)(a[cx]) & 0x000f);
            buf.append(hexDigitChars.charAt(hn));
            buf.append(hexDigitChars.charAt(ln));
        }
        return buf.toString();
    }

    public static void main(String args[]){
        String stt = getHmacSHA1("_api_access_key=N1JnWjpXIuzhHdV64VtADlYGfvatetl5gvzI4l&_api_name=sendSms&_api_timestamp=1635823722253&_api_version=1.0.0",
                "SKH52eehG8M2gc5ctrOyoaMS9IMtqyKSzfNW42sG");
        System.out.println(stt);
    }

}
