
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;

/**
 * @author davidfantasy 用于存储两个同一类型的jpa entity bean实例间的差异 主要用于修改日志记录
 */
public class Difference {

	// 字段类型
	private Class<?> fieldType;

	// 字段名称
	private String fieldName;

	// 字段所对应的数据库字段名称
	private String fieldColumnName;

	// 修改前值
	private Object beforeValue;

	// 修改后值
	private Object afterValue;

	// 该字段所属的表名称
	private String tableName;

	public Class<?> getFieldType() {
		return fieldType;
	}

	public void setFieldType(Class<?> fieldType) {
		this.fieldType = fieldType;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldColumnName() {
		return fieldColumnName;
	}

	public void setFieldColumnName(String fieldColumnName) {
		this.fieldColumnName = fieldColumnName;
	}

	public Object getBeforeValue() {
		return beforeValue;
	}

	public void setBeforeValue(Object beforeValue) {
		this.beforeValue = beforeValue;
	}

	public Object getAfterValue() {
		return afterValue;
	}

	public void setAfterValue(Object afterValue) {
		this.afterValue = afterValue;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableName() {
		return tableName;
	}

	public String getTranslatedBeforeValue() {
		return translateValue(beforeValue);
	}

	public String getTranslatedAfterValue() {
		return translateValue(afterValue);
	}

	private String translateValue(Object value) {
		if (value == null)
			return "未填写";
		if (fieldType.isAssignableFrom(Integer.class)) {
			return String.valueOf(value);
		} else if (fieldType.isAssignableFrom(Boolean.class)) {
			return ((Boolean) value) ? "是" : "否";
		} else if (fieldType.isAssignableFrom(Long.class)) {
			return String.valueOf(value);
		} else if (fieldType.isAssignableFrom(java.util.Date.class)
				|| fieldType.isAssignableFrom(java.sql.Date.class)) {
			return DateUtil.getDateString((java.util.Date) value,
					"yyyy-MM-dd HH:mm:ss");
		} else if (fieldType.isAssignableFrom(Timestamp.class)) {
			return ((Timestamp) value).toString();
		} else if (StringUtils.isBlank(value.toString())) {
			return "空值";
		} else {
			return value.toString();
		}
	}
}
