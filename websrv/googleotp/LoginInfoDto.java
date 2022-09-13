package com.molo.service.web.dto;

public class LoginInfoDto {

	public String memberId;
	public String passKey;
  public int passFailCnt;
	public String memberName;
	public String memberPhonenum;
	public String memberInfo;
	public String ip;
	public String otpValue;
	public String modifyDate;
	public String createDate;
	public String nowDate;

	public String otpCode;
	public String otpCodeKey;

	public String rt;
	public String rtMsg;

	/**
	 * @return the memberId
	 */
	public String getMemberId() {
		return memberId;
	}
	/**
	 * @param memberId the memberId to set
	 */
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	/**
	 * @return the memberName
	 */
	public String getMemberName() {
		return memberName;
	}
	/**
	 * @param memberName the memberName to set
	 */
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	/**
	 * @return the memberPhonenum
	 */
	public String getMemberPhonenum() {
		return memberPhonenum;
	}
	/**
	 * @param memberPhonenum the memberPhonenum to set
	 */
	public void setMemberPhonenum(String memberPhonenum) {
		this.memberPhonenum = memberPhonenum;
	}
	/**
	 * @return the memberInfo
	 */
	public String getMemberInfo() {
		return memberInfo;
	}
	/**
	 * @param memberInfo the memberInfo to set
	 */
	public void setMemberInfo(String memberInfo) {
		this.memberInfo = memberInfo;
	}
	/**
	 * @return the modifyDate
	 */
	public String getModifyDate() {
		return modifyDate;
	}
	/**
	 * @param modifyDate the modifyDate to set
	 */
	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}
	/**
	 * @return the createDate
	 */
	public String getCreateDate() {
		return createDate;
	}
	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	/**
	 * @return the passKey
	 */
	public String getPassKey() {
		return passKey;
	}
	/**
	 * @param passKey the passKey to set
	 */
	public void setPassKey(String passKey) {
		this.passKey = passKey;
	}
	/**
	 * @return the passFailCnt
	 */
	public int getPassFailCnt() {
		return passFailCnt;
	}
	/**
	 * @param passFailCnt the passFailCnt to set
	 */
	public void setPassFailCnt(int passFailCnt) {
		this.passFailCnt = passFailCnt;
	}
	/**
	 * @return the rt
	 */
	public String getRt() {
		return rt;
	}
	/**
	 * @param rt the rt to set
	 */
	public void setRt(String rt) {
		this.rt = rt;
	}
	/**
	 * @return the rtMsg
	 */
	public String getRtMsg() {
		return rtMsg;
	}
	/**
	 * @param rtMsg the rtMsg to set
	 */
	public void setRtMsg(String rtMsg) {
		this.rtMsg = rtMsg;
	}
	/**
	 * @return the nowDate
	 */
	public String getNowDate() {
		return nowDate;
	}
	/**
	 * @param nowDate the nowDate to set
	 */
	public void setNowDate(String nowDate) {
		this.nowDate = nowDate;
	}
	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}
	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}
	/**
	 * @return the otpValue
	 */
	public String getOtpValue() {
		return otpValue;
	}
	/**
	 * @param otpValue the otpValue to set
	 */
	public void setOtpValue(String otpValue) {
		this.otpValue = otpValue;
	}
	/**
	 * @return the otpCode
	 */
	public String getOtpCode() {
		return otpCode;
	}
	/**
	 * @param otpCode the otpCode to set
	 */
	public void setOtpCode(String otpCode) {
		this.otpCode = otpCode;
	}
	/**
	 * @return the otpCodeKey
	 */
	public String getOtpCodeKey() {
		return otpCodeKey;
	}
	/**
	 * @param otpCodeKey the otpCodeKey to set
	 */
	public void setOtpCodeKey(String otpCodeKey) {
		this.otpCodeKey = otpCodeKey;
	}

}
