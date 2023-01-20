package org.sijanstu;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author sijanbhandari
 */
public class MKister {
    public static void main(String[] args) {
        new Thread(() -> {
            try {
                Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
                logger.setLevel(Level.OFF);
                BackGroundInputTracker.startKeyTracking();
            } catch (NativeHookException ex) {
                //JOptionPane.showMessageDialog(null, "Error in starting key tracking");
            }
        }).start();
        UI.main(args);
    }
}
