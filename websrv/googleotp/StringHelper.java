package com.molo.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * TODO 클래스 설명 작성
 *
 * @author ybpark
 * @since 2016. 1. 15.
 */
@Component
public class StringHelper {
	private static final Logger LOGGER = LoggerFactory.getLogger(StringHelper.class);

	public static boolean isEmpty(String param) {
		return org.apache.commons.lang.StringUtils.isEmpty(param);
	}

	public static boolean isBlank(String param) {
		return org.apache.commons.lang.StringUtils.isBlank(param);
	}

	public static String nvl(String s) {
		if (isBlank(s)) {
			return "";
		}
		return s;
	}

	public static String nvl(String s, String s1) {
		if (isBlank(s)) {
			return s1;
		}
		return s;
	}

	public static String encodeXssTag(String value) {

        if (value == null) {
            return "";
        }
        String cleanString = value;

        cleanString = cleanString.replaceAll("&",  "&amp;" );
        cleanString = cleanString.replaceAll("<",  "&lt;"  );
        cleanString = cleanString.replaceAll(">",  "&gt;"  );
        cleanString = cleanString.replaceAll("\\(","&#40;" );
        cleanString = cleanString.replaceAll("\"", "&quot;");
        cleanString = cleanString.replaceAll("'",  "&#x27;");
        cleanString = cleanString.replaceAll("/",  "&#x2F;");
        cleanString = cleanString.replaceAll("\\)","&#41;" );

        return cleanString;

    }

	// RequestWrapper.java 설정된 값을 원복
	public static String decodeXssTag(String s) {
		String retS = s;
		if (isBlank(retS)) {
			return retS;
		}

		retS = retS.replaceAll("&lt;div", "<div");
		retS = retS.replaceAll("&lt;img", "<img");
		retS = retS.replaceAll("&lt;caption", "<caption");
		retS = retS.replaceAll("&lt;font", "<font");
		retS = retS.replaceAll("&lt;h", "<h");
		retS = retS.replaceAll("&lt;p","<p" );
		retS = retS.replaceAll("&lt;a","<a" );
    retS = retS.replaceAll("&lt;strong","<strong" );
    retS = retS.replaceAll("&lt;span","<span" );
    retS = retS.replaceAll("&lt;br","<br" );
    retS = retS.replaceAll("&lt;table","<table" );
    retS = retS.replaceAll("&lt;tbody","<tbody" );
    retS = retS.replaceAll("&lt;th","<th" );
    retS = retS.replaceAll("&lt;tr","<tr" );
    retS = retS.replaceAll("&lt;td","<td" );
    retS = retS.replaceAll("&lt;&#x2F;","</" );
    retS = retS.replaceAll("&lt;/","</" );
    retS = retS.replaceAll("&lt;!--","<!--" );

		retS = retS.replaceAll("&amp;", "&");
		retS = retS.replaceAll("&gt;", ">");
		retS = retS.replaceAll("&#40;", "\\(");
		retS = retS.replaceAll("&quot;", "\"");
		retS = retS.replaceAll("&#x27;", "'");
		retS = retS.replaceAll("&#x2F;", "/");
		retS = retS.replaceAll("&#41;", "\\)");

		// 삭제문자
		retS = retS.replaceAll("(?i)script","");
		retS = retS.replaceAll("(?i)alert","");
		retS = retS.replaceAll("(?i)confirm","");
		retS = retS.replaceAll("(?i)prompt","");
		retS = retS.replaceAll("(?i)settimeout","");
		retS = retS.replaceAll("(?i)setinterval","");
		retS = retS.replaceAll("(?i)find","");
		retS = retS.replaceAll("(?i)print","");
		retS = retS.replaceAll("(?i)onerror","");
		retS = retS.replaceAll("(?i)onblur","");
		retS = retS.replaceAll("(?i)onfocus","");
		retS = retS.replaceAll("(?i)onload","");
		retS = retS.replaceAll("(?i)onunload","");
		retS = retS.replaceAll("(?i)ondragdrop","");
		retS = retS.replaceAll("(?i)onmove","");
		retS = retS.replaceAll("(?i)onresize","");

		return retS;
	}

