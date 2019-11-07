/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.bluetooth.base;

import static java.lang.annotation.RetentionPolicy.SOURCE;

import static co.bybardo.myapp.infrastructure.bluetooth.base.BluetoothState.BLUETOOTH_OFF;
import static co.bybardo.myapp.infrastructure.bluetooth.base.BluetoothState.BLUETOOTH_ON;

import java.lang.annotation.Retention;

import androidx.annotation.IntDef;

@Retention(SOURCE)
@IntDef({BLUETOOTH_ON, BLUETOOTH_OFF})
public @interface BluetoothState {
    int BLUETOOTH_ON = 0;
    int BLUETOOTH_OFF = 1;
}
