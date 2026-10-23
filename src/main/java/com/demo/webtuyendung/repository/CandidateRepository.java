package com.demo.webtuyendung.repository;

import com.demo.webtuyendung.entity.CandidateEntity;
import com.demo.webtuyendung.entity.UserEntity;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@SpringBootApplication
public interface CandidateRepository extends JpaRepository<CandidateEntity, Integer> {
    List<CandidateEntity> findAllByPostId(Integer id);
}
