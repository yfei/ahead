package cn.dcube.ahead.elastic.util;

import java.lang.reflect.Field;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonUtils {
	
	public static Object getObjectId(Object obj) {
		try {
			// 获取数据对象的id属性值
			Field idField = obj.getClass().getDeclaredField("id");
			idField.setAccessible(true);
			Object idVal = idField.get(obj);
			return idVal;
		} catch (Exception e) {
			log.debug("{}类无ID字段", obj.getClass().getSimpleName());
		}
		return null;
	}

	/**
	 * 计算总页码
	 * 
	 * @param totalCount
	 * @param pageSize
	 * @return
	 */
	public static int getTotalPages(int totalCount, int pageSize) {
		int totalPage = Math.floorDiv(totalCount, pageSize);
		if (totalCount % pageSize != 0) {
			totalPage += 1;
		}
		return totalPage;
	}

}
