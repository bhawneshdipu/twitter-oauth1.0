package com.dipu.twitter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import okhttp3.*;
import okhttp3.Request;

import java.net.*;
import java.sql.Timestamp;
import java.util.Random;

import sun.misc.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import org.apache.commons.codec.binary.Base64;

/*@Controller annotation as it a a request controller*/

@Controller
public class DemoController {

	@RequestMapping("/")
	String index() {
		return "index";
	}

	@RequestMapping("/get_oauth_token")
	@ResponseBody
	String getAuthToken() {
		String burl = "https://api.twitter.com/oauth/request_token";
		String consumer_key = "uZeVeWO5u5LEdHHKnpOI4pXuM";
		String consumer_secret = "sdlMFDso7zlmXZ0juairElR4X51Y2MfzwFcWZZI23hvRxiEqRf";
		// String token = (String)request.getParameter("oauth_token");
		String token_secret = null;
		String signature_method = "HMAC-SHA1";
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		long oauth_timestamp = ts.getTime() / 1000;
		String timestamp = Long.toString(oauth_timestamp);
		String nonce = genNonce(6);
		// String screen_name=(String)request.getParameter("screen_name");
		// String user_id=(String)request.getParameter("user_id");

		// System.out.println("Nonce = " + nonce + "\nNonce Length" +
		// nonce.length());

		// https://api.twitter.com/oauth/authorize?oauth_token=86327591-MUuvybGO3g2BvirO3sWSzz3Xda4teSu1wX2ruxbCb

		// parameter string
		String parameter_string = null;
		try {
			parameter_string = URLEncoder.encode("oauth_consumer_key", "UTF-8") + "="
					+ URLEncoder.encode(consumer_key, "UTF-8") + "&" + URLEncoder.encode("oauth_nonce", "UTF-8") + "="
					+ URLEncoder.encode(nonce, "UTF-8") + "&" + URLEncoder.encode("oauth_signature_method", "UTF-8")
					+ "=" + URLEncoder.encode("HMAC-SHA1", "UTF-8") + "&"
					+ URLEncoder.encode("oauth_timestamp", "UTF-8") + "=" + URLEncoder.encode(timestamp, "UTF-8") + "&"
					+ URLEncoder.encode("oauth_version", "UTF-8") + "=" + URLEncoder.encode("1.0", "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Parameter String = " + parameter_string + "\n");

		// creating signature
		String signature_base_string = null;
		try {
			signature_base_string = "GET&" + URLEncoder.encode(burl, "UTF-8") + "&"
					+ URLEncoder.encode(parameter_string, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println("signature_base_string = " + signature_base_string
		// + "\n\n");

		String oauth_signature = "";
		oauth_signature = computeSignature(signature_base_string, consumer_secret, token_secret);
		String signature = null;
		try {
			signature = URLEncoder.encode(oauth_signature, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("signature=" + signature + "\n\n");

		// Authorization Header
		String authHeader = "OAuth oauth_consumer_key=\"" + consumer_key + "\","
				+ "oauth_signature_method=\"HMAC-SHA1\"," + "oauth_timestamp=\"" + timestamp + "\"," + "oauth_nonce=\""
				+ nonce + "\"," + "oauth_version=\"1.0\"," + "oauth_signature=\"" + signature + "\"";

		OkHttpClient client = new OkHttpClient();

		okhttp3.Request req = new okhttp3.Request.Builder().url("https://api.twitter.com/oauth/request_token").get()
				.addHeader("authorization", authHeader).build();

		Response resp = null;
		try {
			resp = client.newCall(req).execute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String res = null;
		try {
			res = resp.body().string();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return res;

	}

	@RequestMapping("/authorize_me")
	@ResponseBody
	String authorizeMe(HttpServletRequest request) {

		String oauth_token = (String) request.getParameter("oauth_token");

		String oauth_token_secret = (String) request.getParameter("oauth_token_secret");

		OkHttpClient client = new OkHttpClient();
		Request req = new Request.Builder().url("https://api.twitter.com/oauth/authorize?" + oauth_token).get().build();

		Response resp = null;
		try {
			resp = client.newCall(req).execute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String res = null;
		try {
			res = resp.body().string();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//return res;
return "https://api.twitter.com/oauth/authorize?" + oauth_token;
		
	}

	@RequestMapping("/validate_me")
	@ResponseBody
	String validateMe(HttpServletRequest request) {

		String oauth_verifier = (String) request.getParameter("oauth_verifier");
		String oauth_token = (String) request.getParameter("oauth_token_verifier");

		RequestBody formBody = new FormBody.Builder().add("oauth_verifier", oauth_verifier)
				.add("oauth_token", oauth_token).build();

		OkHttpClient client = new OkHttpClient();
		Request req = new Request.Builder().url("https://api.twitter.com/oauth/access_token?oauth_token=" + oauth_token
				+ "&oauth_verifier=" + oauth_verifier).get().build();

		Response resp = null;
		try {
			resp = client.newCall(req).execute();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String res = null;
		try {
			res = resp.body().string();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;

	}

	@RequestMapping("/get_timeline")
	@ResponseBody
	String getTimeline(HttpServletRequest request) {

		String burl = "https://api.twitter.com/1.1/statuses/user_timeline.json";
		String consumer_key = (String) request.getParameter("oauth_consumer_key");
	
		String consumer_secret = (String) request.getParameter("oauth_consumer_secret");
		String token = (String) request.getParameter("oauth_token");
		String token_secret = (String) request.getParameter("oauth_token_secret");
		String signature_method = "HMAC-SHA1";
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		long oauth_timestamp = ts.getTime() / 1000;
		String timestamp = Long.toString(oauth_timestamp);
		String nonce = genNonce(6);
		String screen_name = (String) request.getParameter("screen_name");
		String user_id = (String) request.getParameter("user_id");

		// System.out.println("Nonce = " + nonce + "\nNonce Length" +
		// nonce.length());

		// https://api.twitter.com/oauth/authorize?oauth_token=86327591-MUuvybGO3g2BvirO3sWSzz3Xda4teSu1wX2ruxbCb

		// parameter string
		String parameter_string = null;
		try {
			parameter_string = URLEncoder.encode("oauth_consumer_key", "UTF-8") + "="
					+ URLEncoder.encode(consumer_key, "UTF-8") + "&" + URLEncoder.encode("oauth_nonce", "UTF-8") + "="
					+ URLEncoder.encode(nonce, "UTF-8") + "&" + URLEncoder.encode("oauth_signature_method", "UTF-8")
					+ "=" + URLEncoder.encode("HMAC-SHA1", "UTF-8") + "&"
					+ URLEncoder.encode("oauth_timestamp", "UTF-8") + "=" + URLEncoder.encode(timestamp, "UTF-8") + "&"
					+ URLEncoder.encode("oauth_token", "UTF-8") + "=" + URLEncoder.encode(token, "UTF-8") + "&"
					+ URLEncoder.encode("oauth_version", "UTF-8") + "=" + URLEncoder.encode("1.0", "UTF-8");
		} catch (UnsupportedEncodingException e4) {
			// TODO Auto-generated catch block
			e4.printStackTrace();
		}

		// System.out.println("Parameter String = " + parameter_string + "\n");

		// creating signature
		String signature_base_string = null;
		try {
			signature_base_string = "GET&" + URLEncoder.encode(burl, "UTF-8") + "&"
					+ URLEncoder.encode(parameter_string, "UTF-8");
		} catch (UnsupportedEncodingException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		// System.out.println("signature_base_string = " + signature_base_string
		// + "\n\n");

		String oauth_signature = "";
		oauth_signature = computeSignature(signature_base_string, consumer_secret, token_secret);
		String signature = null;
		try {
			signature = URLEncoder.encode(oauth_signature, "UTF-8");
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		// System.out.println(signature + "\n\n");

		// Authorization Header
		String authHeader = "OAuth oauth_consumer_key=\"" + consumer_key + "\"," + "oauth_token=\"" + token + "\","
				+ "oauth_signature_method=\"HMAC-SHA1\"," + "oauth_timestamp=\"" + timestamp + "\"," + "oauth_nonce=\""
				+ nonce + "\"," + "oauth_version=\"1.0\"," + "oauth_signature=\"" + signature + "\"";

		OkHttpClient client = new OkHttpClient();

		okhttp3.Request req = new okhttp3.Request.Builder()
				.url("https://api.twitter.com/1.1/statuses/user_timeline.json").get()
				.addHeader("authorization", authHeader).addHeader("screen_name", screen_name)
				.addHeader("user_id", user_id)

				.build();

		Response resp = null;
		try {
			resp = client.newCall(req).execute();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String res = null;
		try {
			res = resp.body().string();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;

	}

	public String genNonce(int len) {
		// Pick from some letters that won't be easily mistaken for each
		// other. So, for example, omit o O and 0, 1 l and L.
		String letters = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ";

		String pw = "";
		for (int i = 0; i < len; i++) {
			int index = (int) (new Random().nextDouble() * letters.length());
			pw += letters.substring(index, index + 1);
		}
		return pw;
	}

	@RequestMapping("/get_friend_list")
	@ResponseBody
	String getFriendList(HttpServletRequest request) {

		String burl = "https://api.twitter.com/1.1/friends/list.json";
		String consumer_key = (String) request.getParameter("oauth_consumer_key");
		;
		String consumer_secret = (String) request.getParameter("oauth_consumer_secret");
		String token = (String) request.getParameter("oauth_token");
		String token_secret = (String) request.getParameter("oauth_token_secret");
		String signature_method = "HMAC-SHA1";
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		long oauth_timestamp = ts.getTime() / 1000;
		String timestamp = Long.toString(oauth_timestamp);
		String nonce = genNonce(6);
		String screen_name = (String) request.getParameter("screen_name");
		String user_id = (String) request.getParameter("user_id");

		// System.out.println("Nonce = " + nonce + "\nNonce Length" +
		// nonce.length());

		// https://api.twitter.com/oauth/authorize?oauth_token=86327591-MUuvybGO3g2BvirO3sWSzz3Xda4teSu1wX2ruxbCb

		// parameter string
		String parameter_string=null;
		try {
			parameter_string = URLEncoder.encode("oauth_consumer_key", "UTF-8") + "="
					+ URLEncoder.encode(consumer_key, "UTF-8") + "&" + URLEncoder.encode("oauth_nonce", "UTF-8") + "="
					+ URLEncoder.encode(nonce, "UTF-8") + "&" + URLEncoder.encode("oauth_signature_method", "UTF-8")
					+ "=" + URLEncoder.encode("HMAC-SHA1", "UTF-8") + "&"
					+ URLEncoder.encode("oauth_timestamp", "UTF-8") + "=" + URLEncoder.encode(timestamp, "UTF-8") + "&"
					+ URLEncoder.encode("oauth_token", "UTF-8") + "=" + URLEncoder.encode(token, "UTF-8") + "&"
					+ URLEncoder.encode("oauth_version", "UTF-8") + "=" + URLEncoder.encode("1.0", "UTF-8");
		} catch (UnsupportedEncodingException e4) {
			// TODO Auto-generated catch block
			e4.printStackTrace();
		}

		// System.out.println("Parameter String = " + parameter_string + "\n");

		// creating signature
		String signature_base_string=null;
		try {
			signature_base_string = "GET&" + URLEncoder.encode(burl, "UTF-8") + "&"
					+ URLEncoder.encode(parameter_string, "UTF-8");
		} catch (UnsupportedEncodingException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		// System.out.println("signature_base_string = " + signature_base_string
		// + "\n\n");

		String oauth_signature = "";
		oauth_signature = computeSignature(signature_base_string, consumer_secret, token_secret);
		String signature=null;
		try {
			signature = URLEncoder.encode(oauth_signature, "UTF-8");
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		// System.out.println(signature + "\n\n");

		// Authorization Header
		String authHeader = "OAuth oauth_consumer_key=\"" + consumer_key + "\"," + "oauth_token=\"" + token + "\","
				+ "oauth_signature_method=\"HMAC-SHA1\"," + "oauth_timestamp=\"" + timestamp + "\"," + "oauth_nonce=\""
				+ nonce + "\"," + "oauth_version=\"1.0\"," + "oauth_signature=\"" + signature + "\"";

		OkHttpClient client = new OkHttpClient();

		okhttp3.Request req = new okhttp3.Request.Builder().url("https://api.twitter.com/1.1/friends/list.json").get()
				.addHeader("authorization", authHeader).addHeader("screen_name", screen_name)
				.addHeader("user_id", user_id)

				.build();

		Response resp = null;
		try {
			resp = client.newCall(req).execute();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String res = null;
		try {
			res = resp.body().string();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;

	}

	@RequestMapping("/get_follower_list")
	@ResponseBody
	String getFollowerList(HttpServletRequest request) {

		String burl = "https://api.twitter.com/1.1/followers/list.json";
		String consumer_key = (String) request.getParameter("oauth_consumer_key");
		;
		String consumer_secret = (String) request.getParameter("oauth_consumer_secret");
		String token = (String) request.getParameter("oauth_token");
		String token_secret = (String) request.getParameter("oauth_token_secret");
		String signature_method = "HMAC-SHA1";
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		long oauth_timestamp = ts.getTime() / 1000;
		String timestamp = Long.toString(oauth_timestamp);
		String nonce = genNonce(6);
		String screen_name = (String) request.getParameter("screen_name");
		String user_id = (String) request.getParameter("user_id");

		// System.out.println("Nonce = " + nonce + "\nNonce Length" +
		// nonce.length());

		// https://api.twitter.com/oauth/authorize?oauth_token=86327591-MUuvybGO3g2BvirO3sWSzz3Xda4teSu1wX2ruxbCb

		// parameter string
		String parameter_string = null;
		try {
			parameter_string = URLEncoder.encode("oauth_consumer_key", "UTF-8") + "="
					+ URLEncoder.encode(consumer_key, "UTF-8") + "&" + URLEncoder.encode("oauth_nonce", "UTF-8") + "="
					+ URLEncoder.encode(nonce, "UTF-8") + "&" + URLEncoder.encode("oauth_signature_method", "UTF-8")
					+ "=" + URLEncoder.encode("HMAC-SHA1", "UTF-8") + "&"
					+ URLEncoder.encode("oauth_timestamp", "UTF-8") + "=" + URLEncoder.encode(timestamp, "UTF-8") + "&"
					+ URLEncoder.encode("oauth_token", "UTF-8") + "=" + URLEncoder.encode(token, "UTF-8") + "&"
					+ URLEncoder.encode("oauth_version", "UTF-8") + "=" + URLEncoder.encode("1.0", "UTF-8");
		} catch (UnsupportedEncodingException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		// System.out.println("Parameter String = " + parameter_string + "\n");

		// creating signature
		String signature_base_string = null;
		try {
			signature_base_string = "GET&" + URLEncoder.encode(burl, "UTF-8") + "&"
					+ URLEncoder.encode(parameter_string, "UTF-8");
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		// System.out.println("signature_base_string = " + signature_base_string
		// + "\n\n");

		String oauth_signature = "";
		oauth_signature = computeSignature(signature_base_string, consumer_secret, token_secret);
		String signature = null;
		try {
			signature = URLEncoder.encode(oauth_signature, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// System.out.println(signature + "\n\n");

		// Authorization Header
		String authHeader = "OAuth oauth_consumer_key=\"" + consumer_key + "\"," + "oauth_token=\"" + token + "\","
				+ "oauth_signature_method=\"HMAC-SHA1\"," + "oauth_timestamp=\"" + timestamp + "\"," + "oauth_nonce=\""
				+ nonce + "\"," + "oauth_version=\"1.0\"," + "oauth_signature=\"" + signature + "\"";

		OkHttpClient client = new OkHttpClient();

		okhttp3.Request req = new okhttp3.Request.Builder().url("https://api.twitter.com/1.1/followers/list.json").get()
				.addHeader("authorization", authHeader).addHeader("screen_name", screen_name)
				.addHeader("user_id", user_id)

				.build();

		Response resp = null;
		try {
			resp = client.newCall(req).execute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String res = null;
		try {
			res = resp.body().string();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	private String computeSignature(String signatueBaseStr, String oAuthConsumerSecret, String oAuthTokenSecret) {

		byte[] byteHMAC = null;
		try {
			Mac mac = Mac.getInstance("HmacSHA1");
			SecretKeySpec spec;
			if (null == oAuthTokenSecret) {
				String signingKey = URLEncoder.encode(oAuthConsumerSecret, "UTF-8") + '&';
				spec = new SecretKeySpec(signingKey.getBytes(), "HmacSHA1");
			} else {
				String signingKey = URLEncoder.encode(oAuthConsumerSecret, "UTF-8") + '&'
						+ URLEncoder.encode(oAuthTokenSecret, "UTF-8");
				spec = new SecretKeySpec(signingKey.getBytes(), "HmacSHA1");
			}
			mac.init(spec);
			byteHMAC = mac.doFinal(signatueBaseStr.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String(Base64.encodeBase64(byteHMAC));
	}

}
