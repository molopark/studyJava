var login = {
	onlyNumber: function(e) {
		$(e).on("keyup", function() {
		    $(this).val($(this).val().replace(/[^0-9]/g,""));
		});
	}
}
$(document).ready(function(){

	$(document).on("click",".btn-login", function() {

		if ($.trim($("#login-id").val()) == "") {
			alert("ID 를 입력해 주시기 바랍니다.");
			return false;
		}
/*
		if ($.trim($("#encodedKey").val()) == "") {
			alert("OTP받기 후 번호를 입력해 주시기 바랍니다.");
			return false;
		}
*/
		if ($.trim($("#login-otp").val()) == "") {
			alert("OTP번호를 입력해 주시기 바랍니다.");
			$("#login-otp").focus();
			return false;
		}

		data = {
				memberId : $("#login-id").val(),
				passKey : $("#login-pw").val(),
				//capchaInput : $("#captcha").val(),
				otpCode : $("#login-otp").val(),
				otpCodeKey : $("#encodedKey").val()
		};
	    $.ajax({
	        url:'/LoginExcute.json',
	        dataType:'json',
	        data : data,
	        type:"post",
	        success: function(data) {
	        	var rt = data.rt;
	        	var rtMsg = data.rtMsg;
	        	if(rt == 'SUCCESS') {
		        		location.href = '/main.do';
	        	} else {
	        		alert(rtMsg);
	        		$("#login-pw").val("");
	        		$("#login-otp").val("");
	        		//$("#captcha").val("");
	        	}
	        }
	    });

	});

	$(document).on("click",".btn-otp", function() {

		if ($.trim($("#login-id").val()) == "") {
			alert("ID 를 입력해 주시기 바랍니다.");
			return false;
		}

		data = {
				memberId : $("#login-id").val()
		};
	    $.ajax({
	        url:'/MakeOtp.json',
	        dataType:'json',
	        data : data,
	        type:"post",
	        success: function(data) {
	        	var rt = data.rt;
	        	var rtMsg = data.rtMsg;
	        	if(rt == 'SUCCESS') {
	        		$("#encodedKey").val(data.encodedKey);
	        		$("#otpUrl").html("<iframe src='"+data.url+"' width='110' height='110'></iframe>");
	        	} else {
	        		$("#encodedKey").val();
	        		$("#otpUrl").html("<iframe src='' width='110' height='110'></iframe>");
	        		alert(rtMsg);
	        	}
	        }
	    });

	});

	$("#btnCaptchaImage").click(function() {
        var rand = "r=" + Math.floor(Math.random() * 999999) + 100000; //100000 부터 999999범위 중 radom 수 발생
        $("#imgCaptcha").attr("src", "/CapchaCertInput.do?" + rand);
        $("#captcha").val("");
        return false;
    });


	$("#btnCaptchaAudio").click(function() {
        var rand = "r=" + Math.floor(Math.random() * 999999) + 100000;
        var uAgent = navigator.userAgent;
        var srcUrl = "/captchaAudio.do?" + rand;

        if (uAgent.indexOf('Trident') > -1 || uAgent.indexOf('MSIE') > -1 || window.ActiveXobject) {

            if (uAgent.indexOf("MSIE 8") > 0) {
                var htmlString = "<object type='audio/x-wav' width='0' height='0'>"
                        + "<param name='src' value='" + srcUrl + "'/>"
                        + "<param name='autostart' value='true' />"
                        + "<param name='controller' value='false' />"
                        + "</object>";
                $("#eleCaptchaAudio").empty();
                $("#eleCaptchaAudio").append(htmlString);
            } else {
                $("#eleCaptchaAudio").html("<bgsound loop='1' src='" + srcUrl + "' />");
            }
        } else {
            var a = document.createElement("audio");
            if (a.canPlayType && a.canPlayType("audio/wav") && !(uAgent.indexOf('Safari') > -1 && uAgent.indexOf('Chrome') < 0)) {
                //html5 audio wav 지원되는 경우, 사파리는 세션ID가 전달되지 않아 사용불가하므로 미지원 alert 노출
                $("#eleCaptchaAudio").html("<audio src='" + srcUrl + "' autoplay='true' />");
            } else {
                alert("보안문자 음성듣기가 지원되지 않는 브라우저입니다.");
            }
        }

        return false;
    });

});
