package amarsoft.com.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;





public class DateUtils {
	
	public static String getNowTime(String pattern) {
		return new SimpleDateFormat(pattern).format(new Date());
	}
	
	public static Date add(Date date,int addDay) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, addDay);
		return cal.getTime();
	}
	
	public static String format(Date date,String pattern) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.format(date);
	}	
	
    public static long getValidTimeLong() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -7);
        return cal.getTime().getTime();
    }
    
    public static boolean checkValidTime(String time){
    	@SuppressWarnings("deprecation")
		Date hisTime = new Date(time);
    	long hisTimeLong = hisTime.getTime();
        long validTimeLong = getValidTimeLong();
        return hisTimeLong > validTimeLong ? true : false;
    }
    
    /**
	 * 爬虫是否在有效期(24小时)内
	 * @param beginTime 爬虫表时间
	 * @return
	 */
	public static boolean isInValidDate(String beginTime){
		if(StringUtils.isBlank(beginTime)) return false;
		Date beginDate = parseDate(beginTime);
		if(beginDate==null) return false;
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR_OF_DAY, -24);
		Date validDate = cal.getTime();
		return beginDate.compareTo(validDate)>=0;
	}
	
	/**
	 * 解析时间格式，如：yyyy/MM/dd HH:mm:ss
	 * @param time
	 * @return
	 */
	public static Date parseDate(String time){
		return parseDate(time, "yyyy-MM-dd HH:mm:ss");
	}
	
	/**
	 * 解析时间格式，如：yyyy/MM/dd HH:mm:ss
	 * @param time
	 * @return
	 */
	public static Date parseDate(String time, String format){
		if(StringUtils.isBlank(time)) return null;
		if(StringUtils.isBlank(format)) format="yyyy-MM-dd HH:mm:ss";
		try{  
            Date date = new SimpleDateFormat(format).parse(time);
            return date;
        } catch (ParseException e){  
        };
        return null;
	}
	
	
	public static String getFormatDate(String date, String format) {
		String returnDate = null;
		if (format == null || "".equals(format.trim()))
			format = "yyyy/MM/dd HH:mm:ss";// 默认日期格式
		try {
			returnDate = FormatDate(date,format);
		} catch (Exception e) {
			returnDate = date;// 若转换出错返回原日期值
		}
		return returnDate;
	}
	
	public static String getFormatDate(String date) {
		String format = "yyyy/MM/dd HH-mm-ss";
		return getFormatDate(date, format);
	}
	
	@SuppressWarnings("finally")
	private static String FormatDate(String dateStr,String format) {

		HashMap<String, String> dateRegFormat = new HashMap<String, String>();
		dateRegFormat.put("^\\d{4}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}\\D*$",
				"yyyy-MM-dd-HH-mm-ss");// 2014年3月12日 13时5分34秒，2014-03-12
										// 12:05:34，2014/3/12 12:5:34
		dateRegFormat.put("^\\d{4}\\D+\\d{1,2}\\D+\\d{1,2}\\D*$", "yyyy-MM-dd");// 2014年3月12日
		dateRegFormat.put("^\\d{4}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}\\D*$", "yyyy-MM-dd-HH-mm");// 2014-03-12
																												// 12:05
		dateRegFormat.put("^\\d{4}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}\\D*$", "yyyy-MM-dd-HH");// 2014-03-12
																								// 12
		dateRegFormat.put("^\\d{4}\\D+\\d{2}\\D+\\d{2}$", "yyyy-MM-dd");// 2014-03-12
		dateRegFormat.put("^\\d{4}\\D+\\d{2}\\D*$", "yyyy-MM");// 2014-03
		dateRegFormat.put("^\\d{4}\\D*$", "yyyy");// 2014
		dateRegFormat.put("^\\d{14}$", "yyyyMMddHHmmss");// 20140312120534
		dateRegFormat.put("^\\d{12}$", "yyyyMMddHHmm");// 201403121205
		dateRegFormat.put("^\\d{10}$", "yyyyMMddHH");// 2014031212
		dateRegFormat.put("^\\d{8}$", "yyyyMMdd");// 20140312
		dateRegFormat.put("^\\d{6}$", "yyyyMM");// 201403
		dateRegFormat.put("^\\d{2}\\s*:\\s*\\d{2}\\s*:\\s*\\d{2}$", "yyyy-MM-dd-HH-mm-ss");// 13:05:34
																							// 拼接当前日期
		dateRegFormat.put("^\\d{2}\\s*:\\s*\\d{2}$", "yyyy-MM-dd-HH-mm");// 13:05
																			// 拼接当前日期
		dateRegFormat.put("^\\d{2}\\D+\\d{1,2}\\D+\\d{1,2}$", "yy-MM-dd");// 14.10.18(年.月.日)
		dateRegFormat.put("^\\d{1,2}\\D+\\d{1,2}$", "yyyy-dd-MM");// 30.12(日.月)
																	// 拼接当前年份
		dateRegFormat.put("^\\d{1,2}\\D+\\d{1,2}\\D+\\d{4}$", "dd-MM-yyyy");// 12.21.2013(日.月.年)

		String curDate = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
		SimpleDateFormat formatter1 = new SimpleDateFormat(format);
		SimpleDateFormat formatter2;
		String dateReplace;
		String strSuccess = "";
		try {
			for (String key : dateRegFormat.keySet()) {
				if (Pattern.compile(key).matcher(dateStr).matches()) {
					formatter2 = new SimpleDateFormat(dateRegFormat.get(key));
					if (key.equals("^\\d{2}\\s*:\\s*\\d{2}\\s*:\\s*\\d{2}$") || key.equals("^\\d{2}\\s*:\\s*\\d{2}$")) {// 13:05:34
																														// 或
																														// 13:05
																														// 拼接当前日期
						dateStr = curDate + "-" + dateStr;
					} else if (key.equals("^\\d{1,2}\\D+\\d{1,2}$")) {// 21.1
																		// (日.月)
																		// 拼接当前年份
						dateStr = curDate.substring(0, 4) + "-" + dateStr;
					}
					dateReplace = dateStr.replaceAll("\\D+", "-");
					strSuccess = formatter1.format(formatter2.parse(dateReplace));
					break;
				}
			}
			if (strSuccess != null && "".equals(strSuccess)) {
				strSuccess = dateStr;
			}
		} catch (Exception e) {
			throw new Exception("日期格式无效");
		} finally {
			return strSuccess;
		}
	}
	
	/**
	 * 获取近三年的季度，共12个季度，包括本季度
	 * @return
	 */
	public static List<String> getQuarterList(){
		List<String>quarterList=new ArrayList<String>();
		Date date = new Date();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");
	    String dates = sdf.format(date);
	    int year=Integer.parseInt(dates.substring(0,4));
	    int month=Integer.parseInt(dates.substring(5));
	    int quarter=0;
	    if(month>=1&&month<=3){
	    	quarter=1;
	    }else if(month>=4&&month<=6){
	    	quarter=2;
	    }else if(month>=7&&month<=9){
	    	quarter=3;
	    }else if(month>=10&&month<=12){
	    	quarter=4;
	    }
		String yq="";
		for (int i = 0; i <12; i++) {
			if(quarter<1){
				quarter=4;
				year=year-1;
			}
			yq=String.valueOf(year)+"年第"+String.valueOf(quarter)+"季度";
			quarterList.add(yq);
			quarter--;
		}
		return quarterList;
	}

	/**
	 * 获取相差指定天数的日期
	 * @param date
	 * @param addDay
	 * @return
	 */
	public static String getRangeDate(Date date, int addDay, String format){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, addDay);
		return DateUtils.format(cal.getTime(), StringUtils.isEmpty(format) ? "yyy-MM-dd" : format);
	}
	
	/**
	 * 获取相差指定小时的日期
	 */
	public static String getRangeDate1(Date date, int addHours, String format){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR_OF_DAY, addHours);
		return DateUtils.format(cal.getTime(), StringUtils.isEmpty(format) ? "yyy-MM-dd" : format);
	}
	
	//将时间转换为时间戳
    public static long dateToStamp(String s, String format) throws Exception {
        String res;//设置时间格式，将该时间格式的时间转换为时间戳
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(StringUtils.isNotEmpty(format) ? format : "yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long time = date.getTime();
        return time;
    }
    
    //将时间转换为Date
    public static Date dateToDate(String s, String format) throws Exception {
        String res;//设置时间格式，将该时间格式的时间转换为时间戳
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(StringUtils.isNotEmpty(format) ? format : "yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        return date;
    }

    public static Date parse(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date parse = sdf.parse(date);
			return parse;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Date();
	}

	public static String formartTime(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}
}
