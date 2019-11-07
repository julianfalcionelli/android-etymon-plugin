/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.bluetooth.base.model;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

public class BleResult {
    private int status;
    private BluetoothGattCharacteristic bluetoothGattCharacteristic;
    private  BluetoothGattDescriptor bluetoothGattDescriptor;

    public BleResult(int status) {
        this.status = status;
    }

    public BleResult(int status,
            BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        this(status);
        this.bluetoothGattCharacteristic = bluetoothGattCharacteristic;
    }

    public BleResult(int status, BluetoothGattDescriptor bluetoothGattDescriptor) {
        this(status);
        this.bluetoothGattDescriptor = bluetoothGattDescriptor;
    }

    public int getStatus() {
        return status;
    }

    public boolean hasBtInfo() {
        return bluetoothGattCharacteristic != null || bluetoothGattDescriptor != null;
    }

    public BluetoothGattCharacteristic getBluetoothGattCharacteristic() {
        return bluetoothGattCharacteristic;
    }

    public BluetoothGattDescriptor getBluetoothGattDescriptor() {
        return bluetoothGattDescriptor;
    }
}
