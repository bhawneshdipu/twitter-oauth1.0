<%@ page import="com.dipu.twitter.*"%>

<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="java.net.*"%>
<%@ page import="sun.misc.*"%>
<%@ page import="javax.crypto.*"%>

<%@ page import="javax.crypto.spec.*"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="javax.servlet.*"%>
<%@ page import="javax.servlet.*"%>
<%@ page import="java.io.IOException"%>
<%@ page import="okhttp3.*"%>
<%@ page import="org.apache.commons.codec.binary.Base64"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">

<!-- jQuery library -->
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>

<!-- Latest compiled JavaScript -->
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

<script type="text/javascript" src="js/ajax.js"></script>
<link href="css/style.css" rel="stylesheet" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Twitter Auth</title>

</head>
<body>

	<%!public String genNonce(int len) {
		// Pick from some letters that won't be easily mistaken for each
		// other. So, for example, omit o O and 0, 1 l and L.
		String letters = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ";

		String pw = "";
		for (int i = 0; i < len; i++) {
			int index = (int) (new Random().nextDouble() * letters.length());
			pw += letters.substring(index, index + 1);
		}
		return pw;
	}%>


	<%!private String computeSignature(String signatueBaseStr, String oAuthConsumerSecret, String oAuthTokenSecret) {

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
	}%>
	<%
		Long ts = new java.util.Date().getTime() / 1000;
		String timestamp = ts.toString();
		String value = "GET&" + "https://api.twitter.com/oauth/request_token&oauth_consumer_key="
				+ TwitterSecret.oauth_consumer_key + "&oauth_signature_method=HMAC-SHA1&oauth_timestamp="
				+ ts.toString() + "&oauth_nonce=bEqcra&oauth_version=1.0";

		String sign_base_str = URLEncoder.encode(value, "UTF-8");

		String oauth_token_secret = "";

		String sign = computeSignature(TwitterSecret.oauth_consumer_secret, oauth_token_secret, sign_base_str);
		String nonce = genNonce(7);
	%>
	<div class="navbar navbar-default">
		<div class="container-fluid">
			<div class="navbar-header">
				<a class="navbar-brand" href="#">Twitter API Authentication</a>
			</div>
			<ul class="nav navbar-nav">
				<li class="active"><a href="#">Home</a></li>

			</ul>
		</div>
	</div>

	<div class="container">
		
		
		<div class="col-lg-12 col-md-12 ">
			
			
		<div class="col-lg-6 col-md-12 ">
			
			<div class="col-md-12 form-group" hidden="hidden">
				<label class="control-label col-sm-3" for="oauth_url">URL:</label>
				<div class="col-sm-9">
					<input type="text" class="form-control"
						value="https://api.twitter.com/oauth/request_token" id="oauth_url"
						name="oauth_url">
				</div>
			</div>
			<div class="col-md-12 form-group"  hidden="hidden">
				<label class="control-label col-sm-3" for="consumer_secret">consumer_secret:</label>
				<div class="col-sm-9">
					<input type="text" class="form-control" readonly
						value="<%=TwitterSecret.oauth_consumer_secret%>"
						id="oauth_consumer_secret" name="oauth_consumer_secret">
				</div>
			</div>
			<div class="col-md-12 form-group"  hidden="hidden">
				<label class="control-label col-sm-3" for="consumer_key">consumer_key:
				</label>
				<div class="col-sm-9">

					<input type="text" readonly class="form-control"
						value="<%=TwitterSecret.oauth_consumer_key%>"
						id="oauth_consumer_key" name="oauth_consumer_key">
				</div>
			</div>
			<div class="col-md-12 form-group"  hidden="hidden">
				<label class="control-label col-sm-3" for="oauth_signature_method">Oauth_signature
					Method: </label>
				<div class="col-sm-9">

					<input type="text" class="form-control" readonly value="HMAC-SHA1"
						id="oauth_signature_method" name="oauth_signature_method">
				</div>
			</div>
			<div class="col-md-12 form-group"  hidden="hidden">
				<label class="control-label col-sm-3" for="oauth_timestamp">oauth_timestamp:
				</label>
				<div class="col-sm-9">
					<input type="text" class="form-control" readonly
						value="<%=timestamp%>" id="oauth_timestamp" name="oauth_timestamp">
				</div>
			</div>
			<div class="col-md-12 form-group"  hidden="hidden">
				<label class="control-label col-sm-3" for="oauth_nonce">oauth_nonce:
				</label>
				<div class="col-sm-9">
					<input type="text" class="form-control" value="<%=nonce%>"
						id="oauth_nonce" name="oauth_nonce">
				</div>
			</div>
			<div class="col-md-12 form-group"  hidden="hidden">
				<label class="control-label col-sm-3" for="oauth_version">oauth_version:
				</label>
				<div class="col-sm-9">
					<input type="text" class="form-control" readonly value="1.0"
						id="oauth_version" name="oauth_version">
				</div>
			</div>


			<div class="col-md-12 form-group"  hidden="hidden">
				<label class="control-label col-sm-3" for="oauth_signature">oauth_signature:
				</label>
				<div class="col-sm-9">
					<input type="text" class="form-control" value="<%=sign%>"
						id="oauth_signature" name="oauth_signature">
				</div>
			</div>

			<div class="col-md-12 form-group" hidden="hidden">

				<div class="col-sm-9 col-sm-offset-3">
					<input type="button" class="btn btn-sm" name="get_token"
						id="get_token" value="get Token">
				</div>
			</div>

			<div class="col-md-12 form-group" hidden="hidden">
				<label class="control-label col-sm-3" for="oauth_token_input">oauth_token_input:
				</label>
				<div class="col-sm-9">
					<input type="text" class="form-control" value=""
						name="oauth_token_input" id="oauth_token_input">
				</div>
			</div>

			<div class="col-md-12 form-group" hidden="hidden">
				<label class="control-label col-sm-3" for="oauth_token">oauth_token:
				</label>
				<div class="col-sm-9">
					<input type="text" class="form-control" value="" name="oauth_token"
						id="oauth_token">
				</div>
			</div>

			<div class="col-md-12 form-group" hidden="hidden">
				<label class="control-label col-sm-3" for="oauth_token_secret">oauth_token_secret:
				</label>
				<div class="col-sm-9">
					<input type="text" class="form-control" value=""
						name="oauth_token_secret" id="oauth_token_secret">
				</div>
			</div>
			<div class="col-md-12 form-group" >

				<div class="col-sm-9 col-sm-offset-3">

					<input type="button" class="btn btn-sm" value="Authorize me"
						name="authorize" id="authorize">
				</div>
			</div>


			<div class="col-md-12 form-group">
				<label class="control-label col-sm-3" for="oauth_token_verifier">oauth_token_verifier:
				</label>
				<div class="col-sm-9">
					<input type="text" class="form-control" value=""
						name="oauth_token_verifier" id="oauth_token_verifier">
				</div>
			</div>


			<div class="col-md-12 form-group">
				<label class="control-label col-sm-3" for="oauth_verifier">oauth_verifier:
				</label>
				<div class="col-sm-9">
					<input type="text" class="form-control" value=""
						name="oauth_verifier" id="oauth_verifier">
				</div>
			</div>

			<div class="col-md-12 form-group">
				<label class="control-label col-sm-3" for="validate">validate:
				</label>
				<div class="col-sm-9">
					<input type="button" class="btn btn-sm" value="Validate"
						name="validate" id="validate">
				</div>
			</div>

			<div class="col-md-12 form-group">
				<label class="control-label col-sm-3" for="verified_token">verified_token:
				</label>
				<div class="col-sm-9">
					<input type="text" class="form-control" id="verified_token"
						name="verified_token" value="">
				</div>
			</div>
		</div>

		<div class="col-lg-6 col-md-12">
			<div class="row col-md-12 id="verified_block" name="verified_block"></div>

			<button class="btn btn-sm" value="Get Timeline" id="timeline" name="timeline">Get
				Timeline</button>
			
			<button  class="btn btn-sm" value="Get Friends" id="friends" name="friends">Get
				Get Friends</button>
			
			<button class="btn btn-sm" value="Get Followers" id="followers" name="followers">Get
				Followers</button>
			
			<div class="col-md-12" id="api_result_block" name="api_result_block"></div>
		</div>
		</div>


	</div>

</body>
</html>