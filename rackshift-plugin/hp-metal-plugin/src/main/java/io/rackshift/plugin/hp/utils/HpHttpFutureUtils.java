package io.rackshift.plugin.hp.utils;

import io.rackshift.metal.sdk.util.HttpsTrustManager;
import io.rackshift.metal.sdk.util.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class HpHttpFutureUtils {
    private static SSLConnectionSocketFactory factory;
    private static CookieStore cookieStore;
    private static ExecutorService executorService;
    private static final long waitMinutes = 5l;
    private static List<String> sslv1_1IpList;

    static {
        sslv1_1IpList = new ArrayList();
        sslv1_1IpList.add("10.153.5.50");

        cookieStore = new BasicCookieStore();
        SSLContext sslContext = null;
        try {
            sslContext = SSLContexts.custom().useSSL().build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        try {
            sslContext.init(null, new X509TrustManager[]{new HttpsTrustManager()}, new SecureRandom());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        factory = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        executorService = Executors.newFixedThreadPool(10);
    }

    private static CloseableHttpClient buildHttpClientTLSv1_1() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = SSLContext.getInstance("TLSv1.1");
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
        sslContext.init(null, new TrustManager[]{trustManager}, null);
        SSLConnectionSocketFactory sslcf = new SSLConnectionSocketFactory(sslContext, new String[]{"TLSv1.1"}, null, NoopHostnameVerifier.INSTANCE);
        return HttpClients.custom().setSSLSocketFactory(sslcf).build();
    }

    public static CloseableHttpClient getOneHttpClient(String url) throws KeyManagementException, NoSuchAlgorithmException {
        if (sslv1_1IpList.stream().filter(r -> url.contains(r)).collect(Collectors.toList()).size() > 0) {
            return buildHttpClientTLSv1_1();
        }
        return HttpClients.custom().setDefaultCookieStore(cookieStore).setSSLSocketFactory(factory).build();
    }

    public static String getHttps(String url, Map<String, String> headers) {
        try {
            return executorService.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    CloseableHttpClient httpClient = getOneHttpClient(url);
                    HttpGet httpGet = new HttpGet(url);
                    HttpEntity entity = null;
                    try {

                        if (headers != null) {
                            for (Map.Entry<String, String> entry : headers.entrySet()) {
                                httpGet.addHeader(entry.getKey(), entry.getValue());
                            }
                        }

                        HttpResponse response = httpClient.execute(httpGet);
                        if (response.getEntity() == null) {
                            return null;
                        }
                        entity = response.getEntity();
                        return EntityUtils.toString(entity);
                    } catch (Exception e) {
                        LogUtil.error(String.format("HttpClient getHttpsResponse执行失败！url : {%s}, e：{%s}", url, e));
                    } finally {
                        if (httpClient != null) {
                            try {
                                httpClient.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    return null;
                }
            }).get(waitMinutes, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            LogUtil.error(String.format("HttpClient getHttpsResponse执行失败！url : {%s}, e：{%s}", url, e));
        } catch (ExecutionException e) {
            LogUtil.error(String.format("HttpClient getHttpsResponse执行失败！url : {%s}, e：{%s}", url, e));
        } catch (TimeoutException e) {
            LogUtil.error(String.format("HttpClient getHttpsResponse执行失败！失败原因：超时！url : {%s}, e：{%s}", url, e));
        }
        return null;
    }

    public static String postHttps(String url, String payload, String contenttype) {
        return postHttps(url, payload, contenttype, null);
    }

    public static HttpResponse postHttpsResponse(String url, String payload, String contenttype, Map<String, String> headers) {
        try {
            return executorService.submit(new Callable<HttpResponse>() {
                @Override
                public HttpResponse call() throws Exception {
                    HttpPost httpPost = new HttpPost(url);
                    CloseableHttpClient httpClient = getOneHttpClient(url);
                    try {
                        if (headers != null) {
                            for (Map.Entry<String, String> entry : headers.entrySet()) {
                                httpPost.addHeader(entry.getKey(), entry.getValue());
                            }
                        }
                        if (StringUtils.isNotBlank(contenttype)) {
                            httpPost.addHeader("Content-Type", contenttype);
                        }
                        if (StringUtils.isNotBlank(payload)) {
                            HttpEntity httpEntity = new StringEntity(payload, "utf-8");
                            httpPost.setEntity(httpEntity);
                        }
                        return httpClient.execute(httpPost);
                    } catch (Exception e) {
                        throw new RuntimeException("url请求失败：url" + url);
                    } finally {
                        if (httpClient != null) {
                            try {
                                httpClient.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }).get(waitMinutes, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            LogUtil.error(String.format("HttpClient getHttpsResponse执行失败！url : {%s}, e：{%s}", url, e));
        } catch (ExecutionException e) {
            LogUtil.error(String.format("HttpClient getHttpsResponse执行失败！url : {%s}, e：{%s}", url, e));
        } catch (TimeoutException e) {
            LogUtil.error(String.format("HttpClient getHttpsResponse执行失败！失败原因：超时！url : {%s}, e：{%s}", url, e));
        }
        return null;
    }

    public static String postHttps(String url, HttpEntity entity, String contenttype, Map<String, String> headers) {
        try {
            return executorService.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    HttpPost httpPost = new HttpPost(url);
                    HttpEntity r = null;
                    CloseableHttpClient httpClient = getOneHttpClient(url);
                    try {
                        if (headers != null) {
                            for (Map.Entry<String, String> entry : headers.entrySet()) {
                                httpPost.addHeader(entry.getKey(), entry.getValue());
                            }
                        }
                        if (StringUtils.isNotBlank(contenttype)) {
                            httpPost.addHeader("Content-Type", contenttype);
                        }
                        if (entity != null) {
                            httpPost.setEntity(entity);
                        }
                        r = httpClient.execute(httpPost).getEntity();
                        return EntityUtils.toString(r);
                    } catch (Exception e) {
                        throw new RuntimeException("url请求失败：url" + url);
                    } finally {
                        if (httpClient != null) {
                            try {
                                httpClient.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }).get(waitMinutes, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            LogUtil.error(String.format("HttpClient getHttpsResponse执行失败！url : {%s}, e：{%s}", url, e));
        } catch (ExecutionException e) {
            LogUtil.error(String.format("HttpClient getHttpsResponse执行失败！url : {%s}, e：{%s}", url, e));
        } catch (TimeoutException e) {
            LogUtil.error(String.format("HttpClient getHttpsResponse执行失败！失败原因：超时！url : {%s}, e：{%s}", url, e));
        }
        return null;
    }

    public static String postHttps(String url, String payload, String contenttype, Map<String, String> headers) {
        try {
            return executorService.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    HttpPost httpPost = new HttpPost(url);
                    CloseableHttpClient httpClient = getOneHttpClient(url);
                    try {
                        if (headers != null) {
                            for (Map.Entry<String, String> entry : headers.entrySet()) {
                                httpPost.addHeader(entry.getKey(), entry.getValue());
                            }
                        }
                        if (StringUtils.isNotBlank(contenttype)) {
                            httpPost.addHeader("Content-Type", contenttype);
                        }
                        if (StringUtils.isNotBlank(payload)) {
                            HttpEntity httpEntity = new StringEntity(payload, "utf-8");
                            httpPost.setEntity(httpEntity);
                        }
                        return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
                    } catch (Exception e) {
                        throw new RuntimeException("url请求失败：url" + url);
                    } finally {
                        if (httpClient != null) {
                            try {
                                httpClient.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }).get(waitMinutes, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            LogUtil.error(String.format("HttpClient getHttpsResponse执行失败！url : {%s}, e：{%s}", url, e));
        } catch (ExecutionException e) {
            LogUtil.error(String.format("HttpClient getHttpsResponse执行失败！url : {%s}, e：{%s}", url, e));
        } catch (TimeoutException e) {
            LogUtil.error(String.format("HttpClient getHttpsResponse执行失败！失败原因：超时！url : {%s}, e：{%s}", url, e));
        }
        return null;
    }
}
