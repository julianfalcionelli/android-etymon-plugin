/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.bluetooth.base;

import static java.lang.annotation.RetentionPolicy.SOURCE;

import static co.bybardo.myapp.infrastructure.bluetooth.base.BluetoothDeviceState.CONNECTED;
import static co.bybardo.myapp.infrastructure.bluetooth.base.BluetoothDeviceState.DISCONNECTED;

import java.lang.annotation.Retention;

import androidx.annotation.IntDef;

@Retention(SOURCE)
@IntDef({CONNECTED, DISCONNECTED})
public @interface BluetoothDeviceState {
    int CONNECTED = 0;
    int DISCONNECTED = 1;
}
