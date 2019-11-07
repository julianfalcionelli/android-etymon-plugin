/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.bluetooth.myBtDevices.exceptions;

import static co.bybardo.myapp.infrastructure.bluetooth.myBtDevices.MyBluetoothDevicesConstants.ERROR_AUTH_DENIED;
import static co.bybardo.myapp.infrastructure.bluetooth.myBtDevices.MyBluetoothDevicesConstants.ERROR_INSUFFICIENT_AUTH;
import static co.bybardo.myapp.infrastructure.bluetooth.myBtDevices.MyBluetoothDevicesConstants.ERROR_UNSUPPORTED_AUTH_HIGH_VERSION;
import static co.bybardo.myapp.infrastructure.bluetooth.myBtDevices.MyBluetoothDevicesConstants.ERROR_UNSUPPORTED_AUTH_VENDOR_VERSION;
import static co.bybardo.myapp.infrastructure.bluetooth.myBtDevices.MyBluetoothDevicesConstants.ERROR_UNSUPPORTED_AUTH_VERSION;

public class MyBluetoothDeviceException extends Exception {

    public MyBluetoothDeviceException(String message) {
        super(message);
    }

    public static class DeviceNotSupportedDeviceException extends MyBluetoothDeviceException {
        public DeviceNotSupportedDeviceException() {
            super("The selected device is not in the whitelist of supported devices.");
        }
    }

    public static class AuthDeniedDeviceException extends MyBluetoothDeviceException {
        public AuthDeniedDeviceException() {
            super("Authentication denied.");
        }
    }

    public static class InsufficientAuthDeviceException extends MyBluetoothDeviceException {
        public InsufficientAuthDeviceException() {
            super("Insufficient AUth");
        }
    }

    public static class UnsupportedAuthVersionDeviceException extends MyBluetoothDeviceException {
        public UnsupportedAuthVersionDeviceException() {
            super("Unsupported Auth Version.");
        }
    }

    public static class UnsupportedVendorVersionDeviceException extends MyBluetoothDeviceException {
        public UnsupportedVendorVersionDeviceException() {
            super("Unsupported Vendor Version.");
        }
    }

    public static class UnsupportedAuthHigherVersionDeviceException extends
            MyBluetoothDeviceException {
        public UnsupportedAuthHigherVersionDeviceException() {
            super("Unsupported Auth Higher Version.");
        }
    }

    public static class InvalidResponseDeviceException extends MyBluetoothDeviceException {
        public InvalidResponseDeviceException() {
            super("Invalid value.");
        }
    }

    public static class DeviceBusyDeviceException extends MyBluetoothDeviceException {
        public DeviceBusyDeviceException() {
            super("Device busy with a previous command.");
        }
    }

    public static MyBluetoothDeviceException getMyBluetoothDeviceException(int error) {
        if (error == ERROR_AUTH_DENIED) {
            return new AuthDeniedDeviceException();
        }

        if (error == ERROR_INSUFFICIENT_AUTH) {
            return new InsufficientAuthDeviceException();
        }

        if (error == ERROR_UNSUPPORTED_AUTH_VERSION) {
            return new UnsupportedAuthVersionDeviceException();
        }

        if (error == ERROR_UNSUPPORTED_AUTH_VENDOR_VERSION) {
            return new UnsupportedVendorVersionDeviceException();
        }

        if (error == ERROR_UNSUPPORTED_AUTH_HIGH_VERSION) {
            return new UnsupportedAuthHigherVersionDeviceException();
        }

        return new MyBluetoothDeviceException("Unknown BT Error Happened");
    }
}
