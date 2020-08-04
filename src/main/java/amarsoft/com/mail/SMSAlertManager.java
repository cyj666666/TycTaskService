package amarsoft.com.mail;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Set;

import amarsoft.com.utils.AmarHttpClientPool;
import amarsoft.com.utils.ApplicationContextUtils;
import amarsoft.com.utils.DESEncrypt;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;

import com.alibaba.fastjson.JSONObject;






/**
 * 短信通知实现类
 * @author fyang4  2017年8月9日
 * 参数说明：
 * SMSParamBean req:   eSubject 事件主题 ,必填,小于16个字符
				       eApp 发生警告的业务系统，必填,小于16个字符
		[必填]          eRecvMails 接收人邮件地址，用逗号隔开，必填
				       eDetails 事件详情，必填
	---------------------------------------------------------------------------------------
	 			       eType 默认:‘WARNING’ 事件类型，‘INFO’：告知，‘WARNING’：弱警告，‘ALARM’:警告，‘CRITICAL’：崩溃
				       eSubType 业务系统自定义通知类型，默认空
				       nType 通知方式 ： 'MAIL'：邮件,'SMS':短信，默认'MAIL'
		[选填]	       nParams 收件人参数信息，如果通知方式是MAIL，可选填。否则必填。电话号码用逗号隔开
				       maxAlarm 通知最大发送次数，默认2
	 			       alarmInterval 发送间隔单位秒，默认200
				       alarmAlgorithm 间隔计算方式， 等差‘AP’， 斐波那契：‘FIB’，默认'FIB'
 */

public class SMSAlertManager {
	static Logger logger = LoggerFactory.getLogger(SMSAlertManager.class);

	private static String MAIL_API_URL = "http://www.amardata.com/crservice/AmarNotifyService/initEvent";

	public static boolean sendMessage(SMSParamBean req) {
		JSONObject getParam = checkParam(req);
		if(getParam == null)
			return false;
		return execute(getParam);
	}

