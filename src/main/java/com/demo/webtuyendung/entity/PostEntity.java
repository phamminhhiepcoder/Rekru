package com.demo.webtuyendung.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
//import org.bouncycastle.cert.ocsp.Req;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
@Data
public class PostEntity extends AbstractEntity {
    @Column(columnDefinition = "TEXT")
    private String welfare;

    private String inChargePerson;
    private String workTime;

    private Integer showPost;

    private String postSource;

    @Column(name = "start_date")
    protected LocalDate startDate;

    @Column(name = "end_date")
    protected LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "request_id")
    @EqualsAndHashCode.Exclude
    @JsonBackReference
    private RequestEntity request;

    @ManyToOne
    @JoinColumn(name = "hr_id")
    @EqualsAndHashCode.Exclude
    @JsonBackReference
    private EmployeeEntity creator;

    public String getWelfare() {
        return welfare;
    }

    public void setWelfare(String welfare) {
        this.welfare = welfare;
    }

    public Integer getShowPost() {
        return showPost;
    }

    public void setShowPost(Integer showPost) {
        this.showPost = showPost;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public RequestEntity getRequest() {
        return request;
    }

    public void setRequest(RequestEntity request) {
        this.request = request;
    }

    public String getPostSource() {
        return postSource;
    }

    public void setPostSource(String postSource) {
        this.postSource = postSource;
    }

    public EmployeeEntity getCreator() {
        return creator;
    }

    public void setCreator(EmployeeEntity creator) {
        this.creator = creator;
    }


    public String getInChargePerson() {
        return inChargePerson;
    }

    public void setInChargePerson(String inChargePerson) {
        this.inChargePerson = inChargePerson;
    }

    public String getWorkTime() {
        return workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }
}