	/**
     *
     * @param format
     *          ex> format : yyyyMMddHHmmssSSS
     * @return
     */
    public static String getCurrentTime(String format) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.KOREA);

        return dateFormat.format(calendar.getTime());
    }

    public static String getDateTime(String format, int monthGab) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, monthGab);
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.KOREA);

        return dateFormat.format(calendar.getTime());
    }

    @SuppressWarnings("static-access")
	public static String getBeforeAfterDate(String orgDate, int before) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
        try {
			Date orgDay = dateFormat.parse(orgDate);
			calendar.setTime(orgDay);
		} catch (ParseException e) {
			return "";
		}
        calendar.add(calendar.DATE, before);

        return dateFormat.format(calendar.getTime());
    }

    @SuppressWarnings("static-access")
	public static String getBeforeAfterMonth(String orgMon, int before) {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM", Locale.KOREA);
        try {
			Date orgDay = dateFormat.parse(orgMon);
			calendar.setTime(orgDay);
		} catch (ParseException e) {
			return "";
		}

        calendar.add(calendar.MONTH, before);

        return dateFormat.format(calendar.getTime());
    }

    @SuppressWarnings("static-access")
	public static String getLaterDate(String orgDate , int before) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);

        Date orgDay = new Date();
        try {
			orgDay = dateFormat.parse(orgDate);
	        calendar.setTime(orgDay);
	        calendar.add(calendar.DATE, before);
		} catch (ParseException e) {
			return "";
		}

        return dateFormat.format(calendar.getTime());
    }

	public static String getLastDate(String orgDate) {
		String retDate = orgDate;

		if(StringUtils.isEmpty(retDate)) {
			return "";
		}

		if(retDate.length() == 6) {
			retDate = retDate.concat("01");
        }

		if(retDate.length() != 8) {
        	return "";
        }

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
        Date lastDate  = new Date();
		try {
			lastDate = dateFormat.parse(retDate);
		} catch (ParseException e) {
			return "";
		}

        calendar.setTime(lastDate);
        int endDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        return retDate.substring(0,6).concat(String.valueOf(endDay));
	}

	public static long getDiffDate(String strDay , String endDay) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);

        long diffDays = -1;
		try {
			Date beginDate = dateFormat.parse(strDay);
	        Date endDate = dateFormat.parse(endDay);

	        long diff = endDate.getTime() - beginDate.getTime();
	        diffDays = diff / (24 * 60 * 60 * 1000);
		} catch (ParseException e) {
			return diffDays;
		}

        return diffDays;
    }

    public static String makePhoneNumber(String phoneNumber) {
        String regEx = "(\\d{3})(\\d{3,4})(\\d{4})";
        if(!Pattern.matches(regEx, phoneNumber)) {
            return null;
        }
        return phoneNumber.replaceAll(regEx, "$1-$2-$3");
    }

	public static String displayDate(String cToDate, String datePattern ) {
		String cToStr = "";
		if (cToDate != null) {
			try {
				Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S",Locale.KOREA).parse(cToDate);
				SimpleDateFormat formatter = new SimpleDateFormat(datePattern,Locale.KOREA);
				cToStr = formatter.format(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				return cToStr;
			}
		}
		return cToStr;
	}

	public static String displayDate(String cToDate) {
		return displayDate(cToDate, "yyyy-MM-dd");
	}

	public static String displayName(String cName ) {

		String rtnName = "";
		if (isBlank(cName)) {
			return rtnName;
		}

		int slen = cName.length();
		int iMod = 0;
		if(slen > 1) {
			iMod = slen / 3;
		}

		String modStr = "";
		if(iMod > 0) {
			if(slen > 6) {
				iMod = iMod+1;
			}
			for(int i=0;i<iMod;i++) {
				modStr = modStr.concat("*");
			}

			rtnName = cName.substring(0,slen-iMod).concat(modStr);
		} else if(slen > 1) {
			rtnName = cName.substring(0,slen-1)+"*";
		} else {
			rtnName = cName;
		}

		return rtnName;
	}

    /**
     * 공백 문자 체크
     *
     * @param spaceCheck
     * @return
     */
    public static boolean spaceCheck(String spaceCheck){
		if (isBlank(spaceCheck)) {
			return false;
		}

    	for(int i = 0 ; i < spaceCheck.length() ; i++) {
    		if(spaceCheck.charAt(i) == ' ')
    			return true;
    	}
    	return false;
    }

    /**
     * 연속된 숫자 체크
     *
     * @param numberCheck
     * @return
     */
    public static boolean continueNumberCheck(String numberCheck){
		if (isBlank(numberCheck)) {
			return false;
		}

		int o = 0;
		int d = 0;
		int p = 0;
		int n = 0;
		int limit = 4;
		for(int i = 0 ; i < numberCheck.length() ; i++) {
			char tempVal = numberCheck.charAt(i);
			if (i > 0 && (p = o - tempVal) > -2 && p < 2 && (n = p == d ? n + 1 : 0) > limit - 3)
				return true;
			d = p;
			o = tempVal;
		}
		return false;
    }

	public static String getClientIpAddr(HttpServletRequest request) {

		String[] HEADER_CLIENT_IP = { "X-Forwarded-For", "X-FORWARDED-FOR", "NS-CLIENT-IP", "HTTP_X_REAL_IP", "X-Real-IP", "X-RealIP", "REMOTE_ADDR",
				"Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"};

		for (String name : HEADER_CLIENT_IP) {
			String clientIp = request.getHeader(name);
			LOGGER.info("getClientIpAddr : {}={}", name, clientIp);
			if(!StringHelper.isBlank(clientIp) && !"unknown".equalsIgnoreCase(clientIp)) {
				if(clientIp.contains(",")) {
					String clientIpVal = clientIp.replaceAll(" ", "");
					clientIp = clientIpVal.substring(0, clientIpVal.indexOf(","));
				}
				return clientIp;
			}
		}
		LOGGER.info("getClientIpAddr : getRemoteAddr");
		return request.getRemoteAddr();
	}

}
