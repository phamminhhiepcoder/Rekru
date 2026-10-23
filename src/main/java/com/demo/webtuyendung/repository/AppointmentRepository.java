package com.demo.webtuyendung.repository;

import com.demo.webtuyendung.entity.AppointmentEntity;
import com.demo.webtuyendung.entity.CommentEntity;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@SpringBootApplication
public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Integer> {
}
