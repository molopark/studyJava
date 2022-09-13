package com.molo.service.web.service;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base32;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.molo.common.utils.StringHelper;
import com.molo.service.web.dao.LoginDao;
import com.molo.service.web.dto.LoginInfoDto;

@Service
public class LoginService {
    private final static Logger LOGGER = LogManager.getLogger(LoginService.class);

	@Autowired LoginDao loginDao;

    public LoginInfoDto selectLoginInfo(LoginInfoDto loginReqDto, String remoteIp) {

    	LoginInfoDto memberInfo = loginDao.selectLoginInfo(loginReqDto);

        if(memberInfo == null || "".equals(StringHelper.nvl(memberInfo.getMemberId()))) {
        	LOGGER.info("memberInfo is null");
        	memberInfo = new LoginInfoDto();
        	memberInfo.setRt("0005");
        	memberInfo.setRtMsg("입력한 정보가 일치하지 않습니다.");
    		  this.failLogin(memberInfo);
          return memberInfo;
        }

        boolean chkLoginIp = this.chkLoginIp(memberInfo, remoteIp);
        if(!chkLoginIp) {
        	LOGGER.info("chkLoginIp false");
        	memberInfo.setRt("0003");
        	memberInfo.setRtMsg("입력한 정보가 일치하지 않습니다.");
    		  this.failLogin(memberInfo);
        	return memberInfo;
        }

        boolean chkLoginDateLimit = this.chkLoginDateLimit(memberInfo);
        if(!chkLoginDateLimit) {
        	LOGGER.info("chkLoginDateLimit false");
        	memberInfo.setRt("0004");
        	memberInfo.setRtMsg("비밀번호 변경 기간이 만료되었습니다.\n관리자에게 문의하세요.");
    		  this.failLogin(memberInfo);
        	return memberInfo;
        }

        memberInfo.setRt("SUCCESS");
    		memberInfo.setPassKey(null);
    		memberInfo.setRtMsg("");

        return memberInfo;
    }

    public LoginInfoDto selectLoginInfo(LoginInfoDto loginReqDto) {

    	LoginInfoDto memberInfo = loginDao.selectLoginInfo(loginReqDto);

        if(memberInfo == null || "".equals(StringHelper.nvl(memberInfo.getMemberId()))) {
        	LOGGER.info("memberInfo is null");
        	memberInfo = new LoginInfoDto();
        	memberInfo.setRt("0005");
        	memberInfo.setRtMsg("입력한 정보가 일치하지 않습니다.");
    		  this.failLogin(memberInfo);
          return memberInfo;
        }

        return memberInfo;
    }

    public boolean chkLoginIp(LoginInfoDto loginReqDto, String remoteIp) {
    	String dataIp = StringHelper.nvl(loginReqDto.getIp());

        LOGGER.info("remoteIp : {}", remoteIp);
        LOGGER.info("loginReqDto ip : {}", dataIp);
        if(dataIp.contains(remoteIp)) {
            return true;
        }
        return false;
    }

    public boolean chkLoginDateLimit(LoginInfoDto loginReqDto) {
        String createDate = loginReqDto.getCreateDate();
        String nowDate = loginReqDto.getNowDate();
        LOGGER.info("nowDate : {}", nowDate);
        LOGGER.info("createDate : {}", createDate);

        int diffDate = (int) StringHelper.getDiffDate(createDate, nowDate);
        LOGGER.info("diffDate : {}", diffDate);
        // 기준일자 (생성일 + 90일)
        if(diffDate <= 90) {
            return true;
        }
        return false;
    }

    public boolean chkLoginLimit(LoginInfoDto loginReqDto) {
        int passFailCnt = loginReqDto.getPassFailCnt();
        String modifyDate = loginReqDto.getModifyDate();
        String nowDate = loginReqDto.getNowDate();

        if(passFailCnt >= 5 && modifyDate.equals(nowDate)) {
            return true;
        }
        return false;
    }

    public boolean failLogin(LoginInfoDto loginReqDto) {
        int passFailCnt = loginReqDto.getPassFailCnt();
        if(passFailCnt >= 5) {
            passFailCnt = 5;
        } else {
            passFailCnt = passFailCnt + 1;
        }
        loginReqDto.setPassFailCnt(passFailCnt);
        int updateCnt = loginDao.updateLoginResult(loginReqDto);
        if(updateCnt > 0) {
            return true;
        }
        return false;
    }

    public boolean successLogin(LoginInfoDto loginReqDto) {
    	loginReqDto.setPassFailCnt(0);
        int updateCnt = loginDao.updateLoginResult(loginReqDto);
        if(updateCnt > 0) {
            return true;
        }
        return false;
    }

    private static boolean check_code(String secret, long code, long t) throws NoSuchAlgorithmException, InvalidKeyException {
        Base32 codec = new Base32();
        byte[] decodedKey = codec.decode(secret);

        // Window is used to check codes generated in the near past.
        // You can use this value to tune how far you're willing to go.
        int window = 3;
        for (int i = -window; i <= window; ++i) {
            long hash = verify_code(decodedKey, t + i);
            LOGGER.info("code : " + code);
            LOGGER.info("hash : " + hash);

            if (hash == code) {
                return true;
            }
        }

        // The validation code is invalid.
        return false;
    }

    private static int verify_code(byte[] key, long t)
            throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] data = new byte[8];
        long value = t;
        for (int i = 8; i-- > 0; value >>>= 8) {
            data[i] = (byte) value;
        }

        SecretKeySpec signKey = new SecretKeySpec(key, "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(signKey);
        byte[] hash = mac.doFinal(data);

        int offset = hash[20 - 1] & 0xF;

        // We're using a long because Java hasn't got unsigned int.
        long truncatedHash = 0;
        for (int i = 0; i < 4; ++i) {
            truncatedHash <<= 8;
            // We are dealing with signed bytes:
            // we just keep the first byte.
            truncatedHash |= (hash[offset + i] & 0xFF);
        }

        truncatedHash &= 0x7FFFFFFF;
        truncatedHash %= 1000000;

        return (int) truncatedHash;
    }

}
