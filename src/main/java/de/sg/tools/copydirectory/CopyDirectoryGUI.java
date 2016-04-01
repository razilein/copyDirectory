package de.sg.tools.copydirectory;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class CopyDirectoryGUI {

    private static final String LABEL_QUELLVERZEICHNIS = "Quellverzeichnis";

    private static final String LABEL_ZIELVERZEICHNIS = "Zielverzeichnis";

    private final JTextField tfVezeichnisFrom = new JTextField(70);

    private final JTextField tfVerzeichnisTo = new JTextField(70);

    private JFrame frame;

    public JFrame createGUI() {
        frame = new JFrame("Verzeichnis kopieren © Sita Geßner");
        frame.setResizable(false);
        frame.setLayout(new FlowLayout((FlowLayout.LEFT)));
        frame.setSize(800, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(new JLabel(LABEL_QUELLVERZEICHNIS));
        frame.add(tfVezeichnisFrom);

        frame.add(new JLabel(LABEL_ZIELVERZEICHNIS));
        frame.add(tfVerzeichnisTo);

        final JButton btnAuffrischen = new JButton("Kopieren");
        btnAuffrischen.addActionListener(actionlistenerBtnKopieren());
        frame.add(btnAuffrischen);

        frame.setVisible(true);
        return frame;
    }

    private ActionListener actionlistenerBtnKopieren() {
        return new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                if (validate()) {
                    final String message = CopyDirectoryUtils.copy(tfVezeichnisFrom.getText(),
                            tfVerzeichnisTo.getText());
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
