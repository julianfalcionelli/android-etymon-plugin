/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.bluetooth.myBtDevices.devices.myDevice;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface MyBluetoothDeviceManager {
    // Operations
    Single<String> getFirmwareVersion();

    Completable wakeUp();
}
