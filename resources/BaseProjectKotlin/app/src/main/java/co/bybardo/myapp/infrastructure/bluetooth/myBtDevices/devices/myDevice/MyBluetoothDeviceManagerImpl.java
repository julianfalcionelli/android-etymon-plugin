/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.bluetooth.myBtDevices.devices.myDevice;


import static co.bybardo.myapp.infrastructure.bluetooth.myBtDevices.MyBluetoothDevicesConstants.COMMANDS_INITIAL_DELAY_MILLIS;
import static co.bybardo.myapp.infrastructure.bluetooth.myBtDevices.MyBluetoothDevicesConstants.FIRMWARE_VERSION_COMMAND_BYTES;
import static co.bybardo.myapp.infrastructure.bluetooth.myBtDevices.MyBluetoothDevicesConstants.UNKONW_ERROR;
import static co.bybardo.myapp.infrastructure.bluetooth.myBtDevices.MyBluetoothDevicesConstants.UUID_AUTH_CHARACTERISTIC;
import static co.bybardo.myapp.infrastructure.bluetooth.myBtDevices.MyBluetoothDevicesConstants.UUID_AUTH_SERVICE;
import static co.bybardo.myapp.infrastructure.bluetooth.myBtDevices.MyBluetoothDevicesConstants.UUID_COMMAND_CHARACTERISTIC;
import static co.bybardo.myapp.infrastructure.bluetooth.myBtDevices.MyBluetoothDevicesConstants.UUID_COMMAND_SERVICE;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.util.Log;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import co.bybardo.myapp.infrastructure.bluetooth.base.BluetoothDeviceManager;
import co.bybardo.myapp.infrastructure.bluetooth.base.BluetoothManager;
import co.bybardo.myapp.infrastructure.bluetooth.base.model.BleResult;
import co.bybardo.myapp.infrastructure.bluetooth.myBtDevices.exceptions.MyBluetoothDeviceException;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;

@SuppressLint("LogNotTimber")
public class MyBluetoothDeviceManagerImpl extends BluetoothDeviceManager implements
        MyBluetoothDeviceManager {

    public MyBluetoothDeviceManagerImpl(BluetoothManager bluetoothManager, Context context,
            String deviceMacAddress) {
        super(bluetoothManager, context, deviceMacAddress);
    }

    protected Single<BleResult> writeAuth(byte[] data) {
        return Completable.timer(COMMANDS_INITIAL_DELAY_MILLIS, TimeUnit.MILLISECONDS)
                .andThen(write(UUID_AUTH_SERVICE, UUID_AUTH_CHARACTERISTIC, data, false))
                .flatMap((Function<BleResult, SingleSource<BleResult>>)
                        bleResult -> checkForErrors(bleResult, true))
                .onErrorResumeNext(error -> {
                    if (error instanceof MyBluetoothDeviceException.DeviceBusyDeviceException) {
                        return Completable.timer(COMMANDS_INITIAL_DELAY_MILLIS,
                                TimeUnit.MILLISECONDS)
                                .andThen(readCommand(data)); // Retry
                    } else {
                        return Single.error(error);
                    }
                });
    }

    protected Single<BleResult> writeCommand(byte[] data) {
        return Completable.timer(COMMANDS_INITIAL_DELAY_MILLIS, TimeUnit.MILLISECONDS)
                .andThen(write(UUID_COMMAND_SERVICE, UUID_COMMAND_CHARACTERISTIC, data, false))
                .flatMap((Function<BleResult, SingleSource<BleResult>>)
                        bleResult -> checkForErrors(bleResult, true))
                .onErrorResumeNext(error -> {
                    if (error instanceof MyBluetoothDeviceException.DeviceBusyDeviceException) {
                        return Completable.timer(COMMANDS_INITIAL_DELAY_MILLIS,
                                TimeUnit.MILLISECONDS)
                                .andThen(readCommand(data)); // Retry
                    } else {
                        return Single.error(error);
                    }
                });
    }

    protected Single<BleResult> readCommand(byte[] data) {
        return Completable.timer(COMMANDS_INITIAL_DELAY_MILLIS, TimeUnit.MILLISECONDS)
                .andThen(read(UUID_COMMAND_SERVICE, UUID_COMMAND_CHARACTERISTIC, data, false))
                .flatMap((Function<BleResult, SingleSource<BleResult>>)
                        bleResult -> checkForErrors(bleResult, false))
                .onErrorResumeNext(error -> {
                    if (error instanceof MyBluetoothDeviceException.DeviceBusyDeviceException) {
                        return Completable.timer(COMMANDS_INITIAL_DELAY_MILLIS,
                                TimeUnit.MILLISECONDS)
                                .andThen(readCommand(data)); // Retry
                    } else {
                        return Single.error(error);
                    }
                });
    }

    // This method checks if there is any error in the Bluetooth response and if is the case
    // wrap it to an Exception
    private Single<BleResult> checkForErrors(
            BleResult bleResult, boolean isWrite) {
        if (bleResult.getStatus() != BluetoothGatt.GATT_SUCCESS) {
            if (isWrite && bleResult.getStatus() == UNKONW_ERROR) { // TODO HOT-FIX FOR Code 136
                return Single.just(bleResult); // TODO: THIS IS TEMPORAL, WE ARE IGNORING THIS ERROR
            } else {
                return Single.error(
                        MyBluetoothDeviceException.getMyBluetoothDeviceException(
                                bleResult.getStatus()));
            }
        } else {
            BluetoothGattCharacteristic bluetoothGattCharacteristic =
                    bleResult.getBluetoothGattCharacteristic();

            if (bluetoothGattCharacteristic != null &&
                    bluetoothGattCharacteristic.getStringValue(0).isEmpty()) {
                Log.w(TAG, "MyDeviceBluetoothException.DeviceBusyDeviceException");
                return Single.error(new MyBluetoothDeviceException.DeviceBusyDeviceException());
            } else {
                return Single.just(bleResult);
            }
        }
    }

    private Completable checkValue(BleResult bleResult, String expectedValue) {
        if (bleResult.getBluetoothGattCharacteristic() != null &&
                bleResult.getBluetoothGattCharacteristic()
                        .getStringValue(0).equals(expectedValue)) {
            return Completable.complete();
        } else {
            return Completable.error(new MyBluetoothDeviceException.InvalidResponseDeviceException());
        }
    }

    @Override
    public Single<String> getFirmwareVersion() {
        return runSequenceOfCommands(writeCommand(FIRMWARE_VERSION_COMMAND_BYTES)
                .flatMap(__ -> readCommand(FIRMWARE_VERSION_COMMAND_BYTES))
                .flatMap(
                        bleResult ->
                        {
                            String result =
                                    bleResult.getBluetoothGattCharacteristic()
                                            .getStringValue(0);
                            Log.d(TAG, "Firmware version: " + result);

                            List<String> values = Arrays.asList(result.split(","));

                            if (!result.isEmpty()) {
                                return Single.just(values.get(2));
                            } else {
                                return Single.error(
                                        new MyBluetoothDeviceException.InvalidResponseDeviceException());
                            }
                        }

                ));
    }

    @Override
    public Completable wakeUp() {
        return runSequenceOfCommands(checkConnection());
    }

}
