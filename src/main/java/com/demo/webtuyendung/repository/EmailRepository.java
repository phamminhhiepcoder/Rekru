package com.demo.webtuyendung.repository;

import com.demo.webtuyendung.entity.EmailEntity;
import com.demo.webtuyendung.entity.PostEntity;
import com.demo.webtuyendung.entity.RequestEntity;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@SpringBootApplication
public interface EmailRepository extends JpaRepository<EmailEntity, Integer> {
}

