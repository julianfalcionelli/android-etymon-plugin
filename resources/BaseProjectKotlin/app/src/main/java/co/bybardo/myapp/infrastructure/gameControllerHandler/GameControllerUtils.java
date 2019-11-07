/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.gameControllerHandler;

import android.os.Handler;
import android.view.InputDevice;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameControllerUtils {
    public static final String TAG = GameControllerUtils.class.getSimpleName();

    // Can be PlayStation Controller Name
    public static List<String> GAME_CONTROLLER_NAMES = Arrays.asList("Wireless Controller");

    public static List<Integer> GAME_CONTROLLER_SOURCES = Arrays.asList(InputDevice.SOURCE_GAMEPAD,
            InputDevice.SOURCE_JOYSTICK);

    public static void addGameController(String name) {
        GAME_CONTROLLER_NAMES.add(name);
    }

    public static void addGameController(int source) {
        GAME_CONTROLLER_SOURCES.add(source);
    }

    public static ArrayList getGameControllerIds() {
        ArrayList gameControllerDeviceIds = new ArrayList();
        int[] deviceIds = getInputDeviceIds();

        for (int deviceId : deviceIds) {
            if (isGameController(getDevice(deviceId)) && !gameControllerDeviceIds.contains(
                    deviceId)) {
                gameControllerDeviceIds.add(deviceId);
            }
        }

        return gameControllerDeviceIds;
    }

    public static int[] getInputDeviceIds() {
        return InputDevice.getDeviceIds();
    }

    public static InputDevice getDevice(int deviceId) {
        return InputDevice.getDevice(deviceId);
    }

    public static boolean isGameController(InputDevice device) {
        return GAME_CONTROLLER_SOURCES.contains(device.getSources())
                || GAME_CONTROLLER_NAMES.contains(device.getName());
    }

    public static boolean isGameControllerConnected() {
        return !getGameControllerIds().isEmpty();
    }

    private static float getCenteredAxis(MotionEvent event, int axis) {

        if (event.getDevice() != null) {
            final InputDevice.MotionRange range =
                    event.getDevice().getMotionRange(axis, event.getSource());

            // A joystick at rest does not always report an absolute position of
            // (0,0). Use the getFlat() method to determine the range of values
            // bounding the joystick axis center.
            if (range != null) {
                final float flat = range.getFlat();
                final float value = event.getAxisValue(axis);

                // Ignore axis values that are within the 'flat' region of the
                // joystick axis center.
                if (Math.abs(value) > flat) {
                    return value;
                }
            }
        }

        return 0;
    }

    public static void initializeControllerSupport(final IGameControllerListener listener) {
        if (GameControllerUtils.isGameControllerConnected()) {
            new Handler().postDelayed(listener::onInitialFocusRequested, 200);
        }
    }

    //Return true if we handler the input event
    public static boolean handlerInputEvent(InputEvent event, IGameControllerListener listener) {
        if (GameControllerUtils.isGameControllerConnected()) {
            if (listener.checkInitialState()) {
                listener.onInitialFocusRequested();
                return true;
            }

            Key key = Key.fromInputEvent(event);
            return listener.onKeyPressed(key);
        }

        return false;
    }

    public enum Key {
        KEY_UNKNOWN(null, "UNKNOWN"),
        KEY_Y(KeyEvent.KEYCODE_BUTTON_Y, "Y"),
        KEY_B(KeyEvent.KEYCODE_BUTTON_B, "B"),
        KEY_Z(KeyEvent.KEYCODE_BUTTON_Z, "Z"),
        KEY_A(KeyEvent.KEYCODE_BUTTON_A, "A"),
        KEY_X(KeyEvent.KEYCODE_BUTTON_X, "X"),
        KEY_C(KeyEvent.KEYCODE_BUTTON_C, "C"),
        KEY_R1(KeyEvent.KEYCODE_BUTTON_R1, "R1"),
        KEY_R2(KeyEvent.KEYCODE_BUTTON_R2, "R2"),
        KEY_L1(KeyEvent.KEYCODE_BUTTON_L1, "L1"),
        KEY_L2(KeyEvent.KEYCODE_BUTTON_L2, "L2"),
        KEY_ARROW_UP(null, "ARROW UP"),
        KEY_ARROW_DOWN(null, "ARROW DOWN"),
        KEY_ARROW_LEFT(null, "ARROW LEFT"),
        KEY_ARROW_RIGHT(null, "ARROW RIGHT"),
        KEY_LS_UP(null, "LEFT-STICK UP"),
        KEY_LS_DOWN(null, "LEFT-STICK DOWN"),
        KEY_LS_LEFT(null, "LEFT-STICK LEFT"),
        KEY_LS_RIGHT(null, "LEFT-STICK RIGHT"),
        KEY_RS_UP(null, "RIGHT-STICK UP"),
        KEY_RS_DOWN(null, "RIGHT-STICK DOWN"),
        KEY_RS_LEFT(null, "RIGHT-STICK LEFT"),
        KEY_RS_RIGHT(null, "RIGHT-STICK RIGHT");

        private Integer mKeyCode;
        private String mKeyName;

        Key(Integer keyCode, String keyName) {
            mKeyCode = keyCode;
            mKeyName = keyName;
        }

        public static Key fromKeyCode(int keyCode) {
            for (Key key : Key.values()) {
                if (key.getKeyCode() != null &&
                        key.getKeyCode() == keyCode) {
                    return key;
                }
            }

            return KEY_UNKNOWN;
        }

        public static Key fromInputEvent(InputEvent event) {

            // If the input event is a MotionEvent, check its hat axis values.
            if (event instanceof MotionEvent) {
                // Use the hat axis value to find the D-pad direction
                MotionEvent motionEvent = (MotionEvent) event;

                //CHECK X AXIS

                float xAxis = getCenteredAxis(motionEvent, MotionEvent.AXIS_HAT_X); //Arrows

                if (xAxis != 0) {
                    if (Float.compare(xAxis, -1.0f) == 0) {
                        return Key.KEY_ARROW_LEFT;
                    } else if (Float.compare(xAxis, 1.0f) == 0) {
                        return Key.KEY_ARROW_RIGHT;
                    }
                }

                xAxis = getCenteredAxis(motionEvent, MotionEvent.AXIS_X); //Left Stick

                if (xAxis != 0) {
                    if (Float.compare(xAxis, -1.0f) == 0) {
                        return Key.KEY_LS_LEFT;
                    } else if (Float.compare(xAxis, 1.0f) == 0) {
                        return Key.KEY_LS_RIGHT;
                    }
                }

                xAxis = getCenteredAxis(motionEvent, MotionEvent.AXIS_Z); //Right Stick

                if (xAxis != 0) {
                    if (Float.compare(xAxis, -1.0f) == 0) {
                        return Key.KEY_RS_LEFT;
                    } else if (Float.compare(xAxis, 1.0f) == 0) {
                        return Key.KEY_RS_RIGHT;
                    }
                }

                //CHECK Y AXIS

                float yAxis = getCenteredAxis(motionEvent, MotionEvent.AXIS_HAT_Y); //Arrows

                if (yAxis != 0) {
                    if (Float.compare(yAxis, -1.0f) == 0) {
                        return Key.KEY_ARROW_UP;
                    } else if (Float.compare(yAxis, 1.0f) == 0) {
                        return Key.KEY_ARROW_DOWN;
                    }
                }

                yAxis = getCenteredAxis(motionEvent, MotionEvent.AXIS_Y); //Left Stick

                if (yAxis != 0) {
                    if (Float.compare(yAxis, -1.0f) == 0) {
                        return Key.KEY_LS_UP;
                    } else if (Float.compare(yAxis, 1.0f) == 0) {
                        return Key.KEY_LS_DOWN;
                    }
                }

                yAxis = getCenteredAxis(motionEvent, MotionEvent.AXIS_RZ); //Right Stick

                if (yAxis != 0) {
                    if (Float.compare(yAxis, -1.0f) == 0) {
                        return Key.KEY_RS_UP;
                    } else if (Float.compare(yAxis, 1.0f) == 0) {
                        return Key.KEY_RS_DOWN;
                    }
                }
            }
            // If the input event is a KeyEvent, check its key code.
            else if (event instanceof KeyEvent) {

                KeyEvent keyEvent = (KeyEvent) event;

                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    return Key.fromKeyCode(keyEvent.getKeyCode());
                }
            }

            return Key.KEY_UNKNOWN;
        }

        public Integer getKeyCode() {
            return mKeyCode;
        }

        @Override
        public String toString() {
            return mKeyName;
        }
    }

    public interface IGameControllerListener {
        //Return true if need to re-set the initial focus
        boolean checkInitialState();

        void onInitialFocusRequested();

        //Return true if we handler the input event
        boolean onKeyPressed(Key key);
    }
}
