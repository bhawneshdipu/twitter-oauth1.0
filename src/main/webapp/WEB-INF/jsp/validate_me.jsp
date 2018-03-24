<%@ page import="java.io.*" %>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="javax.servlet.http.*" %>
<%@ page import="javax.servlet.*" %>
<%@ page import="javax.servlet.*" %>
<%@ page import="java.io.IOException"%>
<%@ page  import="okhttp3.*" %>

<%
//+"&oauth_token="+oauth_token
String oauth_verifier=(String)request.getParameter("oauth_verifier");
String oauth_token=(String)request.getParameter("oauth_token");

RequestBody formBody = new FormBody.Builder()
.add("oauth_verifier", oauth_verifier)
.add("oauth_token",oauth_token)
.build();

OkHttpClient client = new OkHttpClient();
Request req = new Request.Builder()
  .url("https://api.twitter.com/oauth/access_token?oauth_token="+oauth_token+"&oauth_verifier="+oauth_verifier)
  .get()
  .build();

Response resp = client.newCall(req).execute();
out.print(resp.body().string());

%>
