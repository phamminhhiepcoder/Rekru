package com.demo.webtuyendung.enums;

public enum Position {
    HEAD_OF_DEPARTMENT("Trưởng phòng"),
    DEPUTY("Phó phòng"),
    EMPLOYEE("Nhân viên");

    private String positionName;

    Position(String positionName) {
        this.positionName = positionName;
    }

    public String getPositionName() {
        return positionName;
    }

    @Override
    public String toString() {
        return this.positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public static Position fromPositionName(String positionName) {
        for (Position position : Position.values()) {
            if (position.getPositionName().equalsIgnoreCase(positionName)) {
                return position;
            }
        }
        throw new IllegalArgumentException("Không tìm thấy vị trí: " + positionName);
    }
}
