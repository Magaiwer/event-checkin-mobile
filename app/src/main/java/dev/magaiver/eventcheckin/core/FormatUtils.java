package dev.magaiver.eventcheckin.core;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Date;

import static java.text.NumberFormat.getCurrencyInstance;
import static java.text.NumberFormat.getNumberInstance;

public class FormatUtils {

    private static final DateFormat DATE_INSTANCE = DateFormat.getDateInstance(DateFormat.SHORT);

    public static String dateLocalFormat(Date date) {
        return DATE_INSTANCE.format(date);
    }

    public static String decimalFormat(BigDecimal value) {
        return getCurrencyInstance().format(value);
    }

    public static String numberFormat(BigDecimal value) {
        return getNumberInstance().format(value);
    }

}
