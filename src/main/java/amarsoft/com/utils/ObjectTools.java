package amarsoft.com.utils;

import amarsoft.com.bean.FileLogBean;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class ObjectTools {
	
	static org.slf4j.Logger logger = LoggerFactory.getLogger(ObjectTools.class);
	
	public static final String STRING = "String";
	public static final String DOUBLE = "double";
	public static final String INT = "int";
	
	
//	public static List<Object> setRecordToBean(List<Record> records,Class<?> obj) {
//		List<Object> listcron = new ArrayList<Object>();
//		try {
//		for(Record record:records) {
//			if(record != null) {
//				amarsoft.com.utils.Field[] fields = record.getFields();
//				Object sbn = obj.newInstance();
//				for(int i=0;i<fields.length;i++) {
//					if(fields[i].getValue() != null && StringUtils.isNotBlank(fields[i].getValue().toString()))
//						try {
//							ObjectTools.setObjectThower(sbn, fields[i].getCulomnName(), fields[i].getValue());
//						}catch(Exception e){
//							logger.info(obj.toString()+"找不到字段"+fields[i].getCulomnName());
//						}
//				}
//				listcron.add(sbn);
//			}
//		}
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//		return listcron;
//	}
	
	/**
	 */
	public static List<Object> setObjectBean(Class<?> obj, List<Record> records) {
		List<Object> list = new ArrayList<Object>();
		if (obj != null) {
			for (Record record : records) {
				if (record != null) {
					Object objBean;
					try {
						objBean = obj.newInstance();
						amarsoft.com.utils.Field[] fields = record.getFields();
						for (int i = 0; i < fields.length; i++) {
							String name = fields[i].getCulomnName();
							String value = fields[i].getStringValue();
							setObjectAttr(objBean,name,value);						
						}
						list.add(objBean);
					} catch (InstantiationException | IllegalAccessException e1) {
						e1.printStackTrace();
					}

				}
			}
		}
		return list;
	}

	/**
	 * @param obj
	 * @param field
	 * @param value
	 */
	public static void setObjectAttr(Object obj, String field, Object value){
		if(obj!=null){
			Method[] method = obj.getClass().getDeclaredMethods();
			for(Method md:method){
				String methodName = md.getName();
				if(("set"+field).equalsIgnoreCase(methodName)){
					try {
						Class<?> paramType = md.getParameterTypes()[0];
						if(paramType == int.class) {
							value = Integer.parseInt(value.toString());
						}
						md.invoke(obj, value);
						break;
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	
	/**
	 * @param obj
	 * @param field
	 * @param value
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static void setObjectThower(Object obj, String field, Object value) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if(obj!=null){
			Method[] method = obj.getClass().getDeclaredMethods();
			for(Method md:method){
				String methodName = md.getName();
				if(("set"+field).equalsIgnoreCase(methodName)){
						Class<?> paramType = md.getParameterTypes()[0];
						if(paramType == int.class) {
							value = Integer.parseInt(value.toString());
						}else if(paramType == double.class) {
							value = Double.parseDouble(value.toString());
						}
						md.invoke(obj, value);
						break;
				}
			}
		}
	}
	/**
	 * ֵ
	 */
	public static Object getObjectAttr(Object obj, String field){
		Object o = null;
		if(obj!=null){
			Method[] method = obj.getClass().getDeclaredMethods();
			for(Method md:method){
				String methodName = md.getName();
				if(("get"+field).equalsIgnoreCase(methodName)){
					try {
						o = md.invoke(obj);
						break;
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return o ;
	}
	
	/**
	 * ���ض���obj������������ƴ�ӵ�insert
	 * @param obj
	 * @return
	 */
	public static String getInsertSqlByObj(Object obj, String tableName){
		String preSql = "";
		String aftSql = "";
		if(obj!=null){
			Field[] fields = obj.getClass().getDeclaredFields();
			int cn = 0;
			for(Field f:fields){
				if(cn==0){
					preSql+="insert into "+tableName+" ("+f.getName();
					aftSql+=" values (?";
				}else{
					if(cn==(fields.length-1)){
						preSql+=","+f.getName()+")";
						aftSql+=",?)";
					}
					else{
						preSql+=","+f.getName();
						aftSql+=",?";
					}
				}
				cn++;
			}
		}
		return preSql +aftSql;
	}
	
	/**
	 * ���ض���obj������������ƴ�ӵ�insert
	 * @param obj
	 * @return
	 */
	public static String getInsertSqlByObjSt(Object obj, String tableName){
		String preSql = "";
		String aftSql = "";
		if(obj!=null){
			Field[] fields = obj.getClass().getDeclaredFields();
			int cn = 0;
			for(Field f:fields){
				String typeName = f.getType().getSimpleName();
				Object attVal = getObjAttVal(obj,f.getName());
				if(cn==0){
					preSql+="insert into "+tableName+" ("+f.getName();
					if(typeName.equalsIgnoreCase(ObjectTools.DOUBLE)||typeName.equalsIgnoreCase(ObjectTools.INT)){
						aftSql+=" values ("+attVal;
					}else{
						if(attVal==null){
							aftSql+=" values (null";
						}else{
							aftSql+=" values ('"+attVal+"'";
						}
						
					}
					
				}else{
					if(cn==(fields.length-1)){
						preSql+=","+f.getName()+")";
						if(typeName.equalsIgnoreCase(ObjectTools.DOUBLE)||typeName.equalsIgnoreCase(ObjectTools.INT)){
							aftSql+=","+attVal+")";
						}else{
							if(attVal==null){
								aftSql+=",null)";
							}else{
								aftSql+=",'"+attVal+"')";
							}
							
						}
						
					}
					else{
						preSql+=","+f.getName();
						if(typeName.equalsIgnoreCase(ObjectTools.DOUBLE)||typeName.equalsIgnoreCase(ObjectTools.INT)){
							aftSql+=","+attVal;
						}else{
							if(attVal==null){
								aftSql+=",null";
							}else{
								aftSql+=",'"+attVal+"'";
							}
							
						}
						
					}
				}
				cn++;
			}
		}
		return preSql +aftSql;
	}
	/**
	 * ͨ�������ȡobj����ֵ
	 * @param obj
	 * @param field
	 * @return
	 */
	public static Object getObjAttVal(Object obj, String field){
		Object oj = null;
		if(obj!=null){
			Method[] methods = obj.getClass().getDeclaredMethods();
			for(Method m:methods){
				if(("get"+field).equalsIgnoreCase(m.getName())){
					try {
						oj = m.invoke(obj);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
					break;
				}
			}
		}
		return oj;
	}
	public static void main(String[] args) {
		/*IndRiskAlarm ira = new IndRiskAlarm();
		Field[] fields = ira.getClass().getDeclaredFields();
		ObjectTools.setObjectAttr(ira, "customerId", "11111");*/
	
		FileLogBean FileLogBean = new FileLogBean();
		FileLogBean.setBankBatchSerialno("aaa");
		System.out.println(ObjectTools.foreachBean(FileLogBean));
//		String sDate = ObjectTools.getNowDate("yyyy/MM/dd HH:mm:ss");
//		System.out.println(sDate);
//		DateFormat dformat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//		DateFormat dformat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		Date sDate2 = null;
//		try {
//			sDate2 = dformat.parse(sDate);
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println(dformat2.format(sDate2));
	}
	/**
	 * @return
	 */
	public static String getDateChange(String oldFmt, String newFmt, String oldDate){
		DateFormat dformat  = new SimpleDateFormat(oldFmt);
		DateFormat dformat2 = new SimpleDateFormat(newFmt);
		String newDate = "";
		try {
			newDate = dformat2.format(dformat.parse(oldDate));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newDate;
	}
	/**
	 * ���ص�ǰʱ��
	 * @param format
	 * @return
	 */
	public static String getNowDate(String format){
		DateFormat dformat = new SimpleDateFormat(format);
		return dformat.format(new Date());
	}
	/**
	 * @param before
	 * @param format
	 * @return
	 */
	public static String getDate(int type, int before, String format){
		Calendar clendar = Calendar.getInstance();
		clendar.add(type, before);
		DateFormat dformat = new SimpleDateFormat(format);
		return dformat.format(clendar.getTime());
	}
	
	
	public static void setObjectAttrVal(Object edp){
		Field[] field = edp.getClass().getDeclaredFields();
		if(field==null) return;
		for(Field ff:field){
			String fieldName = ff.getName();
			if("SERIALNO".equalsIgnoreCase(ff.getName())
					||"CUSTOMERID".equalsIgnoreCase(ff.getName())
					||"CUSTOMERNAME".equalsIgnoreCase(ff.getName())
					||"COUNTYEAR".equalsIgnoreCase(ff.getName())
					||"COUNTMONTH".equalsIgnoreCase(ff.getName())
					||"INPUTDATE".equalsIgnoreCase(ff.getName())
					||"UPDATEDATE".equalsIgnoreCase(ff.getName())						
					||"INPUTTIME".equalsIgnoreCase(ff.getName())
					||"UPDATETIME".equalsIgnoreCase(ff.getName())
					)
			{
				continue;
			}
			String fieldType = ff.getType().getSimpleName();
			if(fieldType.equalsIgnoreCase(ObjectTools.INT)){
				ObjectTools.setObjectAttr(edp,fieldName,0);
			}else if(fieldType.equalsIgnoreCase(ObjectTools.DOUBLE)){
				ObjectTools.setObjectAttr(edp,fieldName,0.0);
			}else {
				ObjectTools.setObjectAttr(edp,fieldName,"0");
			}
		}
	}
	/**
	 * 遍历JAVABEAN
	 * @param obj
	 * @return
	 */
	public static Map<String, String> foreachBean(Object obj) {
		Class<?> cls = obj.getClass();
		Field[] fields = cls.getDeclaredFields();
		Map<String, String> map =  new HashMap<String, String>();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			field.setAccessible(true);
			try {
				map.put(field.getName(), field.get(obj)==null?"":field.get(obj).toString());
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		 }
		return map;
	}
	
}
