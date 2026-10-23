package com.demo.webtuyendung.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "comments")
@Data
public class CommentEntity extends AbstractEntity {
    private String content;

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    @EqualsAndHashCode.Exclude
    @JsonBackReference
    private CandidateEntity candidate;

    @ManyToOne
    @JoinColumn(name = "reviewer_id")
    @EqualsAndHashCode.Exclude
    @JsonBackReference
    private EmployeeEntity reviewer;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public CandidateEntity getCandidate() {
        return candidate;
    }

    public void setCandidate(CandidateEntity candidate) {
        this.candidate = candidate;
    }

    public EmployeeEntity getReviewer() {
        return reviewer;
    }

    public void setReviewer(EmployeeEntity reviewer) {
        this.reviewer = reviewer;
    }
}
