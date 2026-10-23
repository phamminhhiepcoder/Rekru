package com.demo.webtuyendung.entity;

import com.demo.webtuyendung.enums.CVEvaluationStatus;
import com.demo.webtuyendung.enums.CandidateStatus;
import com.demo.webtuyendung.enums.Round;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Set;


//status: -1: đang đánh giá, 0: huỷ, 1: dc chọn, 2: gửi qua cho xem
@Entity
@Table(name = "candidates")
@Data
public class CandidateEntity extends AbstractEntity {
    @Column(name = "full_name")
    private String fullName;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String email;

    private String source;

    private String cvFile;
    private String introducer;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "round")
    private Round round;

    @Enumerated(EnumType.STRING)
    @Column(name = "evaluation_status")
    private CVEvaluationStatus CVEvaluationStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "candidate_status")
    private CandidateStatus candidateStatus;

    @ManyToOne
    @JoinColumn(name = "post_id")
    @EqualsAndHashCode.Exclude
    @JsonBackReference
    private PostEntity post;


    @Column(columnDefinition = "TEXT")
    private String notes;

    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonManagedReference
    private Set<CommentEntity> comments;

    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonManagedReference
    private Set<RejectEntity> rejects;


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCvFile() {
        return cvFile;
    }

    public void setCvFile(String cvFile) {
        this.cvFile = cvFile;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Round getRound() {
        return round;
    }

    public void setRound(Round round) {
        this.round = round;
    }

    public CVEvaluationStatus getEvaluationStatus() {
        return CVEvaluationStatus;
    }

    public void setEvaluationStatus(CVEvaluationStatus CVEvaluationStatus) {
        this.CVEvaluationStatus = CVEvaluationStatus;
    }

    public PostEntity getPost() {
        return post;
    }

    public void setPost(PostEntity post) {
        this.post = post;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public CVEvaluationStatus getCVEvaluationStatus() {
        return CVEvaluationStatus;
    }

    public void setCVEvaluationStatus(CVEvaluationStatus CVEvaluationStatus) {
        this.CVEvaluationStatus = CVEvaluationStatus;
    }

    public CandidateStatus getCandidateStatus() {
        return candidateStatus;
    }

    public void setCandidateStatus(CandidateStatus candidateStatus) {
        this.candidateStatus = candidateStatus;
    }


    public String getIntroducer() {
        return introducer;
    }

    public void setIntroducer(String introducer) {
        this.introducer = introducer;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Set<CommentEntity> getComments() {
        return comments;
    }

    public void setComments(Set<CommentEntity> comments) {
        this.comments = comments;
    }

    public Set<RejectEntity> getRejects() {
        return rejects;
    }

    public void setRejects(Set<RejectEntity> rejects) {
        this.rejects = rejects;
    }
}
