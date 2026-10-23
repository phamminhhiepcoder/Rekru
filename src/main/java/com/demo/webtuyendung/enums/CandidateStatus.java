package com.demo.webtuyendung.enums;

public enum CandidateStatus {
    IN_REVIEW("Đang đánh giá"),
    FIT("Phù hợp"),
    NOT_FIT("Không phù hợp"),
    REJECTED("Bị từ chối"),
    DECLINED("Từ chối hoặc rút hồ sơ"),
    SUITABLE_FOR_OTHER_POSITION("Phù hợp với vị trí khác"),
    SELECTED("Đã chọn");

    private String status;

    CandidateStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return this.status;
    }

    public static String getStatusForUI(CandidateStatus status) {
        return status.getStatus();
    }
}
