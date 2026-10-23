package com.demo.webtuyendung.enums;

public enum ContractType {
    LONG_TERM("Dài hạn"),
    TEMPORARY("Tạm thời");

    private String type;

    ContractType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return this.type;
    }

    public static String getTypeForUI(ContractType type) {
        return type.getType();
    }
}
