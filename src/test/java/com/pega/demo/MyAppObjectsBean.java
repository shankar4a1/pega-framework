package com.pega.demo;

import com.pega.config.ObjectBean;
import com.pega.demo.workobjects.Forecast;
import com.pega.demo.workobjects.Space_PegaSocialGroup;
import com.pega.demo.workobjects.Spaces;
import com.pega.util.DataUtil;

import java.util.LinkedHashMap;
import java.util.Map;


public class MyAppObjectsBean {

	private static Forecast forecast;
	private static Spaces spaces;
	private static Space_PegaSocialGroup space_PegaSocialGroup;
	private static Map<String, Object> objectsMap = new LinkedHashMap<String, Object>();
	private static Map<String, String> objectNames = new LinkedHashMap<String, String>();

	public static Forecast getForecast() {
		return forecast;
	}

	public static void setForecast(Forecast forecast) {
		MyAppObjectsBean.forecast= forecast;
	}

	public static void setSpaces(Spaces spaces) {
		MyAppObjectsBean.spaces= spaces;
	}

	public static Spaces getSpaces() {
		return spaces;
	}
	
	public static Space_PegaSocialGroup getSpace_PegaSocialGroup() {
		return space_PegaSocialGroup;
	}

	public static void setSpace_PegaSocialGroup(Space_PegaSocialGroup space_PegaSocialGroup) {
		MyAppObjectsBean.space_PegaSocialGroup = space_PegaSocialGroup;
	}

	public static String putTimeStampedValue(String name) {
		String existingValue = objectNames.get(name);
		if (existingValue == null) {
			String value = DataUtil.getRandomNumberString(name);
			objectNames.put(name, value);
			return value;
		} else {
			return existingValue;
		}
	}

	public static String getTimestampedValue(String name) {
		String value = objectNames.get(name);
		if (value != null) {
			return value;
		} else {
			return name;
		}
	}

}
