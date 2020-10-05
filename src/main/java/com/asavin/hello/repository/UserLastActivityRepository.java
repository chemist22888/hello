package com.asavin.hello.repository;

import com.asavin.hello.entity.User;
import com.asavin.hello.entity.UserLastActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserLastActivityRepository {}