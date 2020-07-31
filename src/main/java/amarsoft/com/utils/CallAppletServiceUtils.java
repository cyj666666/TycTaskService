package amarsoft.com.utils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class CallAppletServiceUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(CallAppletServiceUtils.class);

	public static String post(String url, JSONObject requestParams,String userid) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		try {
			URIBuilder uriBuilder;
			URI uri = null;
			try {
				uriBuilder = new URIBuilder(url);
				uri = uriBuilder.build();
			} catch (URISyntaxException e) {
				LOGGER.error("", e);
			}
			HttpPost httpPost = new HttpPost(uri);
			httpPost.setHeader("Content-Type", "application/json;charset=utf-8");
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("userid", userid);
			httpPost.setEntity(new StringEntity(requestParams.toJSONString(), Charset.forName("UTF-8")));
			response = httpclient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				String resp = null;
				if (entity != null) {
					resp = EntityUtils.toString(entity, "UTF-8");
				}
				return resp;
			} else {
				LOGGER.error("调用AppletService失败，返回响应码:" + statusCode);
			}
		} catch (IOException e) {
			LOGGER.error("调用AppletService发生异常", e);
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (Exception e) {
					LOGGER.error("", e);
				}
			}
			try {
				httpclient.close();
			} catch (IOException e) {
				LOGGER.error("", e);
			}
		}
		return null;
	}

	public static JSONObject get(String url, JSONObject requestParams) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		URI uri = null;
		try {
			URIBuilder uriBuilder = new URIBuilder(url);
			Set<String> set = requestParams.keySet();
			for (String key : set) {
				uriBuilder.setParameter(key, requestParams.getString(key));
			}
			uri = uriBuilder.build();
		} catch (URISyntaxException e) {
			LOGGER.error("", e);
		}
		HttpGet httpgGet = new HttpGet(uri);
		httpgGet.setHeader("Content-Type", "application/json;charset=utf-8");
		httpgGet.setHeader("Accept", "application/json");
//		httpgGet.setHeader("userid", userid);
		httpgGet.setHeader("token","demotoken");
		try {
			response = httpclient.execute(httpgGet);
			String resp = null;
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					resp = EntityUtils.toString(entity, "UTF-8");
				}
			} else {
				LOGGER.error("调用AppletService失败，返回响应码:" + statusCode);
			}
			JSONObject json = JSON.parseObject(resp, Feature.OrderedField);
//			JSONObject json = JSONObject.parseObject(resp);
			return json;
			
//			return JSONObject.toJSONString(json, SerializerFeature.WriteMapNullValue);
		} catch (IOException e) {
			LOGGER.error("调用AppletService发生异常", e);
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (Exception e) {
					LOGGER.error("", e);
				}
			}
			try {
				httpclient.close();
			} catch (IOException e) {
				LOGGER.error("", e);
			}
		}
		return null;
	}

}
