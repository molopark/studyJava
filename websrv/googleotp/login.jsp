<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="Keywords" content="">
	<meta name="Description" content="">
	<title>MOLO Web</title>
	<link rel="stylesheet" type="text/css" href="/css/webfont.css">
	<link rel="stylesheet" type="text/css" href="/css/common.css">
	<style>
		html, body {width: 100%; height: 100%; background-color: #eceff8;}
	</style>

	<script src="/js/jquery-1.12.4.min.js"></script>
	<!--[if lt IE 9]>
	<script src="../../js/html5shiv.min.js"></script>
	<![endif]-->

	<script type="text/javascript" src="/js/login.js"></script>
</head>
<body>

<!-- login-wrap start -->
<div id="login-wrap">
	<div class="login-cont">
		<h2 class="title">로그인 하신 후 이용 하실 수 있습니다.</h2>
		<div class="frm">
			<label for="login-id" class="screen-out">아이디</label> <input type="text" name="" id="login-id" maxlength="20" placeholder="ID(사번)">
			<label for="login-pw" class="screen-out">패스워드</label> <input type="password" name="" id="login-pw" maxlength="16" placeholder="비밀번호">
		</div>
<%--
		<div class="captcha">
			<!-- <p class="captcha-info"><span class="dot">서비스를 이용하시려면 아래 이미지의 문자를 보이는대로 입력 후 관리자 로그인을 눌러주세요.</span></p> -->
			<div class="captcha-cont">
            	<div class="captcha-img">
                    <div class="img">
                        <img src="/CapchaCertInput.do" id="imgCaptcha" alt="보안문자 이미지"/>
                    </div>
                	<ul class="btn-list">
						<!-- <li><a href="javascript:;" id="btnCaptchaAudio" class="btn-voice">음성듣기</a></li> -->
						<li><a href="javascript:;" id="btnCaptchaImage" class="btn-refresh"><img src="/images/icon-refresh.png" alt="새로고침" />새로고침</a></li>
					</ul>
                </div>
				<div class="frm">
                	<label for="captcha" class="screen-out">이미지의 문자 입력</label> <input type="text" name="" id="captcha" placeholder="이미지의 문자 입력" maxlength="5" onKeyDown="javascript:login.onlyNumber(this)">
					<!--<input type="text" id="captcha" name="captcha" autocomplete="off" maxlength="10" class="text" title="이미지의 문자 입력" style="width:250px;" />-->
					<span id="eleCaptchaAudio" style="display:none;"></span>
				</div>
			</div>
		</div>
--%>
		<div class="otp">
        	<div class="otp_input">
            	<input type="hidden" name="encodedKey" id="encodedKey" value="" readonly>
            	<div class="frm">
                    <label for="login-otp" class="screen-out">OTP번호</label> <input type="text" onKeyDown="javascript:login.onlyNumber(this)" name="" id="login-otp" maxlength="6" placeholder="OTP번호">
                </div>
                <div class="btn"><a href="#" class="btn-otp">OTP 받기</a></div>
            </div>
            <div class="qr" id="otpUrl">
            	<iframe src="" width="110" height="110"></iframe>
            </div>
            <ul class="text_info">
            	<li>Google OTP 앱 설치 후 이용이 가능합니다.</li>
                <li>OTP 받기 후 앱으로 생성된 QR코드를 읽으면 입력번호를 확인 가능합니다.</li>
            </ul>
        </div>
        <div class="btn_login">
        	<a href="#" class="btn-login">관리자 로그인</a>
        </div>
	</div>
	<div class="login-footer">Copyright© 2018 MOLO Web. All rights reserved.</div>
</div>

<!--// login-wrap end -->
</body>
</html>
