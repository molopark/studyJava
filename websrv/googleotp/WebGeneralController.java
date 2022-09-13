 package com.molo.common.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.molo.common.constants.BizResponseCd;
import com.molo.common.exception.AjaxException;
import com.molo.common.exception.BizException;

public class WebGeneralController {
    private final static Logger LOGGER = LogManager.getLogger(WebGeneralController.class);

	@ExceptionHandler(BizException.class)
	public ModelAndView bizExceptionHandler(BizException be) {
		ModelAndView model;
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("rt", be.getErrCode());
		result.put("rtMsg", "FAIL");

		model = new ModelAndView("/error/systemError","data", result);

		return model;
	}

	@ExceptionHandler(Exception.class)
	public ModelAndView exceptionHandler(Exception e) {
	  LOGGER.error("WebGeneralController.exceptionHandler #exception={}", e.getMessage());

		ModelAndView model;
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("rt", "FFFF");
		result.put("rtMsg", "FAIL");

		model = new ModelAndView("/error/systemError","data", result);

		return model;
	}

	@ExceptionHandler(AjaxException.class)
	public ModelAndView ajaxExceptionHandler(AjaxException ae, HttpServletResponse response) {
		response.setStatus(ae.getHttpStatus());

		Map<String, Object> error = new HashMap<String, Object>();
		error.put("rt", ae.getErrCode());
		error.put("rtMsg", "FAIL");

		return new ModelAndView("jsonView", error);
	}

//	public ModelAndView createAjaxResponse(String code, String message) {
//		Map<String, Object> data = new HashMap<String, Object>();
//		data.put("code", code);
//		data.put("message", message);
//		return new ModelAndView("jsonView", data);
//	}

//	public Pagination makePagination(int pageNum, int listSize, int listItemSize, int totalItemSize) {
//		Pagination pagination = new Pagination(pageNum, listSize);
//		pagination.setListItemSize(listItemSize);
//		pagination.setTotalItemSize(totalItemSize);
//		return pagination;
//	}

	public Map<String, Object> getModel() {
	    return new HashMap<String, Object>();
	}

	public Map<String, Object> getAjaxModel(String rt, String rtMsg, Object data) {
	    Map<String, Object> model = new HashMap<String, Object>();
	    model.put("rt", rt);
	    model.put("rtMsg", rtMsg);
	    model.put("data", data);
        return model;
	}
	public Map<String, Object> getAjaxModel(String rt, String rtMsg) {
        return getAjaxModel(rt, rtMsg, null);
    }
}
