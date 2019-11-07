/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.bluetooth.base;

import android.Manifest;

public final class BluetoothConstants {

    public static final String TAG = "Bluetooth";

    private BluetoothConstants() {

    }

    public static final String UTF_8 = "UTF_8";

    public static final int GET_FIRST_8_BITS = 0xFF;

    public static final String[] REQUIRED_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
}
