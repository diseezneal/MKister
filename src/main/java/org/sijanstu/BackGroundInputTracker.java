package org.sijanstu;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author sijanbhandari
 */


public class BackGroundInputTracker {

    static NativeKeyListener nativeKeyListener;

    //for key mapping
    static boolean isMetaPressed = false;
    public static boolean isEnabled = true;

    //for settings
    static boolean isMeta2Pressed = false;
    static boolean isShiftPressed = false;
    static int downCount = 0;
    static int upCount = 0;

    public static void startKeyTracking() throws NativeHookException {
        //if already started stop it
        if (nativeKeyListener != null) {
            GlobalScreen.removeNativeKeyListener(nativeKeyListener);
        }
        GlobalScreen.registerNativeHook();
        nativeKeyListener = new NativeKeyListener() {
            @Override
            public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {
            }

            @Override
            public void nativeKeyPressed(NativeKeyEvent nke) {
                String key = NativeKeyEvent.getKeyText(nke.getKeyCode());
                UI.lastPressed.setText(key);
                if (key.equals("Meta") || key.equals("⌘")) {
                    isMeta2Pressed = true;
                }
                if (key.equals("⌃") || key.equals("Control")) {
                    isShiftPressed = true;
                    System.out.println("Down");
                }
                if (key.equals("Down") || key.equals("↓")) {
                    downCount++;
                }
                if (key.equals("Up") || key.equals("↑")) {
                    upCount++;
                }

                if (isMeta2Pressed && isShiftPressed && downCount == 2) {
                    System.out.println("Settings");
                    isEnabled = !isEnabled;
                    UI.instance.isON(!UI.instance.toggler.isSelected());
                    downCount = 0;
                    upCount = 0;
                    isMeta2Pressed = false;
                    isShiftPressed = false;
                    System.out.println("Pressing Meta+Shift+Down twice disables the shortcut");
                }

                if (isMeta2Pressed && isShiftPressed && upCount == 2) {
                    UI.instance.setVisible(!UI.instance.isVisible());
                    if (UI.instance.isVisible()) {
                        UI.instance.setAlwaysOnTop(true);
                    }
                    downCount = 0;
                    upCount = 0;
                    isMeta2Pressed = false;
                    isShiftPressed = false;
                    System.out.println("Pressing Meta+Shift+Up twice hides the UI");
                }

                if (isEnabled) {
                    if (key.equals("Meta") || key.equals("⌘")) {
                        if (!isMetaPressed) {
                            isMetaPressed = true;
                            //System.out.println("Meta pressed");
                        } else {
                            isMetaPressed = false;
                            //System.out.println("Meta released");
                        }
                    } else {
                        if (isMetaPressed) {
                            //check if key is left or right
                            if (key.equals("Left") || key.equals("←")) {
                                //System.out.println("Pressed Left");
                                ProcessBuilder pb = new ProcessBuilder("osascript", "-e", "tell application \"System Events\" to key code 50 using {command down}");
                                try {
                                    pb.start();
                                    System.out.println("Switched to left window");
                                } catch (Exception e) {
                                    Logger.getLogger(BackGroundInputTracker.class.getName()).log(Level.SEVERE, null, e);
                                    System.out.println("Error in executing command" + e.getMessage());
                                    System.out.println("Error in executing command" + e.getLocalizedMessage());
                                }
                            }
                            if (key.equals("Right") || key.equals("→")) {
                                //System.out.println("Pressed Right");
                                //press Meta+Shift+`
                                ProcessBuilder pb = new ProcessBuilder("osascript", "-e", "tell application \"System Events\" to key code 50 using {command down, shift down}");
                                try {
                                    pb.start();
                                    System.out.println("Switched to right window");
                                } catch (Exception e) {
                                    // write into log file
                                    Logger.getLogger(BackGroundInputTracker.class.getName()).log(Level.SEVERE, null, e);
                                    System.out.println("Error in executing command" + e.getMessage());
                                    System.out.println("Error in executing command" + e.getLocalizedMessage());
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {
                String key = NativeKeyEvent.getKeyText(nativeKeyEvent.getKeyCode());
                System.out.println("Key Released: " + key);
                if (key.equals("Meta") || key.equals("⌘")) {
                    // System.out.println("Released ⌘)");
                    isMetaPressed = false;
                    isMeta2Pressed = false;
                    downCount = 0;
                    upCount = 0;
                }
                if (key.equals("Shift") || key.equals("⇧")) {
                    // System.out.println("Released Shift");
                    isShiftPressed = false;
                    downCount = 0;
                    upCount = 0;
                }
            }

        };
        GlobalScreen.addNativeKeyListener(nativeKeyListener);
    }

    public static void main(String[] args) {
        try {
            BackGroundInputTracker.startKeyTracking();
        } catch (NativeHookException e) {
            System.out.println("error" + e.getMessage());
            System.out.println("Error in starting key tracking");
        }
    }
}
