package com.demo.webtuyendung.enums;

public enum Round {
    PRESCREEN("Sàng lọc hồ sơ"),
    PHONE_INTERVIEW("Phỏng vấn qua điện thoại"),
    HIRING_REVIEW("Đánh giá tuyển dụng"),
    ASSESSMENT_TEST("Bài kiểm tra đánh giá"),
    FIRST_INTERVIEW("Phỏng vấn vòng 1"),
    SECOND_INTERVIEW("Phỏng vấn vòng 2"),
    THIRD_INTERVIEW("Phỏng vấn vòng 3"),
    JOB_OFFER("Đề nghị tuyển dụng"),
    ONBOARDING("Hội nhập công ty");

    private String round;

    Round(String round) {
        this.round = round;
    }

    public String getStatus() {
        return round;
    }

    @Override
    public String toString() {
        return this.round;
    }

    public static String getStatusForUI(CandidateStatus status) {
        return status.getStatus();
    }

}
