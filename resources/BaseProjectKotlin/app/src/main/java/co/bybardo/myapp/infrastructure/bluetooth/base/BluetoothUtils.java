/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.bluetooth.base;


import static co.bybardo.myapp.infrastructure.bluetooth.base.BluetoothConstants.GET_FIRST_8_BITS;

import android.bluetooth.BluetoothDevice;
import android.os.ParcelUuid;
import android.util.Base64;
import android.util.Log;

public final class BluetoothUtils {
    public static final String TAG = "BluetoothUtils";
    private static final String LOG_SEPARATOR = "===========================================";
    private static final String LOG_NEW_LINE = "\n";
    private static final String LOG_ITEM_SEPARATOR = "----------";

    private BluetoothUtils() {

    }

    public static void dumpBluetoothDevice(BluetoothDevice bluetoothDevice) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(LOG_SEPARATOR);
        stringBuilder.append("Dumping Bluetooth Device Data");
        stringBuilder.append(LOG_NEW_LINE);
        stringBuilder.append("Mac Address: " + bluetoothDevice.getAddress());
        stringBuilder.append(LOG_NEW_LINE);
        stringBuilder.append("Name: " + bluetoothDevice.getName());
        stringBuilder.append(LOG_NEW_LINE);
        stringBuilder.append(LOG_ITEM_SEPARATOR);
        stringBuilder.append(LOG_NEW_LINE);

        stringBuilder.append("List Of Services: ");
        stringBuilder.append(LOG_NEW_LINE);
        stringBuilder.append(LOG_NEW_LINE);

        ParcelUuid[] uuids = bluetoothDevice.getUuids();

        if (uuids != null) {
            for (ParcelUuid parcelUuid : bluetoothDevice.getUuids()) {
                stringBuilder.append("Service: " + parcelUuid.getUuid().toString());
                stringBuilder.append(LOG_NEW_LINE);
            }
        }

        stringBuilder.append(LOG_NEW_LINE);
        stringBuilder.append(LOG_SEPARATOR);
        stringBuilder.append(LOG_NEW_LINE);

        Log.d(TAG, stringBuilder.toString());
    }

    public static byte[] hexToBytes(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }

        return data;
    }

    private final static char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte getByteOfPart(int value, int part) {
        if (part == 0) {
            throw new RuntimeException("Min part of a byte is 1 (first 8 bits)");
        }

        return (byte) ((part == 1 ? value : value >> (part - 1) * 8) & GET_FIRST_8_BITS);
    }

    public static byte[] base64ToUtf8(String value) {
        return Base64.decode(value, Base64.DEFAULT);

        /*
        try {
            return new String(value.getBytes(), BluetoothConstants.UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }*/
    }

    public static byte[] utf8ToBase64(String value) {
        return Base64.decode(value, Base64.DEFAULT);
    }
}
