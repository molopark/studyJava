package com.molo.service.common.captcha;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class AlphabetAudioProducer {
    private final static Logger LOGGER = LogManager.getLogger(AlphabetAudioProducer.class);

	private static final Map<String, String> AUDIO_MAP;

	static {
		AUDIO_MAP = new HashMap<String, String>();

//		for (int i = 0; i < 26; i++) {
//			String audioKey = String.valueOf((char)('A' + i));
//			AUDIO_MAP.put(audioKey, "/sounds/en/alphabets/" + audioKey + ".wav");
//		}

		for (int i = 0; i < 10; i++) {
			String audioKey = "" + i;
			AUDIO_MAP.put(audioKey, "/sounds/ko/" + i + ".wav");
		}

	}

	/**
	 * 캡차 알파벳 문자열 오디오 전송
	 * response 헤더에 캐시 관련 옵션 추가하면 IE에서 페이지 새로고침시 오디오 재생이 안되므로 추가하지 말것
	 * @param response
	 * @param alphaStr
	 */
	public static void writeAudio(HttpServletResponse response, String alphaStr) {

		if (alphaStr == null || alphaStr.length() == 0 || response == null) {
			return;
		}

		// header에 캐시 관련 설정이 있으면 ie 에서 bgsound 로 재생할 경우 페이지 새로고침 시 audio 재생이 안됨
		// object로 할 경우 audio 호출이 두번됨.. 왜 그런지 모르겠음(유사현상이 발생하는 사이트도 있고 아닌 사이트도 있음)
//		response.setHeader("Cache-Control", "private,no-cache,no-store");
//		response.setHeader("Pragma", "no-cache");
//		response.setDateHeader("Expires", 0);
//
		response.setContentType("audio/wave");

		//long start = System.currentTimeMillis();
		List<AudioInputStream> audioInputStreamList = null;
		AudioFormat audioFormat = null;
		Long frameLength = null;
		AudioInputStream captchaAudio = null;
		ByteArrayOutputStream baos = null;
		OutputStream os = null;

		//LOG.debug("audio string={}", alphaStr);
		try {

			AudioInputStream audioInputStream = null;
			// loop through our files first and load them up
			for (int i = 0; i < alphaStr.length(); i++) {
				String resource = AUDIO_MAP.get(String.valueOf(alphaStr.charAt(i)).toUpperCase());

				InputStream is = Thread.currentThread().getContextClassLoader()
						.getResourceAsStream(resource);

				InputStream bis = new BufferedInputStream(is);
				audioInputStream = AudioSystem.getAudioInputStream(bis);

				// get the format of first file
				if (audioFormat == null) {
					audioFormat = audioInputStream.getFormat();
				}

				// add it to our stream list
				if (audioInputStreamList == null) {
					audioInputStreamList = new ArrayList<AudioInputStream>();
				}
				audioInputStreamList.add(audioInputStream);

				// keep calculating frame length
				if (frameLength == null) {
					frameLength = audioInputStream.getFrameLength();
				} else {
					frameLength += audioInputStream.getFrameLength();
				}
			}

			// Convert to BAOS so we can set the content-length header
			baos = new ByteArrayOutputStream(1024);
			captchaAudio = new AudioInputStream(new SequenceInputStream(Collections
					.enumeration(audioInputStreamList)), audioFormat, frameLength);

			AudioSystem.write(captchaAudio, AudioFileFormat.Type.WAVE, baos);

			response.setContentLength(baos.size());

			os = response.getOutputStream();
			os.write(baos.toByteArray());
			os.flush();
			os.close();

			//LOG.debug("captcha audio response complete");
		} catch (IOException e) {
			LOGGER.info("exception");
			//LOG.error("writeAudio ERROR", e);
		} catch (Exception e) {
			LOGGER.info("exception");
			//LOG.error("writeAudio ERROR", e);
		} finally {
			if (captchaAudio != null) {
				try {
					captchaAudio.close();
				} catch (IOException e) {
					LOGGER.info("exception");
					//LOG.error("audioInputStream close ERROR", e);
				}
			}
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException e) {
					LOGGER.info("exception");
					//LOG.error("baos close ERROR", e);
				}
			}
			if (audioInputStreamList != null) {
				for(AudioInputStream ais : audioInputStreamList) {
					if (ais != null) {
						try {
							ais.close();
						} catch (IOException e) {
							LOGGER.info("exception");
							//LOG.error("ais close ERROR", e);
						}
					}
				}

				audioInputStreamList = null;
			}
			if(os !=null){
				try {
					os.close();
				} catch (IOException e) {
					LOGGER.info("exception");
					//LOG.error("os close ERROR", e);
				}
			}
		}

		//long end = System.currentTimeMillis();
		//LOG.debug("response audio time = {}ms", end - start);
	}

}