	public static void main(String[] args) {
		SMSParamBean req = new SMSParamBean();
		req.seteSubject("舆情监测预警信息");
		req.seteType("INFO");
		req.seteApp("发生警告的业务系统");
		req.seteRecvMails("wplu@amarsoft.com");
		req.seteDetails("事件的详情信息");
		req.setNoUrl("true");
		StringBuffer sb = new StringBuffer();
		sb.append("<html><header><style>table tr{word-break:break-all;white-space:warp;}</style></header><body><div style='overflow:hidden;'><font size='15' color='black'><table align='left' border='1' cellpadding='1' cellspacing='0'>"
				+ "<tr style='font-size: 16px;color:white;font-weight:bold'  height='20px' bgcolor='#9e9bd2'>");
		sb.append("<th>机构号</th>");
		sb.append("<th>当日上传新增名单量</th>");
		sb.append("<th>合同约定客户量</th>");
		sb.append("<th>配置拦截阈值</th>");
		sb.append("</tr>");
		sb.append("<tr>");
		sb.append("<td align='center'>" + "bankId" + "</td>");
		sb.append("<td align='center'>" + "curNum" + "</td>");
		sb.append("<td align='center'>" + "heTongNum" + "</td>");
		sb.append("<td align='center'>" + "yuzhi*100" + "%</td>");
		sb.append("</tr>");
		sb.append("</table></font></div></br><span style='color:red;font-weight:bold;'>解决方案提示：检查客户上传的名单否正常，如果正常则需要启动Task批量手动加入。检查名单需要解析请找技术人员帮助！ <a href=\"https://cloud.amardata.com/spy/logon.html\">点击此处进入SAAS项目</a></span></body>"
				+ "</html></br>");
		req.seteDetails(sb.toString());
		sendMessage(req);
	}
	/**
	 * 参数合法检验，参数无误则返回填有参数信息的JSON对象，否则返回null。
	 * @param req 未经校验的发送内容
	 * @return
	 */
	public static JSONObject checkParam(SMSParamBean req){
		JSONObject reqParam = new JSONObject(); // 定义一个JSON数据格式对象，用其保存请求主体数据。

		// 对eSubject，eApp，eRecvMails，eDetails四个必填参数做合法检测
		// 必填参数通过检验后，再判断选填参数。否则return。
		if(StringUtils.isBlank(req.geteSubject())||StringUtils.isBlank(req.geteApp())||StringUtils.isBlank(req.geteRecvMails())||StringUtils.isBlank(req.geteDetails())){
			logger.error("缺少必填参数，执行失败！");
			return null;
		}else{
			if(req.geteSubject().length() > 30){
				reqParam.put("eSubject",req.geteSubject().subSequence(0, 30));
				logger.info("eSubject信息过长，只截取了30个字符进行发送！");
			}else{
				reqParam.put("eSubject", req.geteSubject());
			}
			if(req.geteApp().length() > 30){
				reqParam.put("eApp", req.geteApp().substring(0, 30));
				logger.info("eApp信息过长，只截取了30个字符进行发送！");
			}else{
				reqParam.put("eApp", req.geteApp());
			}
			if(req.geteDetails().length() < 5){
				reqParam.put("eDetails", req.geteDetails()+req.geteDetails()+req.geteDetails()+req.geteDetails()+req.geteDetails());
				logger.info("eDetails过短！");
			}else{
				reqParam.put("eDetails", req.geteDetails());
			}
			//reqParam.put("eDetails", req.geteDetails());
			reqParam.put("eRecvMails", req.geteRecvMails());
		}

		// 检验选填参数
		reqParam.put("token", req.getToken()); // 为JSON对象的各个key值赋值
		reqParam.put("eId", req.geteId());
		reqParam.put("eType", req.geteType());
		reqParam.put("eSubType", req.geteSubType());
		reqParam.put("nType", req.getnType());
		reqParam.put("nParams", req.getnParams());
		reqParam.put("maxAlarm", req.getMaxAlarm());
		reqParam.put("alarmInterval", req.getAlarmInterval());
		reqParam.put("alarmAlgorithm", req.getAlarmAlgorithm());
		reqParam.put("eId", req.geteId());
		reqParam.put("noUrl", req.getNoUrl());
		reqParam.put("token", req.getToken());


		Set<String> keys = reqParam.keySet();

		for (String key : keys) {
			String value = reqParam.getString(key);
			// 参数值为空时自动填写默认值
			if(StringUtils.isBlank(value)){
				if(key.equalsIgnoreCase("eType")){
					reqParam.put(key, "WARNING");
				}else if(key.equals("eSubType")){
					reqParam.put(key, "");
				}else if(key.equals("nType")){
					reqParam.put(key, "MAIL");
				}else if(key.equals("maxAlarm")){
					reqParam.put(key, 1);
				}else if(key.equals("alarmInterval")){
					reqParam.put(key, 200);
				}else if(key.equals("alarmAlgorithm")){
					reqParam.put(key, "FIB");
				}
			}else{  // 选填参数非空时，做合法检验
				if(key.equals("eType")){
					if(!(value.equalsIgnoreCase("WARNING")||value.equalsIgnoreCase("INFO")||value.equalsIgnoreCase("ALARM")||value.equalsIgnoreCase("CRITICAL")))
						reqParam.put(key, "WARNING");
				}else if(key.equals("nType")){
					if(!(value.equalsIgnoreCase("MAIL")||value.equalsIgnoreCase("SMS")))
						reqParam.put(key, "MAIL");
				}else if(key.equals("maxAlarm")){
					if(!(value.matches("[0-9]+")))
						reqParam.put(key, "2");
				}else if(key.equals("alarmInterval")){
					if(!(value.matches("[0-9]+")))
						reqParam.put(key, "120");
				}else if(key.equals("alarmAlgorithm")){
					if(!(value.equalsIgnoreCase("AP")||value.equalsIgnoreCase("FIB")))
						reqParam.put(key, "FIB");
				}
			}
		}

		// 当通知方式为SMS时，联系人信息必填。
		if(reqParam.getString("nType").equalsIgnoreCase("SMS")){
			if(StringUtils.isBlank(reqParam.getString("nParams"))){
				logger.error("接口调用失败： 参数缺失！通知方式为SMS时，联系电话为必填项！");
				return null;
			}
		}
		return reqParam;
	}

