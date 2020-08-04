package amarsoft.com.mail;

import amarsoft.com.utils.DESEncrypt;
import amarsoft.com.utils.DateUtils;

import java.util.Date;





/**
 * @author fyang4 
 */
public class SMSParamBean {
	private String eId = "hub"+ DateUtils.format(new Date(), "MMddHHmmssSSS")+"";
	private String token = DESEncrypt.encrypt(eId);
	
	private String eSubject = "";
	private String eApp = "";
	private String eRecvMails = "";
	private String eDetails = "";
	
	private String eType = ""; 
	private String eSubType = "";
	private String nType = "";
	private String nParams = "";
	private String maxAlarm = "1";
	private String alarmInterval = "";
	private String alarmAlgorithm = "";
	private String noUrl = "";
	
	// constructor
	public SMSParamBean() {
		super();
	}
	public SMSParamBean(String eSubject, String eApp, String eRecvMails, String eDetails, String eType, String eSubType,
			String nType, String nParams, String maxAlarm, String alarmInterval, String alarmAlgorithm) {
		super();
		this.eSubject = eSubject;
		this.eApp = eApp;
		this.eRecvMails = eRecvMails;
		this.eDetails = eDetails;
		this.eType = eType;
		this.eSubType = eSubType;
		this.nType = nType;
		this.nParams = nParams;
		this.maxAlarm = maxAlarm;
		this.alarmInterval = alarmInterval;
		this.alarmAlgorithm = alarmAlgorithm;
	}
	
	// getter/setter
	public String geteSubject() {
		return eSubject;
	}
	public String geteId() {
		return eId;
	}
	public String getToken() {
		return token;
	}
	public void seteSubject(String eSubject) {
		this.eSubject = eSubject;
	}
	public String geteApp() {
		return eApp;
	}
	public void seteApp(String eApp) {
		this.eApp = eApp;
	}
	public String geteRecvMails() {
		return eRecvMails;
	}
	public void seteRecvMails(String eRecvMails) {
		this.eRecvMails = eRecvMails;
	}
	public String geteDetails() {
		return eDetails;
	}
	public void seteDetails(String eDetails) {
		this.eDetails = eDetails;
	}
	public String geteType() {
		return eType;
	}
	public void seteType(String eType) {
		this.eType = eType;
	}
	public String geteSubType() {
		return eSubType;
	}
	public void seteSubType(String eSubType) {
		this.eSubType = eSubType;
	}
	public String getnType() {
		return nType;
	}
	public void setnType(String nType) {
		this.nType = nType;
	}
	public String getnParams() {
		return nParams;
	}
	public void setnParams(String nParams) {
		this.nParams = nParams;
	}
	public String getMaxAlarm() {
		return maxAlarm;
	}
	public void setMaxAlarm(String maxAlarm) {
		this.maxAlarm = maxAlarm;
	}
	public String getAlarmInterval() {
		return alarmInterval;
	}
	public void setAlarmInterval(String alarmInterval) {
		this.alarmInterval = alarmInterval;
	}
	public String getAlarmAlgorithm() {
		return alarmAlgorithm;
	}
	public void setAlarmAlgorithm(String alarmAlgorithm) {
		this.alarmAlgorithm = alarmAlgorithm;
	}
	public String getNoUrl() {
		return noUrl;
	}
	public void setNoUrl(String noUrl) {
		this.noUrl = noUrl;
	}
//	public static void main(String[] args) {
//		System.out.println(DateUtils.getNowTime("MMddHHmmssSSS"));
//	}
}
