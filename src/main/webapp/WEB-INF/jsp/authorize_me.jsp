<%@ page import="java.io.*" %>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="javax.servlet.http.*" %>
<%@ page import="javax.servlet.*" %>
<%@ page import="javax.servlet.*" %>
<%@ page import="java.io.IOException"%>
<%@ page  import="okhttp3.*" %>

<%
String oauth_token=(String)request.getParameter("oauth_token");

String oauth_token_secret=(String)request.getParameter("oauth_token_secret");

OkHttpClient client = new OkHttpClient();
Request req = new Request.Builder()
  .url("https://api.twitter.com/oauth/authorize?"+oauth_token)
  .get()
  .build();

Response resp = client.newCall(req).execute();
out.print(resp.body().string());

%>

