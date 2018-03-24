<%@ page import="java.io.*" %>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="javax.servlet.http.*" %>
<%@ page import="javax.servlet.*" %>
<%@ page import="javax.servlet.*" %>
<%@ page import="java.io.IOException"%>
<%@ page  import="okhttp3.*" %>

<%@ page import="java.net.*"%>
<%@ page import="sun.misc.*"%>
<%@ page import="javax.crypto.*"%>

<%@ page import="javax.crypto.spec.*"%>
<%@ page import="org.apache.commons.codec.binary.Base64"%>


<%!


 public  String genNonce(int len)
  {
      // Pick from some letters that won't be easily mistaken for each
      // other. So, for example, omit o O and 0, 1 l and L.
      String letters = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ";

      String pw = "";
      for (int i=0; i<len; i++)
      {
          int index = (int)(new Random().nextDouble()*letters.length());
          pw += letters.substring(index, index+1);
      }
      return pw;
  }%>
  
<%!

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



%>

<%

String burl="https://api.twitter.com/1.1/friends/list.json";
	String consumer_key = (String)request.getParameter("oauth_consumer_key");;
	String consumer_secret =(String)request.getParameter("oauth_consumer_secret");
	String token = (String)request.getParameter("oauth_token");
	String token_secret = (String)request.getParameter("oauth_token_secret");
	String signature_method = "HMAC-SHA1";
	Timestamp ts = new Timestamp(System.currentTimeMillis());
	long oauth_timestamp = ts.getTime() / 1000;
	String timestamp = Long.toString(oauth_timestamp);
	String nonce = genNonce(6);
	String screen_name=(String)request.getParameter("screen_name");
	String user_id=(String)request.getParameter("user_id");
	
	// System.out.println("Nonce = " + nonce + "\nNonce Length" +
	// nonce.length());

	// https://api.twitter.com/oauth/authorize?oauth_token=86327591-MUuvybGO3g2BvirO3sWSzz3Xda4teSu1wX2ruxbCb

	// parameter string
	String parameter_string = URLEncoder.encode("oauth_consumer_key", "UTF-8") + "="
			+ URLEncoder.encode(consumer_key, "UTF-8") + "&"
			+ URLEncoder.encode("oauth_nonce", "UTF-8") + "=" + URLEncoder.encode(nonce, "UTF-8") + "&"
			+ URLEncoder.encode("oauth_signature_method", "UTF-8") + "=" + URLEncoder.encode("HMAC-SHA1", "UTF-8")
			+ "&" + URLEncoder.encode("oauth_timestamp", "UTF-8") + "=" + URLEncoder.encode(timestamp, "UTF-8")
			+ "&" + URLEncoder.encode("oauth_token", "UTF-8") + "="
			+ URLEncoder.encode(token, "UTF-8") + "&"
			+ URLEncoder.encode("oauth_version", "UTF-8") + "=" + URLEncoder.encode("1.0", "UTF-8");

	// System.out.println("Parameter String = " + parameter_string + "\n");

	// creating signature
	String signature_base_string = "GET&"
			+ URLEncoder.encode(burl, "UTF-8") + "&"
			+ URLEncoder.encode(parameter_string, "UTF-8");
	// System.out.println("signature_base_string = " + signature_base_string
	// + "\n\n");

	String oauth_signature = "";
	oauth_signature = computeSignature(signature_base_string, consumer_secret, token_secret);
	String signature = URLEncoder.encode(oauth_signature, "UTF-8");
	// System.out.println(signature + "\n\n");

	// Authorization Header
	String authHeader = "OAuth oauth_consumer_key=\"" + consumer_key + "\"," + "oauth_token=\"" + token + "\","
			+ "oauth_signature_method=\"HMAC-SHA1\"," + "oauth_timestamp=\"" + timestamp + "\"," + "oauth_nonce=\""
			+ nonce + "\"," + "oauth_version=\"1.0\"," + "oauth_signature=\"" + signature + "\"";



OkHttpClient client = new OkHttpClient();

okhttp3.Request req = new okhttp3.Request.Builder()
   .url("https://api.twitter.com/1.1/friends/list.json")
   .get()
   .addHeader("authorization", authHeader)
   .addHeader("screen_name", screen_name)
   .addHeader("user_id", user_id)
   
   .build();

Response resp = client.newCall(req).execute();
String res=resp.body().string();
out.print(res);




%>

