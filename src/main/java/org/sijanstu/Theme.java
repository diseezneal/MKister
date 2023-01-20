package org.sijanstu;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Theme {

    public static void setTheme() {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
        FlatLaf.updateUI();
    }

}

