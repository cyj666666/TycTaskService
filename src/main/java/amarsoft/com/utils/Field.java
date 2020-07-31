package amarsoft.com.utils;

import java.sql.Types;

/**
 * 数据库查询结果结果记录字段抽象类
 * @author yangdengwu
 */
public class Field {
	
	private Object value;
	
	/**
	 * 数据类型
	 */
	private Integer dataType;
	
	/**
	 * 列的字段�?
	 */
	private String culomnName;

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Integer getIntValue() {
		if (value == null) {
			return null;
		}
		if (isNumberType(dataType) || isNumberValue(value)) {
			Number number = (Number) value;
			return number.intValue();
		} 
		
		return Integer.parseInt(value.toString());
	}

	public Long getLongValue() {
		if (value == null) {
			return null;
		}
		if (isNumberType(dataType) || isNumberValue(value)) {
			Number number = (Number) value;
			return number.longValue();
		} 
		
		return Long.parseLong(value.toString());
	}

	public Double getDoubleValue() {
		if (value == null) {
			return null;
		}
		if (isNumberType(dataType) || isNumberValue(value)) {
			Number number = (Number) value;
			return number.doubleValue();
		} 
		
		return Double.parseDouble(value.toString());
	}

	public String getStringValue() {
		return value == null ? null : value.toString();
	}

	public Integer getDataType() {
		return dataType;
	}

	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}

	public String getCulomnName() {
		return culomnName;
	}

	public void setCulomnName(String culomnName) {
		this.culomnName = culomnName;
	}
	
	public Field clone() {
		Field field = new Field();
		field.setCulomnName(this.culomnName);
		if (this.dataType != null) {
			field.setDataType(this.dataType);
		}
		field.setValue(this.value);
		
		return field;
	}
	
	private boolean isNumberType(Integer dataType) {
		if (dataType == null) {
			return false;
		}
		if (dataType == Types.TINYINT ||
				dataType == Types.SMALLINT ||
				dataType == Types.INTEGER ||
				dataType == Types.BIGINT ||
				dataType == Types.FLOAT ||
				dataType == Types.DOUBLE) {
			return true;
		}
		
		return false;
	}
	
	private boolean isNumberValue(Object value) {
		return value instanceof Number ? true : false;
	}

}
