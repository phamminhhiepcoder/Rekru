package com.demo.webtuyendung.enums;

public enum CVEvaluationStatus {
    IN_PROGRESS("Chờ đánh giá"),
    SUITABLE("Phù hợp"),
    UNSUITABLE("Không phù hợp");

    private String status;

    CVEvaluationStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return this.status;
    }

    public static String getStatusForUI(CVEvaluationStatus status) {
        return status.getStatus();
    }
}
