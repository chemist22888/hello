package com.asavin.hello.repository;

import com.asavin.hello.entity.Chat;
import com.asavin.hello.entity.Coment;
import com.asavin.hello.entity.Post;
import com.asavin.hello.entity.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComentRepository extends JpaRepository<Coment,Long> {
    List<Coment> findByUser(User user);

    @Query("select c from Coment c where c.id < :id")
    List<Coment> getComentWhereIdLess(@Param("id")Long id, Pageable pageable);
    @Query("select c from Coment c where c.id < :id")
    List<Coment> getComentWhereIdLess(@Param("id")Long id);
    @Query("select c from Coment c order by c.id desc ")
    List<Coment> findAllDesc();

    @Query("select c from Coment c order by c.id desc ")
    List<Coment> findAllDesc(Pageable pageable);
    default List<Coment> getUserWhereIdLessLimit(Long id,int quantity) {
        if(id!=-1 && quantity!=-1)
            return getComentWhereIdLess(id, PageRequest.of(0,quantity));
        else if(id!=-1 && quantity == -1)
            return getComentWhereIdLess(id);
        else if(id==-1 && quantity != -1)
            return findAll(PageRequest.of(0,quantity)).getContent();
        else
            return findAll();
    }
}
