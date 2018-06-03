package com.rw.common.utils.model;

import com.rw.common.utils.annotation.AttributeAnnotation;

public class ClassUtilsModel {
	
	/**
	 * 身份证 - 通过实名认证|实名认证失败
	 */
	@AttributeAnnotation(name="N201",desc="与第三方无关--身份证号码",reportDesc="{'0':'实名认证成功','1':'实名验证失败','2':''}")
	public String N201= "";
	
	/**
	 * 身份证 - 二代身份证标识
	 */
	@AttributeAnnotation(name="N202",desc="二代身份证标识")
	public String N202= "";
	
	
	/**
	 * 身份证 - 二代身份证标识
	 */
	@AttributeAnnotation(name="N203",desc="在线时长")
	public Integer N203= 0;


	public String getN201() {
		return N201;
	}


	public void setN201(String n201) {
		N201 = n201;
	}


	public String getN202() {
		return N202;
	}


	public void setN202(String n202) {
		N202 = n202;
	}


	public Integer getN203() {
		return N203;
	}


	public void setN203(Integer n203) {
		N203 = n203;
	}


	@Override
	public String toString() {
		return "ClassUtilsModel [N201=" + N201 + ", N202=" + N202 + ", N203=" + N203 + "]";
	}
	
	
	
	

}
