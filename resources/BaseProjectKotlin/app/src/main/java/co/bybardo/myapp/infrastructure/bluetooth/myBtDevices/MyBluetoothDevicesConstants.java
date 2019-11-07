/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.bluetooth.myBtDevices;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public final class MyBluetoothDevicesConstants {

    private MyBluetoothDevicesConstants() {

    }

    public static final int COMMANDS_INITIAL_DELAY_MILLIS = 1000;

    public static final byte MAX_BYTE = 127;
    public static final byte MIN_BYTE = -127;

    public static final List<String> DEVICES_SUPPORTED = Arrays.asList("Astra AT");

    private static final String MY_OWN_DEVICE_SERVICE = "52401523-f97c-7f90-0e7f-6c6f4e36db1c";
    private static final String WRITE_READ_ID = "52401524-f97c-7f90-0e7f-6c6f4e36db1c";

    public static final String AUTH_SERVICE = "c051f05a-d56d-4bb8-a37e-4a6313538e09";
    public static final String AUTH_CHARACTERISTIC = "b0e137bb-a556-4cec-af37-14cdb5ab485f";

    public static final String COMMAND_SERVICE = "a8e5ade2-e448-4d4d-8b4b-183dc69f8fbe";
    public static final String COMMAND_CHARACTERISTIC = "7581429e-1950-4d66-a7dd-985b0ebc9320";

    public static final UUID UUID_AUTH_SERVICE = UUID.fromString(AUTH_SERVICE);
    public static final UUID UUID_AUTH_CHARACTERISTIC = UUID.fromString(AUTH_CHARACTERISTIC);

    public static final UUID UUID_COMMAND_SERVICE = UUID.fromString(COMMAND_SERVICE);
    public static final UUID UUID_COMMAND_CHARACTERISTIC = UUID.fromString(COMMAND_CHARACTERISTIC);

    // RSDKs
    /**
     * Values in Hex (32 bits)
     */
    public static final String RSDK_FULL_PACKAGE_1 = "a5b6010000010203040506070809101112131415";
    public static final String RSDK_FULL_PACKAGE_2 = "a5b6010116171819202122232425262728293031";

    public static final String RSDK_VALUE_1 = "00010203040506070809101112131415";
    public static final String RSDK_VALUE_2 = "16171819202122232425262728293031";

    // Auth Package
    public static final int VENDOR_ID = 0xa5b6;
    public static final int AUTH_VERSION = 0x01;
    public static final int SEQUENCE_NUMBER_1 = 0x00;
    public static final int SEQUENCE_NUMBER_2 = 0x01;

    // Auth Data
    public static final int AUTH_SESSION_DURATION_MILLIS = 60 * 60 * 1000;

    // Responses
    public static final int AUTH_SUCCESS = 0;

    // Errors
    public static final int ERROR_INSUFFICIENT_AUTH = 0x08; // 8
    public static final int ERROR_AUTH_DENIED = 0x80; // 128
    public static final int ERROR_UNSUPPORTED_AUTH_VENDOR_VERSION = 0x81; // 129
    public static final int ERROR_UNSUPPORTED_AUTH_VERSION = 0x82; // 130
    public static final int ERROR_UNSUPPORTED_AUTH_HIGH_VERSION = 0x83; // 131
    public static final int ERROR_UNSUPPORTED_AUTH_PROTOCOL_VERSION = 0x84; // 132
    public static final int UNKONW_ERROR = 0x88; // 136

    // Scooter Commands

    // -- Turn On/Off Scooter
    private static final String TURN_ON_OFF_SCOOTER = "SDIG";
    private static final int TURN_ON_SCOOTER_VALUE = 1;
    private static final int TURN_OFF_SCOOTER_VALUE = 0;
    private static final int SYSTEM_ID = 3;
    private static final int BATTERY_ID = 5;

    public static final String getTurnOnSystemCommand() {
        return createCommand(TURN_ON_OFF_SCOOTER, String.valueOf(SYSTEM_ID),
                String.valueOf(TURN_ON_SCOOTER_VALUE));
    }

    public static final String getTurnOffSystemCommand() {
        return createCommand(TURN_ON_OFF_SCOOTER, String.valueOf(SYSTEM_ID),
                String.valueOf(TURN_OFF_SCOOTER_VALUE));
    }

    public static final String getTurnOnBatteryCommand() {
        return createCommand(TURN_ON_OFF_SCOOTER, String.valueOf(BATTERY_ID),
                String.valueOf(TURN_ON_SCOOTER_VALUE));
    }

    public static final String getTurnOffBatteryCommand() {
        return createCommand(TURN_ON_OFF_SCOOTER, String.valueOf(BATTERY_ID),
                String.valueOf(TURN_OFF_SCOOTER_VALUE));
    }

    private static final String createCommand(String commandKey, String... values) {
        StringBuilder command = new StringBuilder("$" + commandKey);

        for (String value : values) {
            command.append(",").append(value);
        }

        return command.toString();
    }

    // Commands
    public static final String FIRMWARE_VERSION_COMMAND = "$ATSW";
    // public static final String TURN_ON_COMMAND = "$SDIG,5,1$SDIG,3,1";
    // public static final String TURN_OFF_COMMAND = "$SDIG,3,0$SDIG,5,0";
    public static final String TURN_ON_COMMAND = "$PWON,1";
    public static final String TURN_OFF_COMMAND = "$PWON,0";

    public static final String OPEN_TOP_CASE_COMMAND = "$TCOP";
    public static final String SCOOTER_REPORT_COMMAND = "$STMS";

    public static final byte[] FIRMWARE_VERSION_COMMAND_BYTES = FIRMWARE_VERSION_COMMAND.getBytes();
    public static final byte[] TURN_ON_COMMAND_BYTES = TURN_ON_COMMAND.getBytes();
    public static final byte[] TURN_OFF_COMMAND_BYTES = TURN_OFF_COMMAND.getBytes();
    public static final byte[] OPEN_TOP_CASE_COMMAND_BYTES = OPEN_TOP_CASE_COMMAND.getBytes();
    public static final byte[] SCOOTER_REPORT_COMMAND_BYTES = SCOOTER_REPORT_COMMAND.getBytes();

    // Commands Success Read Responses

    public static final String AUTH_SUCCESS_RESPONSE = "$RSDK,OK";
    public static final String TURN_ON_OFF_SUCCESS_RESPONSE = "$PWON,OK\r\n";
    // public static final String TURN_ON_OFF_SUCCESS_RESPONSE = "$SDIG,OK\r\n$SDIG,OK\r\n";
    public static final String OPEN_TOP_CASE_SUCCESS_RESPONSE = "$TCOP,OK\r\n";
    public static final int SCOOTER_REPORT_RESPONSE_PROPERTIES_COUNT = 18;

    /*
    $STMS,<batt-soc>,<batt-tmax>,<batt-tmin>,<batt-v>,<batt-a>,<batt-id>,<w-flags>,<e-flags>,
    <motor-tmax>,<inv-tmax>,<velocity-max>,<range>,<odo-since-on>,<e-charged-since-on>,
    <e-regen-since-on>,<e-discharged-since-on>,<ambient-temp>
     */
}
