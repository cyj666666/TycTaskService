package amarsoft.com.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 数据库查询结果记录抽象类
 * @author yangdengwu
 * @since 2018-09-12
 */
public class Record {

	private Field[] fields;
	
	private Map<String, Integer> fieldName2IndexMap;
	
	public Record(Field[] fields, Map<String, Integer> feildName2IndexMap) {
		this.fields = fields;
		this.fieldName2IndexMap = feildName2IndexMap;
	}
	
	public Field[] getFields() {
		return this.fields;
	}
	
	public Set<String> getFieldsName() {
		return fieldName2IndexMap.keySet();
	}
	
	public Integer getIntValue(int index) {
		checkIndex(index);
		return fields[index].getIntValue();
	}
	
	public Integer getIntValueByName(String fieldName) {
		int index = getFieldIndexByName(fieldName);
		return fields[index].getIntValue();
	}
		
	public Long getLongValue(int index) {
		checkIndex(index);
		return fields[index].getLongValue();
	}
	
	public Long getLongValueByName(String fieldName) {
		int index = getFieldIndexByName(fieldName);
		return fields[index].getLongValue();
	}
	
	public Double getDoubeValue(int index) {
		checkIndex(index);
		return fields[index].getDoubleValue();
	}
	
	public Double getDoubeValueByName(String fieldName) {
		int index = getFieldIndexByName(fieldName);
		return fields[index].getDoubleValue();
	}
	
	public String getStringValue(int index) {
		checkIndex(index);
		return fields[index].getStringValue();
	}
	
	public String getStringValueByName(String fieldName) {
		int index = getFieldIndexByName(fieldName);
		return fields[index].getStringValue();
	}
	
	public Object getObjectValue(int index) {
		checkIndex(index);
		return fields[index].getValue();
	}
	
	public Object getObjectValueByName(String fieldName) {
		int index = getFieldIndexByName(fieldName);
		return fields[index].getValue();
	}
	
	private int getFieldIndexByName(String fieldName) {
		if (fieldName == null || fieldName.trim().isEmpty()) {
			throw new RuntimeException("字段名不能为?");
		}
		Integer index = fieldName2IndexMap.get(fieldName.toUpperCase());
		if (index == null) {
			throw new RuntimeException("记录中不存在字段名["+fieldName+"]");
		}
		
		return index.intValue();
	}
	
	private void checkIndex(int index) {
		if (index < 1 || index >= fields.length) {
			new IndexOutOfBoundsException("下标"+index+"越界");
		}
	}
	
	public boolean contain(String fieldName) {
		if (StringUtils.isBlank(fieldName)) {
			return false;
		}
		return fieldName2IndexMap.containsKey(fieldName.toUpperCase()) ? true : false;
	}
	
	public void addField(String fieldName, Object value) {
		addField(fieldName,value,null);
	}
	
	public void addField(String fieldName, Object value, Integer dataType) {
		if (StringUtils.isBlank(fieldName) || (fieldName2IndexMap != null && fieldName2IndexMap.containsKey(fieldName.toUpperCase()))) {
			return;
		}
		
		fieldName = fieldName.toUpperCase();
		Field[] curFields = null;
		Field curField = new Field();
		curField.setCulomnName(fieldName);
		curField.setValue(value);
		curField.setDataType(dataType);
		
		if (fields == null || fields.length == 0 ) {
			curFields = new Field[] {curField};
			this.fields = curFields;
			this.fieldName2IndexMap = new HashMap<>();
			this.fieldName2IndexMap.put(fieldName, 0);
		} else {
			curFields = new Field[this.fields.length + 1];
			for (int i = 0; i < fields.length; i++) {
				curFields[i] = fields[i].clone();
			}
			curFields[fields.length] = curField;
			this.fields = curFields;
			this.fieldName2IndexMap.put(fieldName, fields.length - 1);
		}
	}
 	
}
