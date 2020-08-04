package amarsoft.com.utils;

import org.apache.http.HttpStatus;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

/**
 * http连接池管控
 * @author mjwang
 * @version 2018-10-19
 */
public class AmarHttpClientPool {
	
	private static volatile CloseableHttpClient client = null;
	private static PoolingHttpClientConnectionManager cm = null;
	private static RequestConfig config = null;
	
	//---------------配置连接属性--------------------------
	private static int requestTimeOut = 120000; //从池中获取请求的时间
	private static int connectTimeout = 120000;  //连接到服务器的时间
	private static int socketTimeout = 120000; //读取信息时间
	//---------------配置连接池属性--------------------------
	private static int maxTotal = 400;  //最大连接数
	private static int defaultMaxPerRoute = 200; //单个地址默认连接数量不超过最大连接数据
	//-----------------------------------------------------


	static {
		config = RequestConfig.custom()
                .setConnectionRequestTimeout(requestTimeOut)  
                .setConnectTimeout(connectTimeout) 
                .setSocketTimeout(socketTimeout).build(); 
		
		cm = new PoolingHttpClientConnectionManager();
		cm.setMaxTotal(maxTotal);
		cm.setDefaultMaxPerRoute(defaultMaxPerRoute);
		
		RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();
		client = HttpClients.custom()
				.setRetryHandler(DefaultHttpRequestRetryHandler.INSTANCE)
				.setDefaultRequestConfig(config)
				.setConnectionManager(cm)
				.setDefaultRequestConfig(globalConfig)
				.build();
	}

	public static CloseableHttpClient getHttpClient() {
		if (null == client) {
			synchronized (AmarHttpClientPool.class) {
				if (null == client){
					RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();
					client = HttpClients.custom()
							.setRetryHandler(DefaultHttpRequestRetryHandler.INSTANCE)
							.setDefaultRequestConfig(config)
							.setConnectionManager(cm)
							.setDefaultRequestConfig(globalConfig)
							.build();
				}
			}
		}
		return client;
	}

	@SuppressWarnings("unused")
	public static String execute() throws Exception {
		String url = "http://...";
		CloseableHttpClient httpClient = AmarHttpClientPool.getHttpClient();
		HttpPost httpPost = new HttpPost(url);
        RequestConfig config = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(3000).setConnectionRequestTimeout(5000).build();
		httpPost.setConfig(config);
		httpPost.setEntity(new StringEntity("请求参数....","utf-8"));
		try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
			int status = response.getStatusLine().getStatusCode();
			if (status != HttpStatus.SC_OK) {
				throw new Exception("调用服务："+url+"失败，返回状态码为："+status);
			}
			String result = EntityUtils.toString(response.getEntity());
			System.out.println("原始报文："+result);
			String startStr = "<ns1:out>";
			int startIndex = result.indexOf(startStr);
			int endIndex = result.indexOf("</ns1:out>");
			assert startIndex >=0 && endIndex >= 0 : "报文体标志位<ns1:out>或</ns1:out>不存在,原始报文："+result;
			EntityUtils.consume(response.getEntity());
			return result.substring(startIndex + startStr.length(), endIndex);
		}
	}
	
	/**
	 * 调用案例
	 */
	public static void main(String[] args) {
		try {
			AmarHttpClientPool.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
