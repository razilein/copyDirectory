package de.sg.computerinsel.tools.copydirectory;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

import de.sg.computerinsel.tools.copydirectory.FilesizeUtils;

/**
 * @author Sita Geßner
 */
public class FilesizeUtilsTest {

    @Test
    public void testByteCountToDisplaySize() {
        assertEquals("8 bytes", FilesizeUtils.byteCountToDisplaySize(8L));
        assertEquals("2,5 KB", FilesizeUtils.byteCountToDisplaySize(2560L));
        assertEquals("3,78 KB", FilesizeUtils.byteCountToDisplaySize(3870L));
        assertEquals("10 MB", FilesizeUtils.byteCountToDisplaySize(FilesizeUtils.ONE_MB.multiply(BigDecimal.TEN)));
        assertEquals("10 TB", FilesizeUtils.byteCountToDisplaySize(FilesizeUtils.ONE_TB.multiply(BigDecimal.TEN)));
    }

}
