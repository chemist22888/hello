package com.asavin.hello;

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
//		A a = new A(5,new A(45));
//
		ObjectMapper om = new ObjectMapper();

		String json = "{\"id\":8,\"username\":\"superuser\",\"wall\":{\"id\":1,\"posts\":[{\"id\":1,\"text\":\"logins post\",\"coments\":[{\"id\":1,\"user\":{\"id\":8,\"username\":\"superuser\"},\"text\":\"hi mark\"}],\"creationDate\":{\"epochSecond\":1554048000,\"nano\":0}},{\"id\":3,\"text\":\"post2\",\"coments\":[],\"creationDate\":{\"epochSecond\":1554220800,\"nano\":0}},{\"id\":4,\"text\":\"from postmaaao\",\"coments\":[],\"creationDate\":{\"epochSecond\":1554307200,\"nano\":0}},{\"id\":5,\"text\":\"1в\",\"coments\":[],\"creationDate\":{\"epochSecond\":1554393600,\"nano\":0}},{\"id\":6,\"text\":\"2w\",\"coments\":[],\"creationDate\":{\"epochSecond\":1554480000,\"nano\":0}},{\"id\":12,\"text\":\"Новый пост 11/04\",\"coments\":[],\"creationDate\":{\"epochSecond\":1586579184,\"nano\":0}}]},\"friends\":[{\"id\":1,\"username\":\"login1\"},{\"id\":8,\"username\":\"superuser\"}]}\n";
		System.out.println(om.readTree(json).get("id"));

	}

}

