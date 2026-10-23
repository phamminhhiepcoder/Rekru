package com.demo.webtuyendung.entity;

import com.demo.webtuyendung.enums.InterviewType;
import com.demo.webtuyendung.enums.Round;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Entity
@Table(name = "emails")
@Data
public class EmailEntity extends AbstractEntity {
    protected LocalDateTime dateToSend;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String mailToSend;

    @ManyToOne
    @JoinColumn(name = "appointment_id")
    @EqualsAndHashCode.Exclude
    @JsonBackReference
    private AppointmentEntity appointment;

    public LocalDateTime getDateToSend() {
        return dateToSend;
    }

    public void setDateToSend(LocalDateTime dateToSend) {
        this.dateToSend = dateToSend;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMailToSend() {
        return mailToSend;
    }

    public void setMailToSend(String mailToSend) {
        this.mailToSend = mailToSend;
    }

    public AppointmentEntity getAppointment() {
        return appointment;
    }

    public void setAppointment(AppointmentEntity appointment) {
        this.appointment = appointment;
    }
}
