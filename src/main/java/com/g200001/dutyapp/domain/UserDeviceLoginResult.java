package com.g200001.dutyapp.domain;
import com.g200001.dutyapp.security.xauth.Token;



public class UserDeviceLoginResult {
	private Token Token;
	private String UserID;
	
	public void setToken(Token token){
		this.Token = token;
	}
	
	public Token getToken(){
		return Token;
	}
	
	public void setUserID(String userID){
		this.UserID=userID;
	}
	public String getUserID(){
		return UserID;
	}
}
