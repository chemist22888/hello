package com.asavin.hello.json;

import com.asavin.hello.entity.Chat;
import com.asavin.hello.repository.ChatRepository;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype") // must not be a singleton component as it has state
public class JPAEntityResolver extends SimpleObjectIdResolver {
    //This would be JPA based object repository or you can EntityManager instance directly.
    private ChatRepository objectRepository;

    public JPAEntityResolver() {
    }

    @Autowired
    public JPAEntityResolver(ChatRepository objectRepository) {
        this.objectRepository = objectRepository;
    }

    @Override
    public void bindItem(ObjectIdGenerator.IdKey id, Object pojo) {
        super.bindItem(id, pojo);
    }

    @Override
    public Object resolveId(ObjectIdGenerator.IdKey id) {
        Object resolved = super.resolveId(id);
        if (resolved == null) {
            resolved = new Chat((Long) id.key);
            bindItem(id, resolved);
        }
        return resolved;
    }

    @Override
    public ObjectIdResolver newForDeserialization(Object context) {
        return new JPAEntityResolver(objectRepository);
    }


    public boolean canUseFor(ObjectIdResolver resolverType) {
        return resolverType.getClass() == JPAEntityResolver.class;
    }
}