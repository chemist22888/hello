package com.asavin.hello.repository;

import com.asavin.hello.entity.Chat;
import com.asavin.hello.entity.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends CrudRepository<Chat,Long> {
    @Query(value = "select c from Chat c where :id1 member of c.users and :id2" +
            " member of c.users and c.name=null order by c.users.size  ",countQuery = "select count(u) from User1 u")
    List<Chat> findDilogs(@Param("id1") User id1, @Param("id2") User id2, Pageable pageable);
    default Chat findDilog(User id1, User id2) {
        List<Chat> chats = findDilogs(id1,id2,  PageRequest.of(0,1));

        return  (chats.size()>0)?chats.get(0):null;
    }

}
