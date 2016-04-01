package de.sg.tools.copydirectory;

import static de.sg.tools.copydirectory.HumanReadableFilesizeUtils.byteCountToDisplaySize;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Sita Geßner
 */
public final class CopyDirectoryUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(CopyDirectoryUtils.class);

    private static int countDirectory;

    private static int countFileSuccess;

    private static int countFileFail;

    private static int countWarnings;

    public static String copy(final String directoryFrom, final String directoryTo) {
        final File quelle = new File(directoryFrom);
        LOGGER.info("Starte Kopiervorgang");
        copyDirectory(quelle, new File(directoryTo));
        LOGGER.info(getSuccessMessage());
        return getSuccessMessageDisplay();
    }

    private static void copyDirectory(final File directoryFrom, final File directoryTo) {
        final File[] files = directoryFrom.listFiles();
        if (directoryTo.mkdir()) {
            LOGGER.info("Verzeichnis angelegt: {}", directoryTo);
            countDirectory++;
        }
        for (final File file : files) {
            if (file.isDirectory()) {
                copyDirectory(file, new File(directoryTo.getAbsolutePath() + System.getProperty("file.separator")
                        + file.getName()));
            } else {
                copyFile(file,
                        new File(directoryTo.getAbsolutePath() + System.getProperty("file.separator") + file.getName()));
            }
        }
    }

    private static void copyFile(final File directoryFrom, final File directoryTo) {
        try {
            Files.copy(directoryFrom.toPath(), directoryTo.toPath(), StandardCopyOption.REPLACE_EXISTING);
            final String sizeFrom = byteCountToDisplaySize(directoryFrom.length());
            LOGGER.info("Kopiervorgang von Datei {} (Dateigröße: {}) erfolgreich", directoryFrom, sizeFrom);
            final String sizeTo = byteCountToDisplaySize(directoryTo.length());
            if (!sizeFrom.equals(sizeTo)) {
                LOGGER.warn("Dateigrößen abweichend! Quelle: {}  Ziel: {}", sizeFrom, sizeTo);
                countWarnings++;
            }
            countFileSuccess++;
        } catch (final IOException e) {
            LOGGER.error("Kopieren von  '{}' nach '{}' ist fehlgeschlagen : {} {}", directoryFrom, directoryTo,
                    e.getMessage(), e);
            countFileFail++;
        }

    }

    private static String getSuccessMessage() {
        return "Kopiervorgang beendet Verzeichnisse: " + countDirectory + ", Dateien erfolgreich: " + countFileSuccess
                + ", Dateien nicht erfolgreich: " + countFileFail + ", Dateien mit abweichender Dateigröße: "
                + countWarnings;
    }

    private static String getSuccessMessageDisplay() {
        return "<html><body width='250'><h2>Kopiervorgang beendet</h2>Verzeichnisse angelegt: " + countDirectory
                + "<br>Dateien erfolgreich: " + countFileSuccess + "<br>Dateien nicht erfolgreich: " + countFileFail
                + "<br> Dateien mit abweichender Dateigröße: " + countWarnings + "</body></html>";
    }
}
