package com.bdqn.syht.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

/**
 * �����װ�Ĺ�����
 * @author Administrator
 *
 */
public class RequestUtils {
	// ����ֵ����ΪMap<String, Object>
		public static Map<String, Object> getParameterMap(HttpServletRequest request) {
			Map<String, String[]> properties = request.getParameterMap();// ����������װ��Map<String,
																			// String[]>��
			Map<String, Object> returnMap = new HashMap<String, Object>();
			Iterator<Entry<String, String[]>> iter = properties.entrySet()
					.iterator();
			String name = "";
			String value = "";
			while (iter.hasNext()) {
				Entry<String, String[]> entry = iter.next();
				name = entry.getKey();
				Object valueObj = entry.getValue();
				if (null == valueObj) {
					value = "";
				} else if (valueObj instanceof String[]) {
					String[] values = (String[]) valueObj;
					for (int i = 0; i < values.length; i++) { // ��������������ж����ͬ���
						value = values[i] + ",";
					}
					value = value.substring(0, value.length() - 1);
				} else {
					value = valueObj.toString();// ����������������������Ψһ
				}
				returnMap.put(name, value);
			}
			return returnMap;
		}

		// ����ֵ����ΪMap<String, String>
		public static Map<String, String> getParameterStringMap(
				HttpServletRequest request) {
			Map<String, String[]> properties = request.getParameterMap();// ����������װ��Map<String,
																			// String[]>��
			Map<String, String> returnMap = new HashMap<String, String>();
			String name = "";
			String value = "";
			for (Map.Entry<String, String[]> entry : properties.entrySet()) {
				name = entry.getKey();
				String[] values = entry.getValue();
				if (null == values) {
					value = "";
				} else if (values.length > 1) {
					for (int i = 0; i < values.length; i++) { // ��������������ж����ͬ���
						value = values[i] + ",";
					}
					value = value.substring(0, value.length() - 1);
				} else {
					value = values[0];// ����������������������Ψһ
				}
				returnMap.put(name, value);

			}
			return returnMap;
		}
		
		/**
		 * �����ҳ����
		 * @param map
		 * @return
		 */
		public static Map<String,Object> getPageAndRowsMap(Map<String, Object> map){
			int page = Integer.parseInt(map.get("page").toString());
			int rows = Integer.parseInt(map.get("rows").toString());
			page = (page-1)*rows;
			map.put("page", String.valueOf(page));
			return map;
		}
}
