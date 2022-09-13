package com.molo.common.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.molo.service.web.dto.LoginInfoDto;
import com.molo.service.web.dto.SessionDto;

public class SessionHelper {

	private static final Logger LOG = LoggerFactory.getLogger(SessionHelper.class);

	/** 로그인 세션 name */
	public static final String LOGIN_SESSION = "ADM";

	/** 로그인 후 리다이렉트 url 세션 key name */
	public static final String LOGIN_REDIRECT_URL = "LOGIN_REDIRECT_URL";

	public static SessionDto getSessionDto(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute(LOGIN_SESSION) == null) {
            return null;
        }

        return (SessionDto) session.getAttribute(LOGIN_SESSION);
    }

    public static void makeSession(HttpServletRequest request, LoginInfoDto loginInfoDto) {
        HttpSession session = request.getSession();
        SessionDto sessionDto = new SessionDto();
        sessionDto.setLoginInfoDto(loginInfoDto);

        session.setAttribute(LOGIN_SESSION, sessionDto);
    }

    public static void removeSsessionDto(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        session.removeAttribute(LOGIN_SESSION);
    }

	public static boolean isLogin(HttpServletRequest request) {
        SessionDto sessionDto = getSessionDto(request);

        if (sessionDto == null) {
            LOG.debug("SessionHelper.isLogin() #sessionDto is null");
            return false;
        }

        String memberId = sessionDto.getLoginInfoDto().getMemberId();
        LOG.debug("SessionHelper.isLogin() #memberId={}", memberId);

        return true;
    }

	public static String getLoginRedirectUrl(HttpServletRequest request) {
		HttpSession session = request.getSession();

		if (session.getAttribute(LOGIN_REDIRECT_URL) == null) {
			return "/main.do";
		}

		return (String) session.getAttribute(LOGIN_REDIRECT_URL);
	}


}
