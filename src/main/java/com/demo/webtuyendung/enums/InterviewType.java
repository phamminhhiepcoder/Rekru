package com.demo.webtuyendung.enums;

public enum InterviewType {
    IN_PERSON("Phỏng vấn trực tiếp"),    // Phỏng vấn trực tiếp
    PHONE("Phỏng vấn qua điện thoại"),            // Phỏng vấn qua điện thoại
    ONLINE("Phỏng vấn trực tuyến");          // Phỏng vấn trực tuyến

    private String type;

    InterviewType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return this.type;
    }

    public static String getTypeForUI(InterviewType type) {
        return type.getType();
    }
}
