package com.asavin.hello.aspect;


import com.asavin.hello.annotation.FriendStatus;
import com.asavin.hello.entity.User;
import com.asavin.hello.repository.UserRepository;
import com.asavin.hello.service.JsonService;
import com.asavin.hello.service.UserService;
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
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

//@Aspect
//@ControllerAdvice(annotations = RestController.class)
//@Order(1)
public class UserDetailsAspect1 {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    UserService userService;
    @Autowired
    JsonService jsonService;
    @Autowired
    UserRepository userRepository;
    @Pointcut("@annotation(com.asavin.hello.annotation.FriendStatus)")
    public void friendStatusAnnotated(){}

    @Around("friendStatusAnnotated()")
    public Object foo(ProceedingJoinPoint pjp) throws Throwable {


        final User me = userService.getMe();
        Object res = pjp.proceed();
        ObjectNode jsonRes = (ObjectNode) objectMapper.readTree(objectMapper.writeValueAsString(res));
        System.out.println(jsonRes.get("friends"));
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        String[] fields =
                signature.getMethod().
                        getAnnotation(FriendStatus.class).
                        fields();

        jsonRes = (ObjectNode) friendStatus(jsonRes,me.getId());



//            for (int i = 0; i < properties.length; i++) {
//            switch (properties[i]) {
//                case friendStatus: {

//                    jsonRes = friendStatus(res,me.getId());

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
//                }
//            }
//        }
        return jsonRes;
    }

    private JsonNode friendStatus(JsonNode jsonRes, Long id) throws Throwable{


//        ObjectNode jsonRes = (ObjectNode) objectMapper.readTree(objectMapper.writeValueAsString(res));

        if (!jsonRes.isArray() && !(jsonRes instanceof ArrayNode)) {
            User other = objectMapper.treeToValue(jsonRes,User.class);

            Set<User> friends= userService.findUserById(other.getId()).getFriends();

            ((ObjectNode) jsonRes).putPOJO("friends",friends);

            ((ObjectNode) jsonRes).put("friendStatus", userService.friendStatus(id, other.getId()));

        }
        else if (jsonRes instanceof ArrayNode) {
//            Iterator<User> users = ((ArrayList<User>) res).iterator();
            jsonRes.forEach((node) -> {
                User other;
                try {
                    other = jsonService.fromJson(node, User.class);
                    ((ObjectNode) node).put("friendStatus",
                            userService.friendStatus(id, other.getId()));
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }

            });
        }
        return jsonRes;
    }

}

