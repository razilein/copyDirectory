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

    private static int countDeletedDirectories;

    private static int countDeletedFiles;

    public static String copy(final String directoryFrom, final String directoryTo, final boolean dissolveSubfolder,
            final boolean deleteOnSuccess) {
        initCounter();
        final File source = new File(directoryFrom);
        final File target = new File(directoryTo);
        LOGGER.info("Starte Kopiervorgang");

        if (dissolveSubfolder) {
            copyDirectoryDissolveSubfolder(source, target, deleteOnSuccess);
        } else {
            copyDirectory(source, target, deleteOnSuccess);
        }

        LOGGER.info(getSuccessMessage());
        return getSuccessMessageDisplay();
    }

    private static void copyDirectory(final File directoryFrom, final File directoryTo, final boolean deleteOnSuccess) {
        final File[] files = directoryFrom.listFiles();
        if (directoryTo.mkdir()) {
            LOGGER.info("Verzeichnis angelegt: {}", directoryTo);
            countDirectory++;
        }

        for (final File file : files) {
            if (file.isDirectory()) {
                copyDirectory(file, new File(directoryTo.getAbsolutePath() + System.getProperty("file.separator")
                        + file.getName()), deleteOnSuccess);
                deleteDirectory(deleteOnSuccess, file);
            } else {
                copyFile(
                        file,
                        new File(directoryTo.getAbsolutePath() + System.getProperty("file.separator") + file.getName()),
                        deleteOnSuccess);
            }
        }
    }

    private static void copyFile(final File directoryFrom, final File directoryTo, final boolean deleteOnSuccess) {
        try {
            Files.copy(directoryFrom.toPath(), directoryTo.toPath(), StandardCopyOption.REPLACE_EXISTING);
            final String sizeFrom = byteCountToDisplaySize(directoryFrom.length());
            final String sizeTo = byteCountToDisplaySize(directoryTo.length());

            if (directoryTo.exists() && sizeFrom.equals(sizeTo)) {
                LOGGER.info("Kopiervorgang von Datei {} (Dateigröße: {}) erfolgreich", directoryFrom, sizeFrom);
                countFileSuccess++;
                deleteFile(directoryFrom, deleteOnSuccess);
            }
            if (!sizeFrom.equals(sizeTo)) {
                LOGGER.warn("Dateigrößen abweichend! Quelle: {}  Ziel: {}", sizeFrom, sizeTo);
                countWarnings++;
            }
        } catch (final IOException e) {
            LOGGER.error("Kopieren von  '{}' nach '{}' ist fehlgeschlagen : {} {}", directoryFrom, directoryTo,
                    e.getMessage(), e);
            countFileFail++;
        }
    }

    private static void deleteFile(final File directoryFrom, final boolean deleteOnSuccess) {
        if (deleteOnSuccess) {
            try {
                Files.deleteIfExists(directoryFrom.toPath());
                countDeletedFiles++;
            } catch (final Exception e) {
                LOGGER.warn("Datei {} konnte nicht gelöscht werden: {} {}", directoryFrom.getAbsolutePath(),
                        e.getMessage(), e);
            }
        }
    }

    private static void deleteDirectory(final boolean deleteOnSuccess, final File file) {
        if (deleteOnSuccess && file.listFiles().length == 0) {
            file.delete();
            countDeletedDirectories++;
        }
    }

    private static void copyDirectoryDissolveSubfolder(final File directoryFrom, final File directoryTo,
            final boolean deleteOnSuccess) {
        for (final File file : directoryFrom.listFiles()) {
            if (file.isDirectory()) {
                copyDirectoryDissolveSubfolder(file, directoryTo, deleteOnSuccess);
                deleteDirectory(deleteOnSuccess, file);
            } else {
                copyFile(
                        file,
                        new File(directoryTo.getAbsolutePath() + System.getProperty("file.separator") + file.getName()),
                        deleteOnSuccess);
            }
        }
    }

    private static String getSuccessMessage() {
        final StringBuilder message = new StringBuilder();
        message.append("Kopiervorgang beendet. Verzeichnisse angelegt: ");
        message.append(countDirectory);
        message.append(", Dateien erfolgreich: ");
        message.append(countFileSuccess);
        message.append(", Dateien nicht erfolgreich: ");
        message.append(countFileFail);
        message.append(", Dateien mit abweichender Dateigröße: ");
        message.append(countWarnings);
        message.append(", Dateien gelöscht: ");
        message.append(countDeletedFiles);
        message.append(", Verzeichnisse gelöscht: ");
        message.append(countDeletedDirectories);
        return message.toString();
    }

    private static String getSuccessMessageDisplay() {
        final StringBuilder message = new StringBuilder();
        message.append("<html><body width='250'><h2>Kopiervorgang beendet</h2>");
        message.append("<h3>Erfolgreich kopiert</h3>");
        message.append("Verzeichnisse: ");
        message.append("<font color='green'>");
        message.append(countDirectory);
        message.append("</font>");
        message.append("<br>Dateien: ");
        message.append("<font color='green'>");
        message.append(countFileSuccess);
        message.append("</font>");
        message.append("<h3>Warnungen/Fehler</h3>");
        message.append("Dateikopiervorgang nicht erfolgreich: ");
        if (countFileFail > 0) {
            message.append("<font color='red'>");
        } else {
            message.append("<font color='green'>");
        }
        message.append(countFileFail);
        message.append("</font>");
        message.append("<br>Dateien mit abweichender Dateigröße: ");
        if (countWarnings > 0) {
            message.append("<font color='orange'>");
        } else {
            message.append("<font color='green'>");
        }
        message.append(countWarnings);
        message.append("</font>");
        message.append("<h3>Löschungen</h3>");
        message.append("Dateien: ");
        message.append(countDeletedFiles);
        message.append("<br>Verzeichnisse: ");
        message.append(countDeletedDirectories);
        message.append("</body></html>");
        return message.toString();
    }

    private static void initCounter() {
        countDirectory = 0;
        countFileSuccess = 0;
        countFileFail = 0;
        countWarnings = 0;
        countDeletedDirectories = 0;
        countDeletedFiles = 0;
    }

}
