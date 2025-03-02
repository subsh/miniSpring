package com.minis.minispring.util;

public interface ObjectMapper {
    void setDateFormat(String dataFormat);
    void setDecimalFormat(String decimalFormat);
    String writeValuesAsString(Object obj);
}
