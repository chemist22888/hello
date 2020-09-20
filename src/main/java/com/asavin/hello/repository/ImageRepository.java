package com.asavin.hello.repository;

import com.asavin.hello.entity.Chat;
import com.asavin.hello.entity.Coment;
import com.asavin.hello.entity.Image;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image,Long> {
    @Query("select i from Image i where i.id < :id")
    List<Image> getImageWhereIdLess(@Param("id")Long id, Pageable pageable);
    @Query("select i from Image i where i.id < :id")
    List<Image> getImageWhereIdLess(@Param("id")Long id);

    default List<Image> getUserWhereIdLessLimit(Long id,int quantity) {
        if(id!=-1 && quantity!=-1)
            return getImageWhereIdLess(id, PageRequest.of(0,quantity));
        else if(id!=-1 && quantity == -1)
            return getImageWhereIdLess(id);
        else if(id==-1 && quantity != -1)
            return findAll(PageRequest.of(0,quantity)).getContent();
        else
            return findAll();
    }
    @Query("select i from Image i order by i.id desc ")
    List<Image>findAllDesc();
    @Query("select i from Image i order by i.id desc ")
    List<Image>findAllDesc(Pageable pageable);
    Image findByName(String name);
}
