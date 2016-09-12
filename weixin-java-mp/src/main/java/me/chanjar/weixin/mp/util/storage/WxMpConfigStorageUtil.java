package me.chanjar.weixin.mp.util.storage;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;

import me.chanjar.weixin.common.util.http.ApacheHttpClientBuilder;
import me.chanjar.weixin.common.util.http.DefaultApacheHttpHttpClientBuilder;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;

/**
 * 用于多appId配置工具类
 * 
 * @author xiesf
 * @since 2016-08-20
 *
 */
public class WxMpConfigStorageUtil {

	private static ThreadLocal<String> appId = new ThreadLocal<String>();

	private static Map<String, WxMpConfigStorage> wxMpConfigStorageMap;

	private static Map<String, CloseableHttpClient> closeableHttpClientMap;

	private static Map<String, WxMpConfigStorage> getWxMpConfigStorageMap() {
		if (null == wxMpConfigStorageMap)
			wxMpConfigStorageMap = new HashMap<String, WxMpConfigStorage>();
		return wxMpConfigStorageMap;
	}

	private static Map<String, CloseableHttpClient> getCloseableHttpClientMap() {
		if (null == closeableHttpClientMap)
			closeableHttpClientMap = new HashMap<String, CloseableHttpClient>();
		return closeableHttpClientMap;
	}

	public static void putWxMpConfigStorage(String appId,
			WxMpConfigStorage wxMpConfigStorage) {
		getWxMpConfigStorageMap().put(appId, wxMpConfigStorage);

		ApacheHttpClientBuilder apacheHttpClientBuilder = wxMpConfigStorage
				.getApacheHttpClientBuilder();
		if (null == apacheHttpClientBuilder) {
			apacheHttpClientBuilder = DefaultApacheHttpHttpClientBuilder.get();
		}
		apacheHttpClientBuilder
				.httpProxyHost(wxMpConfigStorage.getHttp_proxy_host())
				.httpProxyPort(wxMpConfigStorage.getHttp_proxy_port())
				.httpProxyUsername(wxMpConfigStorage.getHttp_proxy_username())
				.httpProxyPassword(wxMpConfigStorage.getHttp_proxy_password());

		if (wxMpConfigStorage.getSSLContext() != null) {
			@SuppressWarnings("deprecation")
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
					wxMpConfigStorage.getSSLContext(),
					new String[] { "TLSv1" },
					null,
					SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
			apacheHttpClientBuilder.sslConnectionSocketFactory(sslsf);
		}
		getCloseableHttpClientMap().put(appId, apacheHttpClientBuilder.build());

	}
	public static WxMpConfigStorage getWxMpConfigStorage(String appId) {
		WxMpConfigStorage wxMpConfigStorage = getWxMpConfigStorageMap().get(appId);
		return wxMpConfigStorage;
	}
	public static WxMpConfigStorage currentWxMpConfigStorage() {
		String appid = appId.get();
		if (null == appid)
			return null;
		WxMpConfigStorage wxMpConfigStorage = getWxMpConfigStorageMap().get(appId.get());
		return wxMpConfigStorage;
	}

	public static CloseableHttpClient currentCloseableHttpClient() {
		String appid = appId.get();
		if (null == appid)
			return null;
		CloseableHttpClient closeableHttpClient = getCloseableHttpClientMap()
				.get(appId.get());
		return closeableHttpClient;
	}
	
	public static void setCurrentAppId(String appid){
		appId.set(appid);
	}
}
