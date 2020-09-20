package com.asavin.hello;

import com.asavin.hello.entity.Post;
import com.asavin.hello.entity.User;
import com.asavin.hello.json.UserViewJson;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.IOException;

@SpringBootApplication
@EnableAuthorizationServer
@EnableResourceServer
public class HelloApplication extends SpringBootServletInitializer  {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(HelloApplication.class, args);

//		String a = "{\"text\":\"d\",\"images\":[3]}";
//		System.out.println(		new ObjectMapper().readValue(a,Post.class).getImages().get(0).getId());
//		new ObjectMapper().readValue(a,Post.class).getImages().get(0).getId();
	}

}

