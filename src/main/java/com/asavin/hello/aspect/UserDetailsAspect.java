package com.asavin.hello.aspect;


import com.asavin.hello.entity.User;
import com.asavin.hello.annotation.AdditionalProperties;
import com.asavin.hello.json.UserViewJson;
import com.asavin.hello.repository.UserRepository;
import com.asavin.hello.service.JsonService;
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
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;

@Aspect
@ControllerAdvice(annotations = RestController.class)
//@Order(1)
public class UserDetailsAspect {
    @Autowired
    UserService userService;
    @Autowired
    JsonService jsonService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ObjectMapper objectMapper;
    @Pointcut("@annotation(com.asavin.hello.annotation.AdditionalProperties)")
    public void friendStatusAnnotated(){}

    @Around("friendStatusAnnotated()")
    public Object foo(ProceedingJoinPoint pjp) throws Throwable {


        final User me = userService.getMe();
        Object res = pjp.proceed();
        System.out.println(res.getClass().getName());
        objectMapper = new ObjectMapper(objectMapper.
                writerWithView(UserViewJson.UserFullDetails.class).getFactory());

        ObjectNode jsonRes = (ObjectNode) objectMapper.readTree(objectMapper.writeValueAsString(res));
        System.out.println(jsonRes);
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        AdditionalProperties.Properties[] properties =
                signature.getMethod().
                        getAnnotation(AdditionalProperties.class).
                        properties();


            for (int i = 0; i < properties.length; i++) {
                switch (properties[i]) {
                    case friendStatus: {
                        System.out.println(res.getClass().getDeclaredFields()[0].
                                        getAnnotatedType().getType().getTypeName());
                        jsonRes = jsonRes.put("friendStatus",
                            userService.friendStatus(me.getId(),jsonRes.get("id").longValue()));

                        if(jsonRes.has("friends"))
                            jsonRes.get("friends").elements().forEachRemaining(friend -> {
                                ((ObjectNode)friend).put("friendStatus",
                                        userService.friendStatus(friend.get("id").longValue(),me.getId()));
                            });
    //                    jsonRes.
    //                    System.out.println(jsonRes.getClass().getName());
    //                    if (!jsonRes.isArray() && res instanceof User) {
    //                        User other = objectMapper.treeToValue(jsonRes,User.class);
    //
    //                        Set<User> friends= userService.findUserById(other.getId()).getFriends();
    //
    //                        jsonRes.putPOJO("friends",friends);
    ////                        me = userService.findUserById(me.getId());
    //                        System.out.println("oth"+other.getUsername());
    //                        System.out.println("me"+userRepository.getOne(1l).getUsername());
    ////                        if(other.getFriends() != null)
    //                        ( jsonRes).put("friendStatus", userService.friendStatus(me.getId(), other.getId()));
    //
    //                    }
    //                    else if (res instanceof Collection) {
    //                        Iterator<User> users = ((ArrayList<User>) res).iterator();
    //                        jsonRes.forEach((node) -> {
    //                            User other = users.next();
    //                            try {
    //                                other = jsonService.fromJson(node, User.class);
    //                                System.out.println(other.getUsername());
    //                                System.out.println(me.getUsername());
    //                            } catch (Throwable throwable) {
    //                                throwable.printStackTrace();
    //                            }
    //                            ((ObjectNode) node).put("friendStatus",
    //                                    userService.friendStatus(me.getId(), other.getId()));
    //                        });
    //                    }
                        break;
                    }
                    case onlineStatus:{
                        jsonRes = jsonRes.put("onlineStatus",
                                userService.isOnline(new User(jsonRes.get("id").longValue())));
                        break;
                    }
                }
        }
        return jsonRes;
    }



    private JsonNode friendStatus(JsonNode jsonNode,Long id) throws Throwable{











//        ObjectNode jsonRes = (ObjectNode) objectMapper.readTree(objectMapper.writeValueAsString(res));

        if (!jsonNode.isArray()) {
            User other = objectMapper.treeToValue(jsonNode,User.class);

            Set<User> friends= userService.findUserById(other.getId()).getFriends();

//            jsonRes.putPOJO("friends",friends);

            ((ObjectNode) jsonNode).put("friendStatus", userService.friendStatus(id, other.getId()));

        }
        else  {
//            Iterator<User> users = ((ArrayList<User>) res).iterator();
//            jsonRes.forEach((node) -> {
//                User other = users.next();
//                try {
//                    other = jsonService.fromJson(node, User.class);
//                } catch (Throwable throwable) {
//                    throwable.printStackTrace();
//                }
//                ((ObjectNode) node).put("friendStatus",
//                        userService.friendStatus(id, other.getId()));
//            });
        }
        return jsonNode;
    }

}