	/**
	 * 调用接口，发送信息。发送成功返回true，失败返回false
	 * @param req 经校验后的发送内容
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean execute(JSONObject req){
		CloseableHttpClient httpclient = AmarHttpClientPool.getHttpClient();
		// 请求参数解析
		Set<String> keys = req.keySet();
		StringBuffer param = new StringBuffer();
		for(String key: keys){
			try {
				param = param.append(key).append("=").append(java.net.URLEncoder.encode(req.getString(key),"UTF-8")).append("&");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		String paramTrim = param.toString();
		paramTrim = paramTrim.substring(0,paramTrim.lastIndexOf("&"));
		CloseableHttpResponse response = null;
		try {
			HttpPost post = new HttpPost(MAIL_API_URL); // 定义HttpPost对象并初始化它
			StringEntity reqEntity = new StringEntity(paramTrim,"UTF-8"); // 用StringEntity对象包装请求体数据
			reqEntity.setContentType("application/x-www-form-urlencoded"); // 设置请求头数据传输格式
			post.setEntity(reqEntity); // 设置post请求实体
			response = httpclient.execute(post); // 发送http请求
			JSONObject resp = JSONObject.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));// 将响应结果转化为String,再将String转化为JSON对象
			logger.trace("请求体为:" + EntityUtils.toString(reqEntity,"UTF-8")); // 打印出请求实体
			logger.trace("返回码为:"+response.getStatusLine().getStatusCode()); // 打印http请求返回码
			logger.trace("返回结果为:"+resp);

			// 根据响应结果判断是否发送成功
			if(resp.getString("success").contains("true")){
				return true;
			}else{
				return false;
			}
		} catch (IOException e) {
			logger.error("出错！",e);
			return false;
		} catch (ParseException e1) {
			logger.error("出错！",e1);
			return false;
		} finally{
			if(response != null) {
				try {
					response.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 生成解除邮件
	 * @param noUrl 是否生成解除邮件
	 * @param isHtml 是否是html格式
	 * @param eventId 事件ID，用来生成解除邮件
	 * @return
	 */
	public static String getRemoveEventHref(String noUrl, boolean isHtml, String eventId){
		if(!"true".equalsIgnoreCase(noUrl)){
			return "";
		}
		if(!isHtml) return "";
		StringBuilder sb = new StringBuilder("</br>打开链接解除警报 ");
		String notifyUrl = "http://www.amardata.com/crservice/AmarNotifyService/endEvent?token="+DESEncrypt.encrypt(eventId)+"&amp;eId="+eventId;
		sb.append("<a class=\"zUrl\" target=\"_blank\" href=\""+notifyUrl+"\">")
				.append(notifyUrl)
				.append("</a>");
		return sb.toString();
	}
	/**
	 * 获取要访问的接口URL
	 * @return
	 */






	public static void sendMessage(String bankId, int curNum, int heTongNum, String emailList,double yuzhi) {
		SMSParamBean param = new SMSParamBean();
		param.seteSubject("事件主题：工商变更名单超出阈值！");
		param.seteApp("crservice");
		param.setNoUrl("true");
		param.setnParams("");
		param.seteRecvMails(emailList);
		StringBuffer sb = new StringBuffer();
		sb.append("<html><header><style>table tr{word-break:break-all;white-space:warp;}</style></header><body><div style='overflow:hidden;'><font size='15' color='black'><table align='left' border='1' cellpadding='1' cellspacing='0'>"
				+ "<tr style='font-size: 16px;color:white;font-weight:bold'  height='20px' bgcolor='#9e9bd2'>");
		sb.append("<th>机构号</th>");
		sb.append("<th>当日上传新增名单量</th>");
		sb.append("<th>合同约定客户量</th>");
		sb.append("<th>配置拦截阈值</th>");
		sb.append("</tr>");
		sb.append("<tr>");
		sb.append("<td align='center'>" + bankId + "</td>");
		sb.append("<td align='center'>" + curNum + "</td>");
		sb.append("<td align='center'>" + heTongNum + "</td>");
		sb.append("<td align='center'>" + yuzhi*100 + "%</td>");
		sb.append("</tr>");
		sb.append("</table></font></div></br><span style='color:red;font-weight:bold;'>解决方案提示：检查客户上传的名单否正常，如果正常则需要启动Task批量手动加入。检查名单需要解析请找技术人员帮助！ </span></body>"
				+ getRemoveEventHref(param.getNoUrl(), true, param.geteId()) + "</html></br>");
		param.seteDetails(sb.toString());
		param.setMaxAlarm("1");
		SMSAlertManager.sendMessage(param);
	}

}