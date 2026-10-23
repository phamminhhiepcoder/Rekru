package com.demo.webtuyendung.entity;

import com.demo.webtuyendung.enums.ContractType;
import com.demo.webtuyendung.enums.RequestStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Entity
@Table(name = "requests")
@Data
public class RequestEntity extends AbstractEntity {
    private String department;
    private Integer quantity;

    private String position;

    @Column(columnDefinition = "TEXT")
    private String jobDescription;

    private String target; // ngoài ngân sách...

    private String location; // onl, off

    @Column(columnDefinition = "TEXT")
    private String explanation; // giải trình

    @Column(columnDefinition = "TEXT")
    private String standard; // tiêu chuẩn

    @Column(name = "start_date")
    protected LocalDate startDate;

    @Column(name = "end_date")
    protected LocalDate endDate;

    private String confirmation; // giấy xác nhận

    private Integer months;

    private String reason; // thay thế, tuyển mới
    private String requestType; // không/có thời hạn

    @Enumerated(EnumType.STRING)
    @Column(name = "request_status")
    private RequestStatus requestStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "contract_type")
    private ContractType contractType;

    @ManyToOne
    @JoinColumn(name = "requester_id")
    @EqualsAndHashCode.Exclude
    @JsonBackReference
    private EmployeeEntity requester;

    @ManyToOne
    @JoinColumn(name = "reviewer_id")
    @EqualsAndHashCode.Exclude
    @JsonBackReference
    private EmployeeEntity reviewer;

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
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

    public EmployeeEntity getRequester() {
        return requester;
    }

    public void setRequester(EmployeeEntity requester) {
        this.requester = requester;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public EmployeeEntity getReviewer() {
        return reviewer;
    }

    public void setReviewer(EmployeeEntity reviewer) {
        this.reviewer = reviewer;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(String confirmation) {
        this.confirmation = confirmation;
    }


    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getMonths() {
        return months;
    }

    public void setMonths(Integer months) {
        this.months = months;
    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ContractType getContractType() {
        return contractType;
    }

    public void setContractType(ContractType contractType) {
        this.contractType = contractType;
    }
}
