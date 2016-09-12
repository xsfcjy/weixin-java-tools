package me.chanjar.weixin.common.bean;

import java.io.Serializable;

public class WxBaseBean  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String appId;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}
	
	
}
