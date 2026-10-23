package com.demo.webtuyendung.entity;

import com.demo.webtuyendung.enums.InterviewType;
import com.demo.webtuyendung.enums.Round;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Data
public class AppointmentEntity extends AbstractEntity {
    @Column(name = "start_date")
    protected LocalDateTime appointmentDate;

    private String location;

    private String noteToCancel;

    @Enumerated(EnumType.STRING)
    @Column(name = "round")
    private Round round;

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    @EqualsAndHashCode.Exclude
    @JsonBackReference
    private CandidateEntity candidate;

    @ManyToOne
    @JoinColumn(name = "interviewer_id")
    @EqualsAndHashCode.Exclude
    @JsonBackReference
    private EmployeeEntity interviewer;

    @Enumerated(EnumType.STRING)
    @Column(name = "interview_type")
    private InterviewType interviewType;


    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDateTime appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNoteToCancel() {
        return noteToCancel;
    }

    public void setNoteToCancel(String noteToCancel) {
        this.noteToCancel = noteToCancel;
    }

    public CandidateEntity getCandidate() {
        return candidate;
    }

    public void setCandidate(CandidateEntity candidate) {
        this.candidate = candidate;
    }

    public EmployeeEntity getInterviewer() {
        return interviewer;
    }

    public void setInterviewer(EmployeeEntity interviewer) {
        this.interviewer = interviewer;
    }

    public InterviewType getInterviewType() {
        return interviewType;
    }

    public void setInterviewType(InterviewType interviewType) {
        this.interviewType = interviewType;
    }

    public Round getRound() {
        return round;
    }

    public void setRound(Round round) {
        this.round = round;
    }
}
