package com.huynhntp.commons.wear2ndchange.common;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class UtilsService {

    public static String removeAccents(String input) {
        if (input == null) return null;

        // Chuẩn hóa chuỗi (NFD tách dấu)
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);

        // Loại bỏ các ký tự dấu (diacritics)
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String withoutAccents = pattern.matcher(normalized).replaceAll("");

        // Chuyển về chữ thường, loại bỏ ký tự đặc biệt nếu muốn
        return withoutAccents.replaceAll("đ", "d").replaceAll("Đ", "D");
    }

}
