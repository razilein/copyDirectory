package de.sg.tools.copydirectory;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.StringJoiner;

/**
 * @author Sita Geßner
 */
public final class FilesizeUtils {

    public static final long _1_KB = 1024;

    public static final long _1_MB = _1_KB * _1_KB;

    public static final long _1_GB = _1_KB * _1_MB;

    public static final long _1_TB = _1_KB * _1_GB;

    public static final BigDecimal ONE_TB = new BigDecimal(_1_TB);

    public static final BigDecimal ONE_GB = new BigDecimal(_1_GB);

    public static final BigDecimal ONE_MB = new BigDecimal(_1_MB);

    public static final BigDecimal ONE_KB = new BigDecimal(_1_KB);

    private static final DecimalFormat DF = (DecimalFormat) NumberFormat.getNumberInstance(Locale.GERMAN);

    private FilesizeUtils() {
    }

    public static String byteCountToDisplaySize(final Long size) {
        return byteCountToDisplaySize(new BigDecimal(size));
    }

    public static String byteCountToDisplaySize(final BigDecimal size) {
        final BigDecimal displaySize;
        final String displayUnit;

        if (size.compareTo(ONE_TB) > 0) {
            displaySize = size.divide(ONE_TB).setScale(2, BigDecimal.ROUND_CEILING);
            displayUnit = "TB";
        } else if (size.compareTo(ONE_GB) > 0) {
            displaySize = size.divide(ONE_GB).setScale(2, BigDecimal.ROUND_CEILING);
            displayUnit = "GB";
        } else if (size.compareTo(ONE_MB) > 0) {
            displaySize = size.divide(ONE_MB).setScale(2, BigDecimal.ROUND_CEILING);
            displayUnit = "MB";
        } else if (size.compareTo(ONE_KB) > 0) {
            displaySize = size.divide(ONE_KB).setScale(2, BigDecimal.ROUND_CEILING);
            displayUnit = "KB";
        } else {
            displaySize = size;
            displayUnit = "bytes";
        }
        return joinDisplaySizeAndUnit(displaySize, displayUnit);
    }

    private static String joinDisplaySizeAndUnit(final BigDecimal displaySize, final String displayUnit) {
        final StringJoiner joiner = new StringJoiner(" ");
        joiner.add(DF.format(displaySize));
        joiner.add(displayUnit);
        return joiner.toString();
    }

}
