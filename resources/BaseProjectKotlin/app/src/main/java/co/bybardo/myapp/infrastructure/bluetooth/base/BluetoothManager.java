/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.bluetooth.base;

import android.bluetooth.BluetoothDevice;

import java.util.List;
import java.util.UUID;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * BLE Generic Operations:
 * - Turn On / Off
 * - Discover Devices
 * - Pairing / Unpairing
 *
 * Once you got your device paired you must use a BluetoothDeviceManager Instance to perform I/O
 * events.
 */
public interface BluetoothManager {
    // Status
    Single<Integer> getBluetoothStatus();

    Observable<Integer> observeBluetoothStatus();

    // Turn On / Off
    Completable turnOn();

    Completable turnOff();

    // Devices
    Single<List<BluetoothDevice>> getPairedDevices();

    Single<Boolean> isDevicePaired(String deviceMacAddress);

    // Discovery
    Completable startDiscovery();

    Completable stopDiscovery();

    Completable stopDiscoveryAndGetNotified();

    Observable<BluetoothDevice> observeBluetoothDiscovery();

    // Actions
    Completable pairDevice(BluetoothDevice bluetoothDevice);

    void unpair(String deviceMacAddress);

    Completable discoverAndPairDevice(String deviceMacAddress);

    Single<List<UUID>> getBluetoothDeviceServices(BluetoothDevice bluetoothDevice,
            boolean continueWithDiscovery);

    // Stop / Clear states
    void stop();
}
