package de.sg.tools.copydirectory;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CopyDirectoryGUI {

    private static final String LABEL_QUELLVERZEICHNIS = "Quellverzeichnis";

    private static final String LABEL_ZIELVERZEICHNIS = "Zielverzeichnis";

    private final JTextField tfVezeichnisFrom = new JTextField(70);

    private final JTextField tfVerzeichnisTo = new JTextField(70);

    private final JCheckBox cbDissolveSubfolder = new JCheckBox();

    private final JCheckBox cbDeleteOnSuccess = new JCheckBox();

    private final JCheckBox cbIgnoreCorrputFiles = new JCheckBox();

    private JFrame frame;

    public JFrame createGUI() {
        frame = new JFrame("Verzeichnis kopieren © Sita Geßner");
        frame.setResizable(false);
        frame.setLayout(new FlowLayout(FlowLayout.LEFT));
        frame.setSize(800, 230);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final JPanel mainPane = new JPanel();
        mainPane.setLayout(new GridLayout(4, 1));
        mainPane.add(createTfPanel());
        mainPane.add(createCbPanel());
        mainPane.add(createBtnPanel());
        frame.add(mainPane);

        frame.setVisible(true);
        return frame;
    }

    private JPanel createTfPanel() {
        final JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));
        panel.add(new JLabel(LABEL_QUELLVERZEICHNIS));
        panel.add(tfVezeichnisFrom);

        panel.add(new JLabel(LABEL_ZIELVERZEICHNIS));
        panel.add(tfVerzeichnisTo);
        return panel;
    }

    private JPanel createCbPanel() {
        final JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        panel.add(new JLabel("Unterverzeichnisse auflösen"));
        panel.add(cbDissolveSubfolder);

        panel.add(new JLabel("Quellverzeichnisse/-dateien bei Erfolg löschen"));
        panel.add(cbDeleteOnSuccess);

        cbIgnoreCorrputFiles.setSelected(true);
        panel.add(new JLabel("Dateien mit abweichender Größe ignorieren"));
        panel.add(cbIgnoreCorrputFiles);
        return panel;
    }

    private JPanel createBtnPanel() {
        final JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout((FlowLayout.RIGHT)));
        final JButton btnOk = new JButton("OK");
        btnOk.addActionListener(actionlistenerBtnKopieren());
        panel.add(btnOk);
        return panel;
    }

    private ActionListener actionlistenerBtnKopieren() {
        return new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                if (validate()) {
                    final String message = CopyDirectoryUtils.copy(tfVezeichnisFrom.getText(), tfVerzeichnisTo.getText(),
                            cbDissolveSubfolder.isSelected(), cbDeleteOnSuccess.isSelected(), cbIgnoreCorrputFiles.isSelected());
                    JOptionPane.showMessageDialog(frame, message);
                }
            }

            private boolean validate() {
                return isValidDirectory(tfVezeichnisFrom.getText(), LABEL_QUELLVERZEICHNIS)
                        && isValidDirectory(tfVerzeichnisTo.getText(), LABEL_ZIELVERZEICHNIS);
            }

            private boolean isValidDirectory(final String directory, final String label) {
                boolean isValid = true;
                if (directory == null || directory.length() < 1) {
                    isValid = false;
                    JOptionPane.showMessageDialog(frame, "Bitte geben Sie ein " + label + " an.");
                } else if (!new File(directory).exists() || !new File(directory).canWrite()) {
                    isValid = false;
                    JOptionPane.showMessageDialog(frame, "Das angebene " + label + " '" + directory
                            + "' existiert nicht oder Sie besitzen nicht die notwendigen Rechte.");
                }
                return isValid;
            }

        };
    }
}
