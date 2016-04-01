package de.sg.tools.copydirectory;

import java.math.BigDecimal;

/**
 * @author Sita Geßner
 * @since 7.5.0
 */
public final class HumanReadableFilesizeUtils {

    public static final long _1_KB = 1024;

    public static final long _1_MB = _1_KB * _1_KB;

    public static final long _1_GB = _1_KB * _1_MB;

    public static final long _1_TB = _1_KB * _1_GB;

    public static final BigDecimal ONE_TB = new BigDecimal(_1_TB);

    public static final BigDecimal ONE_GB = new BigDecimal(_1_GB);

    public static final BigDecimal ONE_MB = new BigDecimal(_1_MB);

    public static final BigDecimal ONE_KB = new BigDecimal(_1_KB);

    private HumanReadableFilesizeUtils() {
    }

    public static String byteCountToDisplaySize(final Long size) {
        return byteCountToDisplaySize(new BigDecimal(size));
    }

    public static String byteCountToDisplaySize(final BigDecimal size) {
        final String displaySize;

        if (size.compareTo(ONE_TB) > 0) {
            displaySize = String.valueOf(size.divide(ONE_TB).setScale(2, BigDecimal.ROUND_CEILING)) + " TB";
        } else if (size.compareTo(ONE_GB) > 0) {
            displaySize = String.valueOf(size.divide(ONE_GB).setScale(2, BigDecimal.ROUND_CEILING)) + " GB";
        } else if (size.compareTo(ONE_MB) > 0) {
            displaySize = String.valueOf(size.divide(ONE_MB).setScale(2, BigDecimal.ROUND_CEILING)) + " MB";
        } else if (size.compareTo(ONE_KB) > 0) {
            displaySize = String.valueOf(size.divide(ONE_KB).setScale(2, BigDecimal.ROUND_CEILING)) + " KB";
        } else {
            displaySize = String.valueOf(size) + " bytes";
        }
        return displaySize;
    }

}
