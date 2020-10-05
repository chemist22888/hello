package com.asavin.hello.aspect;


import com.asavin.hello.entity.Post;
import com.asavin.hello.entity.User;
import com.asavin.hello.annotation.AdditionalProperties;
import com.asavin.hello.json.UserViewJson;
import com.asavin.hello.repository.UserRepository;
import com.asavin.hello.service.JsonService;
import com.asavin.hello.service.PostService;
import com.asavin.hello.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.function.Consumer;

@Aspect
@ControllerAdvice(annotations = RestController.class)
//@Order(1)
public class AdditionalPropertiesAspect {
    @Autowired
    UserService userService;
    @Autowired
    JsonService jsonService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    PostService postService;
    User me;
    @Pointcut("@annotation(com.asavin.hello.annotation.AdditionalProperties)")
    public void friendStatusAnnotated() {
    }

    @Around("friendStatusAnnotated()")
    public Object foo(ProceedingJoinPoint pjp) throws Throwable {
        me = userService.getMe();
        Object res = pjp.proceed();
        objectMapper = new ObjectMapper(objectMapper.
                writerWithView(UserViewJson.UserFullDetails.class).getFactory());

        JsonNode jsonRes = objectMapper.readTree(objectMapper.writeValueAsString(res));
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        AdditionalProperties.Properties[] properties =
                signature.getMethod().
                        getAnnotation(AdditionalProperties.class).
                        properties();

        for (int i = 0; i < properties.length; i++) {
            switch (properties[i]) {
                case friendStatus: {
                    jsonRes = ((ObjectNode)jsonRes).put("friendStatus",
                            userService.friendStatus(me.getId(), jsonRes.get("id").longValue()));

                    if (jsonRes.has("friends"))
                        jsonRes.get("friends").elements().forEachRemaining(friend -> {
                            ((ObjectNode) friend).put("friendStatus",
                                    userService.friendStatus(friend.get("id").longValue(), me.getId()));
                        });
                    break;
                }
                case onlineStatus: {
                    jsonRes = ((ObjectNode)jsonRes).put("onlineStatus",
                            userService.isOnline(new User(jsonRes.get("id").longValue())));
                    break;
                }
            }
        }
        return jsonRes;
    }

}

