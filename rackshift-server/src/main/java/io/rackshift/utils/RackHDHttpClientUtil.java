package io.rackshift.utils;

import io.rackshift.model.RSException;
import io.rackshift.model.RackHDResponse;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RackHDHttpClientUtil {

    private static Logger logger = LoggerFactory.getLogger(RackHDHttpClientUtil.class);

    private static final String HTTPS = "https";
    public static final String CHARSET_UTF_8 = "UTF-8";

    /**
     * 根据url构建HttpClient（区分http和https）
     *
     * @param url 请求地址
     * @return CloseableHttpClient实例
     */
    private static CloseableHttpClient buildHttpClient(String url) {
        try {
            if (url.startsWith(HTTPS)) {
                // https 增加信任设置
                TrustStrategy trustStrategy = new TrustSelfSignedStrategy();
                SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(trustStrategy).build();
                HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
                return HttpClients.custom().setSSLContext(sslContext).setSSLHostnameVerifier(hostnameVerifier).build();
            } else {
                // http
                return HttpClientBuilder.create().build();
            }
        } catch (Exception e) {
            throw new RuntimeException("HttpClient查询失败");
        }
    }

    /**
     * Get http请求
     *
     * @param url    请求地址
     * @param config 配置项，如果null则使用默认配置
     * @return 响应结果字符串
     */
    public static String get(String url, HttpClientConfig config) {
        CloseableHttpClient httpClient = buildHttpClient(url);
        HttpGet httpGet = new HttpGet(url);

        if (config == null) {
            config = new HttpClientConfig();
        }
        try {
            httpGet.setConfig(config.buildRequestConfig());

            Map<String, String> header = config.getHeader();
            for (String key : header.keySet()) {
                httpGet.addHeader(key, header.get(key));
            }

            httpGet.addHeader(HTTP.CONTENT_ENCODING, config.getCharset());

            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity, config.getCharset());
        } catch (Exception e) {
            logger.error("HttpClient查询失败", e);
            throw new RuntimeException("HttpClient查询失败");
        } finally {
            try {
                httpClient.close();
            } catch (Exception e) {
                logger.error("HttpClient关闭连接失败", e);
            }
        }
    }

    /**
     * Post请求，请求内容必须为JSON格式的字符串
     *
     * @param url    请求地址
     * @param config 配置项，如果null则使用默认配置
     * @param json   JSON格式的字符串
     * @return 响应结果字符串
     */
    public static RackHDResponse post(String url, String json, HttpClientConfig config) {
        CloseableHttpClient httpClient = buildHttpClient(url);
        HttpPost httpPost = new HttpPost(url);

        if (config == null) {
            config = new HttpClientConfig();
        }
        RackHDResponse response = new RackHDResponse();
        try {
            httpPost.setConfig(config.buildRequestConfig());

            Map<String, String> header = config.getHeader();
            for (String key : header.keySet()) {
                httpPost.addHeader(key, header.get(key));
            }
            httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json");
            if (config.getCharset() != null) {
                httpPost.addHeader(HTTP.CONTENT_ENCODING, config.getCharset());
            }

            EntityBuilder entityBuilder = EntityBuilder.create();
            entityBuilder.setText(json);
            entityBuilder.setContentType(ContentType.APPLICATION_JSON);
            entityBuilder.setContentEncoding(config.getCharset());
            HttpEntity requestEntity = entityBuilder.build();
            httpPost.setEntity(requestEntity);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpResponseEntity = httpResponse.getEntity();
            String data = EntityUtils.toString(httpResponseEntity, CHARSET_UTF_8);
            response.setData(data);
            int reCode = httpResponse.getStatusLine().getStatusCode();
            String responseMessage = getResponseMessage(reCode).equals("") ? httpResponse.getStatusLine().getReasonPhrase() : getResponseMessage(reCode);
            response.setMessage(responseMessage);
            response.setReCode(reCode);
            return response;
        } catch (Exception e) {
            logger.error("HttpClient查询失败", e);
            throw new RuntimeException("HttpClient查询失败");
        } finally {
            try {
                httpClient.close();
            } catch (Exception e) {
                logger.error("HttpClient关闭连接失败", e);
            }
        }
    }

    /**
     * Post请求，请求内容必须为JSON格式的字符串
     *
     * @param url  请求地址
     * @param json JSON格式的字符串
     * @return 响应结果字符串
     */
    public static RackHDResponse post(String url, String json) {
        return RackHDHttpClientUtil.post(url, json, null);
    }

    /**
     * Post请求，请求内容必须为键值对参数
     *
     * @param url    请求地址
     * @param config 配置项，如果null则使用默认配置
     * @param body   请求内容键值对参数
     * @return 响应结果字符串
     */
    public static String post(String url, Map<String, String> body, HttpClientConfig config) {
        CloseableHttpClient httpClient = buildHttpClient(url);
        HttpPost httpPost = new HttpPost(url);
        if (config == null) {
            config = new HttpClientConfig();
        }
        try {
            httpPost.setConfig(config.buildRequestConfig());

            Map<String, String> header = config.getHeader();
            for (String key : header.keySet()) {
                httpPost.addHeader(key, header.get(key));
            }
            httpPost.addHeader(HTTP.CONTENT_ENCODING, config.getCharset());

            if (body != null && body.size() > 0) {
                List<NameValuePair> nvps = new ArrayList<>();
                for (String key : body.keySet()) {
                    nvps.add(new BasicNameValuePair(key, body.get(key)));
                }
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(nvps, config.getCharset()));
                } catch (Exception e) {
                    logger.error("HttpClient转换编码错误", e);
                    RSException.throwExceptions("HttpClient查询失败");
                }
            }

            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity, config.getCharset());
        } catch (Exception e) {
            logger.error("HttpClient查询失败", e);
            throw new RuntimeException("HttpClient查询失败");
        } finally {
            try {
                httpClient.close();
            } catch (Exception e) {
                logger.error("HttpClient关闭连接失败", e);
            }
        }
    }

    private static String getResponseMessage(int statusCode) {
        String message;
        switch (statusCode) {
            case 400:
                message = "请求参数错误！请联系管理员！";
                break;
            case 404:
                message = "请求错误！请联系管理员！";
                break;
            case 415:
                message = "请求参数类型错误！请联系管理员！";
                break;
            case 403:
                message = "验证失败！用户名密码错误或带外异常！";
                break;
            case 500:
                message = "服务器错误！请联系管理员！";
                break;
            case 200:
                message = "请求成功！";
                break;
            case 201:
                message = "创建成功！";
                break;
            case 204:
                message = "删除成功！";
                break;
            case 412:
                message = "操作失败！请联系管理员！";
                break;
            default:
                message = "";
        }
        return message;
    }

    public static RackHDResponse put(String url, String json, HttpClientConfig config) {
        CloseableHttpClient httpClient = buildHttpClient(url);
        HttpPut httpPost = new HttpPut(url);

        if (config == null) {
            config = new HttpClientConfig();
        }
        RackHDResponse response = new RackHDResponse();
        try {
            httpPost.setConfig(config.buildRequestConfig());

            Map<String, String> header = config.getHeader();
            for (String key : header.keySet()) {
                httpPost.addHeader(key, header.get(key));
            }
            httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json");
            if (config.getCharset() != null) {
                httpPost.addHeader(HTTP.CONTENT_ENCODING, config.getCharset());
            }

            EntityBuilder entityBuilder = EntityBuilder.create();
            entityBuilder.setText(json);
            entityBuilder.setContentType(ContentType.APPLICATION_JSON);
            entityBuilder.setContentEncoding(config.getCharset());
            HttpEntity requestEntity = entityBuilder.build();
            httpPost.setEntity(requestEntity);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpResponseEntity = httpResponse.getEntity();
            String data = EntityUtils.toString(httpResponseEntity, CHARSET_UTF_8);
            response.setData(data);
            int reCode = httpResponse.getStatusLine().getStatusCode();
            String responseMessage = getResponseMessage(reCode).equals("") ? httpResponse.getStatusLine().getReasonPhrase() : getResponseMessage(reCode);
            response.setMessage(responseMessage);
            response.setReCode(reCode);
            return response;
        } catch (Exception e) {
            logger.error("HttpClient查询失败", e);
            throw new RuntimeException("HttpClient查询失败");
        } finally {
            try {
                httpClient.close();
            } catch (Exception e) {
                logger.error("HttpClient关闭连接失败", e);
            }
        }
    }
}
