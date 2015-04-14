package com.g200001.dutyapp.domain;

import java.io.Serializable;

public class UserDeviceLoginInfo implements Serializable {
	private String Name;
	private String Password;
	private Integer DeviceType; // 0 android 1 IOS
	private String DeviceID;

	public void setName(String name){
		this.Name = name;
	}
	public String getName(){
		return Name;
	}
	
	public void setPassword(String password){
		this.Password = password;
	}
	public String getPassword(){
		return Password;
	}
	
	public void setDeviceType(Integer deviceType){
		this.DeviceType=deviceType;
	}
	public Integer getDeviceType(){
		return DeviceType;
	}
	public void setDeviceID(String deviceID){
		this.DeviceID = deviceID;
	}
	public String getDeviceID(){
		return DeviceID;
	}
	@Override
	public String toString(){
		return "UserDeviceLoginInfo{"+
				"Name='"+Name+"'"+
				",Password='"+Password+"'"+
				",DeviceType="+DeviceType+
				",DeviceID='"+DeviceID+"'}";
				}
				
	}

