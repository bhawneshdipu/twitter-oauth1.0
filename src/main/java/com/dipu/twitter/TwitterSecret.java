package com.dipu.twitter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component
public class TwitterSecret {
	@Value("${twitter.oauth_consumer_key}")
	public static String oauth_consumer_key;

	@Value("${twitter.oauth_consumer_key}")
	public void setKey(String db) {
		oauth_consumer_key = db;
	}

	
	@Value("${twitter.oauth_consumer_secret}")
	public static String oauth_consumer_secret;

	@Value("${twitter.oauth_consumer_secret}")
	public void setSecret(String db) {

		oauth_consumer_secret = db;
	}

	
}
