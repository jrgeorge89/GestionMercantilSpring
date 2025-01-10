
package com.gestion.mercantil.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatter {
    public static String convertDateFormat(Date date) {
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yy");
        String formattedDate = targetFormat.format(date);
        return formattedDate;
    }
}
