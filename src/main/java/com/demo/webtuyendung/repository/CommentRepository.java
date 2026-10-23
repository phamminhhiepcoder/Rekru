package com.demo.webtuyendung.repository;

import com.demo.webtuyendung.entity.CommentEntity;
import com.demo.webtuyendung.entity.UserEntity;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@SpringBootApplication
public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {
    List<CommentEntity> findAllByCandidateId(Integer id);
}
