package com.molo.service.web.controller;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base32;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.molo.common.controller.WebGeneralController;
import com.molo.common.utils.SessionHelper;
import com.molo.common.utils.StringHelper;
import com.molo.service.common.captcha.AlphabetAudioProducer;
import com.molo.service.common.captcha.RandomLetterSpaceWordRenderer;
import com.molo.service.web.dto.LoginInfoDto;
import com.molo.service.web.dto.SessionDto;
import com.molo.service.web.service.LoginService;

import nl.captcha.Captcha;
import nl.captcha.servlet.CaptchaServletUtil;
import nl.captcha.text.producer.DefaultTextProducer;

@Controller
public class LoginController extends WebGeneralController {
    private final static Logger LOGGER = LogManager.getLogger(LoginController.class);

    //simplecaptcha : simplecaptcha-1.2.1.jar
    //private static char[] CHARS = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
    private static char[] NUMS = {'1','2','3','4','5','6','7','8','9','0'};
    private static final int CAPTCHA_LENGTH = 5;

    @Autowired LoginService loginService;


    @RequestMapping(value = "/Login.do", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView Login(
    		LoginInfoDto loginReqDto
            , HttpServletRequest request
            , HttpServletResponse response
            , HttpSession session) {

    	//LOGGER.info("LoginController.Login()");

    	LOGGER.info("LoginController StringHelper.getClientIpAddr : {}", StringHelper.getClientIpAddr(request));

    	if(SessionHelper.isLogin(request)) {
    		LOGGER.info("Login 로그인 되어있음. 페이지 이동..............................");
    		return new ModelAndView("redirect:"+ SessionHelper.getLoginRedirectUrl(request));
    	}

    	Map<String, Object> model = getModel();

      return new ModelAndView("/login",model);
    }

    @RequestMapping(value = "/MakeOtp.json", method = {RequestMethod.POST})
    public ModelAndView MakeOtp(
    		LoginInfoDto loginReqDto
            , HttpServletRequest request
            , HttpServletResponse response
            , HttpSession session) {

    	Map<String, Object> model = getModel();

    	String trResult = "SUCCESS";
    	String trMsg = "";

    	String loginId = StringHelper.nvl(loginReqDto.getMemberId());
    	LoginInfoDto LoginInfoDto = null;

    	if(!"".equals(loginId)) {
            LoginInfoDto = loginService.selectLoginInfo(loginReqDto);
        	//LOGGER.info("LoginInfoDto : {}", ObjectHelper.convertObjectToString(LoginInfoDto));
    	}

        if(LoginInfoDto == null || "".equals(StringHelper.nvl(LoginInfoDto.getMemberId()))) {
        	trResult = "OTPFail";
        	trMsg = "OTP 받기 실패 , ID 를 확인해 주세요.";
        } else {
            // google otp
        	byte[] buffer = new byte[5 + 5 * 5];
        	new Random().nextBytes(buffer);
        	Base32 codec = new Base32();
        	byte[] secretKey = Arrays.copyOf(buffer, 5);
            byte[] bEncodedKey = codec.encode(secretKey);

            // 생성된 Key!
            String encodedKey = new String(bEncodedKey);
            //LOGGER.info("encodedKey : " + encodedKey);

            String hostName = request.getHeader("host");
            String url = getQRBarcodeURL(loginId, hostName, encodedKey); // 생성된 바코드 주소!
            //LOGGER.info("url : " + url);

        	model.put("encodedKey", encodedKey);
            model.put("url", url);
    	}
        //LOGGER.info("trResult : " + trResult);

    	model.put("rt", trResult);
      model.put("rtMsg", trMsg);

    	return new ModelAndView("jsonView", model);
    }

    @RequestMapping(value = "/LoginExcute.json", method = {RequestMethod.POST})
    public ModelAndView LoginExcute(
    		LoginInfoDto loginReqDto
            , HttpServletRequest request
            , HttpServletResponse response
            , HttpSession session) {

    	//LOGGER.info("LoginController.agentLoginExcute() #START");

    	Map<String, Object> model = getModel();

    	String loginId = loginReqDto.getMemberId();

    	// captcha 체크
    	boolean captchaResult = true;
    	//String reqCaptcha = StringHelper.nvl(request.getParameter("capchaInput"),"");
    	//String sesCaptcha = StringHelper.nvl((String) request.getSession().getAttribute(Captcha.NAME),"");

    	//if (!("").equals(sesCaptcha) && !sesCaptcha.equalsIgnoreCase(reqCaptcha)) {
	    //	captchaResult = false;
	    //}

    	// 수정하는 데이터
      String trResult = "";
      String trMsg = "";
    	String changeData = "";
    	String clientIp = StringHelper.getClientIpAddr(request);

    	if(captchaResult) {
            LoginInfoDto LoginInfoDto = loginService.selectLoginInfo(loginReqDto, clientIp );
        	//LOGGER.info("LoginInfoDto : {}", ObjectHelper.convertObjectToString(LoginInfoDto));

            trResult = LoginInfoDto.getRt();
            trMsg = LoginInfoDto.getRtMsg();

            if(BizResponseCd.SUCCESS.equals(trResult)){
    	    	trResult = "SUCCESS";
    	    	changeData = "로그인 성공";

    	    	SessionHelper.makeSession(request, LoginInfoDto);
    	    } else {
            	trResult = "FAIL:".concat(trResult);
            	changeData = "로그인 실패";
    	    }
    	} else {
        	trResult = "FAIL:0010";
        	trMsg = "캡챠 정보가 일치하지 않습니다.";
        	changeData = "캡챠인증실패";
    	}

    	model.put("rt", trResult);
      model.put("rtMsg", trMsg);

    	// 로그인 정보 초기화
    	model.put("LoginInfoDto", "");

      return new ModelAndView("jsonView", model);
    }

    @RequestMapping(value = "/Logout.do", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView Logout(HttpServletRequest request
            , HttpServletResponse response
            , HttpSession session) {

    	SessionDto SsessionDto = SessionHelper.getSessionDto(request);
    	String loginId = SsessionDto.getLoginInfoDto().getMemberId();

      SessionHelper.removeSsessionDto(request);

    	// 수정하는 데이터
      String trResult = "";
    	String changeData = "";

      if( SessionHelper.isLogin(request) ) {
      	LOGGER.info("Logout 실패");
      } else {
      	trResult = "SUCCESS";
      	changeData = "로그아웃 성공";
      }

      return new ModelAndView("redirect:/Login.do");
    }

    @RequestMapping(value = "/Chklogin.json", method = { RequestMethod.POST, RequestMethod.GET })
    public ModelAndView Chklogin(
    		LoginInfoDto loginReqDto
            , HttpServletRequest request
            , HttpServletResponse response
            , HttpSession session) {

    	LOGGER.info("LoginController.Chklogin() #START");

    	Map<String, Object> model = getModel();

      try {
          boolean isLogin = SessionHelper.isLogin(request);


          if(isLogin) {
              model.put("rt", BizResponseCd.SUCCESS);
              model.put("rtMsg", "로그인 되어 있음");
          } else {
              model.put("rt", BizResponseCd.FAIL);
              model.put("rtMsg", "로그인이 필요합니다.");
              model.put("redirectUrl", "/Login.do");
          }

      } catch(Exception e) {
      }
      LOGGER.info("LoginController.Chklogin() #END");
      return new ModelAndView("jsonView", model);
    }

    //captcha func

    @RequestMapping(value = {"/CapchaCertInput.do"})
    public void doCapchaCertInput(HttpServletRequest request, HttpServletResponse response) {

  	   Captcha captcha = new Captcha.Builder(86, 45)					// default
  		   .addText(new DefaultTextProducer(CAPTCHA_LENGTH, NUMS), new RandomLetterSpaceWordRenderer()).build();

  	   LOGGER.info("captcha answer() : " + captcha.getAnswer());
       response.reset();
       response.setHeader("Pragma", "no-cache");
       response.setDateHeader("Expires", 0);

       CaptchaServletUtil.writeImage(response, captcha.getImage());

       request.getSession().setAttribute(Captcha.NAME, captcha.getAnswer());
    }

    @RequestMapping(value = {"/captchaAudio.do"})
	  public void makeCaptchaAudio(HttpServletRequest request, HttpServletResponse response) {
    	response.reset();
    	String captcha = (String) request.getSession().getAttribute(Captcha.NAME);

    	AlphabetAudioProducer.writeAudio(response, captcha);
	  }

    // googel opt func
    public static String getQRBarcodeURL(String user, String host, String secret) {
        String format = "https://chart.apis.google.com/chart?cht=qr&amp;chs=110x110&amp;chl=otpauth://totp/%s@%s%%3Fsecret%%3D%s&amp;chld=H|0";
        return String.format(format, user, host, secret);
    }

}
