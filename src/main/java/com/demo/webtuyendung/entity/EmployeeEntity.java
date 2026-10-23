package com.demo.webtuyendung.entity;

import com.demo.webtuyendung.enums.Position;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Set;

@Entity
@Table(name = "employees")
@Data
public class EmployeeEntity extends AbstractEntity {
    @Column(name = "full_name")
    private String fullName;

    private String gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "position")
    private Position position;

    @Basic
    @Column(name = "phone_number", nullable = true, length = 20)
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    @JsonBackReference
    private UserEntity user;

    private String department;

    @OneToMany(mappedBy = "requester", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonManagedReference
    private Set<RequestEntity> requestsByRequester;

    @OneToMany(mappedBy = "reviewer", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonManagedReference
    private Set<RequestEntity> requestsToInterview;

    @OneToMany(mappedBy = "reviewer", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonManagedReference
    private Set<CommentEntity> comments;

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Set<CommentEntity> getComments() {
        return comments;
    }

    public void setComments(Set<CommentEntity> comments) {
        this.comments = comments;
    }

    public Set<RequestEntity> getRequestsByRequester() {
        return requestsByRequester;
    }

    public void setRequestsByRequester(Set<RequestEntity> requestsByRequester) {
        this.requestsByRequester = requestsByRequester;
    }

    public Set<RequestEntity> getRequestsToInterview() {
        return requestsToInterview;
    }

    public void setRequestsToInterview(Set<RequestEntity> requestsToInterview) {
        this.requestsToInterview = requestsToInterview;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
