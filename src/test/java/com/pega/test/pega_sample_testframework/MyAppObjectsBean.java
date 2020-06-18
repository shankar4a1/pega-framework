package com.pega.test.pega_sample_testframework;

import com.pega.config.ObjectBean;
import com.pega.crm.workobjects.Forecast;
import com.pega.crm.workobjects.Space_PegaSocialGroup;
import com.pega.crm.workobjects.Spaces;

public class MyAppObjectsBean extends ObjectBean{

	private static Forecast forecast;
	private static Spaces spaces;
	private static Space_PegaSocialGroup space_PegaSocialGroup;

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

}
