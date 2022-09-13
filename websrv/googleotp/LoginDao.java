package com.molo.service.web.dao;

import com.molo.service.web.dto.LoginInfoDto;

public interface LoginDao {

    public LoginInfoDto selectLoginInfo(LoginInfoDto loginReqDto);

    public int updateLoginResult(LoginInfoDto loginReqDto);

    public void updateLoginOtp(LoginInfoDto loginReqDto);

}
