/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.bluetooth.base;

import static android.bluetooth.BluetoothAdapter.STATE_CONNECTED;
import static android.bluetooth.BluetoothAdapter.STATE_DISCONNECTED;
import static android.bluetooth.BluetoothGatt.GATT_SUCCESS;

import static co.bybardo.myapp.infrastructure.bluetooth.base.model.BleOperationType.TYPE_READ;
import static co.bybardo.myapp.infrastructure.bluetooth.base.model.BleOperationType.TYPE_SUBSCRIBE;
import static co.bybardo.myapp.infrastructure.bluetooth.base.model.BleOperationType.TYPE_WRITE;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import co.bybardo.myapp.infrastructure.bluetooth.base.exceptions.BleException;
import co.bybardo.myapp.infrastructure.bluetooth.base.model.BleOperation;
import co.bybardo.myapp.infrastructure.bluetooth.base.model.BleResult;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.internal.schedulers.SingleScheduler;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

/**
 * You must have a concrete implementation according your BT Device specifications.
 */
@SuppressLint("LogNotTimber")
public abstract class BluetoothDeviceManager {
    /**
     * Created a thread with a Single behavior to handle all BT I/O requests sequentially.
     */
    @NonNull
    protected static final Scheduler BLUETOOTH_SINGLE_SCHEDULER = RxJavaPlugins.onSingleScheduler(
            RxJavaPlugins.initSingleScheduler(SingleScheduler::new));

    protected static final String TAG = BluetoothConstants.TAG;

    protected Context mContext;
    protected String mDeviceMacAddress;
    protected BluetoothManager mBluetoothManager;

    private List<BleOperation> mBleOperations = new ArrayList<>();
    private BluetoothGatt mBtGatt;
    private boolean mConnectionReady = false;
    private boolean mBleOperationInProgress = false;
    private BehaviorSubject<Pair<Integer, BluetoothGattCharacteristic>> mBluetoothOperationIdBS =
            BehaviorSubject.create();
    private PublishSubject<BleResult> mBluetoothOperationResultsPS = PublishSubject.create();
    private BehaviorSubject<Integer> mBluetoothDeviceStatusPS = BehaviorSubject.create();
    private BluetoothGattCallback mBluetoothGattCallback = new BluetoothGattCallback() {

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt,
                BluetoothGattCharacteristic characteristic,
                int status) {
            Log.d(TAG, "New Characteristic Write: ");
            printCharacteristicInfo(characteristic);
            Log.d(TAG, "Characteristic Write Status: "
                    + String.valueOf(status));

            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d(TAG, "Write Operation Success");
            } else {
                Log.w(TAG, "Something weird during BLE Write");
            }

            handlerBluetoothResult(characteristic, status);
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                BluetoothGattCharacteristic characteristic,
                int status) {
            Log.d(TAG, "New Characteristic Read: ");
            printCharacteristicInfo(characteristic);
            Log.d(TAG, "Characteristic Read Status: "
                    + String.valueOf(status));

            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d(TAG, "Read Operation Success");
            } else {
                Log.w(TAG, "Something weird during BLE Read");
            }

            handlerBluetoothResult(characteristic, status);
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt,
                BluetoothGattDescriptor descriptor,
                int status) {
            Log.d(TAG, "New Descriptor Write: ");
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d(TAG, "DescriptorWrite Operation Success");
            } else {
                Log.w(TAG, "Something weird during BLE DescriptorWrite");
            }

            handlerBluetoothResult(descriptor, status);
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt,
                BluetoothGattDescriptor descriptor,
                int status) {
            Log.d(TAG, "New Descriptor Read: ");
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d(TAG, "DescriptorRead Operation Gatt Success");
            } else {
                Log.w(TAG, "Something weird during BLE DescriptorRead");
            }

            handlerBluetoothResult(descriptor, status);
        }

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt,
                int status,
                int newState) {
            Log.d(TAG, "onConnectionStateChange");
            switch (newState) {
                case STATE_DISCONNECTED:
                    Log.d(TAG, "onConnectionStateChange STATE_DISCONNECTED");
                    mBtGatt = null;
                    mConnectionReady = false;
                    mBleOperationInProgress = false;
                    mBluetoothOperationResultsPS.onNext(new BleResult(BluetoothGatt.GATT_FAILURE));
                    mBluetoothDeviceStatusPS.onNext(BluetoothDeviceState.DISCONNECTED);
                    break;
                case STATE_CONNECTED:
                    Log.d(TAG, "onConnectionStateChange STATE_CONNECTED");
                    mBtGatt.discoverServices();
                    mBluetoothDeviceStatusPS.onNext(BluetoothDeviceState.CONNECTED);
                    break;
                default:
                    Log.d(TAG, "onConnectionStateChange OTHER");
                    break;
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt,
                int status) {
            Log.d(TAG, "onServicesDiscovered");
            if (status == GATT_SUCCESS) {
                mConnectionReady = true;
                onBTServicesDiscovered(gatt);
                performNextOperation(); // Start processing operations
            }
        }
    };

    private void handlerBluetoothResult(BluetoothGattCharacteristic characteristic,
            int status) {
        mBluetoothOperationResultsPS.onNext(new BleResult(status, characteristic));
        performNextOperation();
    }

    private void handlerBluetoothResult(BluetoothGattDescriptor descriptor,
            int status) {
        mBluetoothOperationResultsPS.onNext(new BleResult(status, descriptor));
        performNextOperation();
    }

    @SuppressLint("CheckResult")
    public BluetoothDeviceManager(BluetoothManager bluetoothManager, Context context,
            String deviceMacAddress) {
        mContext = context;
        mDeviceMacAddress = deviceMacAddress;
        mBluetoothManager = bluetoothManager;

        mBluetoothManager.observeBluetoothStatus()
                .observeOn(Schedulers.io())
                .subscribe(status -> {
                    if (status == BluetoothState.BLUETOOTH_OFF) {
                        clearStates();
                        Log.d(TAG, "Bluetooth Status: " + String.valueOf(status));
                    }
                }, error -> {
                });
    }

    public Observable<Integer> observeDeviceState() {
        return mBluetoothDeviceStatusPS;
    }

    public void dump() {
        BluetoothDevice bluetoothDevice = getPairedDevice(mDeviceMacAddress);

        if (bluetoothDevice == null) {
            return; // Nothing
        }

        BluetoothUtils.dumpBluetoothDevice(bluetoothDevice);
    }

    private BluetoothDevice getPairedDevice(String mac) {
        for (BluetoothDevice bluetoothDevice : mBluetoothManager.getPairedDevices().blockingGet()) {
            if (bluetoothDevice.getAddress().equals(mac)) {
                return bluetoothDevice;
            }
        }

        return null;
    }

    protected Single<BleResult> write(UUID service, UUID charUuid, byte[] data) {
        return write(service, charUuid, data, false);
    }

    protected Single<BleResult> write(UUID service, UUID charUuid, byte[] data,
            boolean highPriority) {
        BleOperation bleOperation = BleOperation.newWriteOperation(service, charUuid, data,
                highPriority);

        return checkConnection()
                .andThen(Completable.fromAction(() -> {
                    addOperationToQueue(bleOperation);
                    checkQueue();
                }))
                .andThen(mBluetoothOperationIdBS
                        .filter(operationIds -> operationIds.first == bleOperation.getId())
                        .firstOrError()
                        .flatMap(operationIds -> mBluetoothOperationResultsPS
                                .filter(btResult ->
                                        // Some error could happened and char is null
                                        !btResult.hasBtInfo() ||
                                                btResult.getBluetoothGattCharacteristic().getUuid()
                                                        .equals(operationIds.second.getUuid()))
                                .doOnNext(result -> Log.i(TAG, "Write Result Sent Successfully"))
                                .firstOrError()))
                .subscribeOn(BLUETOOTH_SINGLE_SCHEDULER);
    }

    private void doWrite(BleOperation bleOperation) {
        BluetoothGattService gattService = mBtGatt.getService(bleOperation.getService());
        BluetoothGattCharacteristic characteristic = gattService.getCharacteristic(
                bleOperation.getCharUuid());
        characteristic.setValue(bleOperation.getData());

        mBluetoothOperationIdBS.onNext(new Pair<>(bleOperation.getId(), characteristic));

        if (mBtGatt.writeCharacteristic(characteristic)) {
            Log.d(TAG, "Performing write");
            printCharacteristicInfo(characteristic);
        } else {
            Log.d(TAG, "Couldn't perform write!");
        }
    }

    protected Single<BleResult> read(UUID service, UUID charUuid, byte[] data) {
        return read(service, charUuid, data, false);
    }

    protected Single<BleResult> read(UUID service, UUID charUuid, byte[] data,
            boolean highPriority) {
        BleOperation bleOperation = BleOperation.newReadOperation(service, charUuid, data,
                highPriority);
        return checkConnection()
                .andThen(Completable.fromAction(() -> {
                    addOperationToQueue(bleOperation);
                    checkQueue();
                }))
                .andThen(
                        mBluetoothOperationIdBS
                                .filter(operationIds -> operationIds.first == bleOperation.getId())
                                .firstOrError()
                                .flatMap(operationIds -> mBluetoothOperationResultsPS
                                        .doOnNext(result ->
                                                Log.i(TAG, "Result: " +
                                                        result.getBluetoothGattCharacteristic()
                                                                .getStringValue(0) +
                                                        " - Excepted "
                                                        + "Command: " +
                                                        new String(bleOperation.getData())))
                                        .filter(btResult ->
                                                // Some error could happened and char is null
                                                !btResult.hasBtInfo() ||
                                                        (btResult.getBluetoothGattCharacteristic()
                                                                .getUuid()
                                                                .equals(operationIds.
                                                                        second.
                                                                        getUuid())
                                                                // &&
                                                                // btResult
                                                                // .getBluetoothGattCharacteristic()
                                                                // .getStringValue(0).contains(
                                                                // new String(
                                                                // bleOperation.getData()))
                                                        )
                                        )
                                        .doOnNext(result -> Log.i(TAG,
                                                "Read Result Sent Successfully"))
                                        .firstOrError())
                )
                .subscribeOn(BLUETOOTH_SINGLE_SCHEDULER);
    }

    private void doRead(BleOperation bleOperation) {
        BluetoothGattService gattService = mBtGatt.getService(bleOperation.getService());
        BluetoothGattCharacteristic characteristic = gattService.getCharacteristic(
                bleOperation.getCharUuid());
        characteristic.setValue(bleOperation.getData());

        mBluetoothOperationIdBS.onNext(new Pair<>(bleOperation.getId(), characteristic));

        if (mBtGatt.readCharacteristic(characteristic)) {
            Log.d(TAG, "Performing read");
        } else {
            Log.d(TAG, "Couldn't perform read!");
        }
    }

    // TODO Fix descriptors related
    protected Completable subscribe(UUID service, UUID charUuid, boolean enable,
            boolean highPriority) {
        return checkConnection()
                .andThen(Completable.fromAction(() -> {
                    addOperationToQueue(
                            BleOperation.newSubscribeOperation(service, charUuid, enable,
                                    highPriority));
                    checkQueue();
                }))
                .subscribeOn(BLUETOOTH_SINGLE_SCHEDULER);
    }

    private void doSubscribe(BleOperation bleOperation) {
        Log.d(TAG, "doSubscribe");
        BluetoothGattService gattService = mBtGatt.getService(bleOperation.getService());
        BluetoothGattCharacteristic characteristic = gattService.getCharacteristic(
                bleOperation.getCharUuid());
        if (mBtGatt.setCharacteristicNotification(characteristic, bleOperation.isEnable())) {
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
                    UUID.fromString("000-00...")); //TODO Check this
            if (descriptor != null) {
                byte[] value =
                        bleOperation.isEnable() ? BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                                : BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE;
                descriptor.setValue(value);

                if (mBtGatt.writeDescriptor(descriptor)) {
                    Log.d(TAG, "Performing subscription");
                } else {
                    Log.d(TAG, "Couldn't perform subscription!");
                }
            }
        }
    }

    protected Completable checkConnection() {
        return mBluetoothManager.turnOn()
                .doOnError(error -> Log.d(TAG, "Error Turning ON BT"))
                .andThen(Completable.fromAction(this::startConnection))
                .andThen(mBluetoothDeviceStatusPS
                        .firstOrError()
                        .flatMapCompletable(status -> {
                                    if (status == BluetoothDeviceState.CONNECTED) {
                                        return Completable.complete();
                                    } else {
                                        return Completable.error(
                                                BleException.
                                                        BluetoothDeviceNotConnectedException::new);
                                    }
                                }
                        ));
    }

    private synchronized void startConnection()
            throws BleException.BluetoothDeviceUnpairedException {
        if (mBtGatt == null) {
            BluetoothDevice bluetoothDevice = getPairedDevice(mDeviceMacAddress);
            if (bluetoothDevice == null) { // Device got unpaired
                Log.d(TAG, "Device got unpaired");
                throw new BleException.BluetoothDeviceUnpairedException();
            }

            mBtGatt = bluetoothDevice.connectGatt(mContext, false, mBluetoothGattCallback);
        }
    }

    protected void onBTServicesDiscovered(BluetoothGatt gatt) {
        List<BluetoothGattService> btServices = gatt.getServices();
        Log.d(TAG, "onBTServicesDiscovered");

        for (BluetoothGattService btService : btServices) {
            Log.d(TAG, "BT Service: " + btService.getUuid().toString());
        }

        // Do something ?
    }

    private synchronized void addOperationToQueue(BleOperation bleOperation) {
        int position = 0;

        for (int i = 0; i < mBleOperations.size(); i++) {
            if (!mBleOperations.get(0).isHighPriority()) { // Keep High Priority Operations at top
                break;
            }

            position++;
        }

        mBleOperations.add(position, bleOperation);
    }

    private synchronized void checkQueue() {
        if (!mBleOperationInProgress) {
            performNextOperation();
        }
    }

    @SuppressLint("CheckResult")
    private synchronized void performNextOperation() {
        if (!mConnectionReady) {
            return; // Connection Not Ready
        }

        if (mBleOperations.size() == 0) {
            mBleOperationInProgress = false;
            return;
        }

        mBleOperationInProgress = true;

        Completable.fromAction(() -> performOperation(mBleOperations.remove(0)))
                .subscribeOn(BLUETOOTH_SINGLE_SCHEDULER)
                .subscribe(
                        () -> Log.d(TAG, "Performing BT Operation"),
                        error -> Log.w(TAG, "Error performing BT Operation")
                );
    }

    private synchronized void performOperation(BleOperation bleOperation) {
        switch (bleOperation.getType()) {
            case TYPE_READ:
                doRead(bleOperation);
                break;
            case TYPE_WRITE:
                doWrite(bleOperation);
                break;
            case TYPE_SUBSCRIBE:
                doSubscribe(bleOperation);
                break;
        }
    }

    private void printCharacteristicInfo(
            BluetoothGattCharacteristic characteristic) {
        if (characteristic != null) {
            Log.d(TAG, "Characteristic Value: " + characteristic.getStringValue(0));
        }
    }

    public void clearStates() {
        if (mBtGatt != null) {
            Log.d(TAG, "Closing BT Gatt");
            mBtGatt.abortReliableWrite();
            mBtGatt.disconnect();
            mBtGatt.close();
            mBtGatt = null;
        }

        mBleOperations = new ArrayList<>();
        mConnectionReady = false;
        mBleOperationInProgress = false;

        mBluetoothOperationIdBS = BehaviorSubject.create();
        mBluetoothOperationResultsPS = PublishSubject.create();
        mBluetoothDeviceStatusPS = BehaviorSubject.create();
    }


    private byte[] generatePacket(int size) {
        byte[] packet = new byte[size];

        for (int i = 0; i < packet.length; i++) { // Initialize
            packet[i] = 0;
        }

        return packet;
    }

    // MARK -- Concurrence Related

    // This is to syncronize a batch of commands since some of the ASTRA commands are internally
    // composed by more than one command, that is for example, to get the Report we first need
    // to make a write of that command and then the read.

    private Integer sequenceOfCommandsId = 0;
    private List<Integer> sequenceOfCommandsQueue = new ArrayList<>();

    // This must be a Behavior in order to capture the self-incoming stream during the QUEUE Check
    // during onSubscribe
    private BehaviorSubject<Integer> sequenceOfCommandsSubject = BehaviorSubject.create();
    private Integer currentSequenceOfCommandsId = null;

    private boolean isSequenceOfCommandsInProgress() {
        return currentSequenceOfCommandsId != null;
    }

    private Integer getNextSequenceOfCommandsId() {
        return ++sequenceOfCommandsId;
    }

    private void checkSequenceOfCommandsQueue(Integer sequenceId) {
        sequenceOfCommandsQueue.add(sequenceId);

        if (!isSequenceOfCommandsInProgress()) {
            startNextSequenceOfCommandsId();
        }
    }

    private void onSequenceOfCommandsFinished(Integer sequenceId) {
        if (sequenceId.equals(currentSequenceOfCommandsId)) { // Just for double verification
            currentSequenceOfCommandsId = null;
            if (!sequenceOfCommandsQueue.isEmpty()) {
                startNextSequenceOfCommandsId();
            }
        }
    }

    private void startNextSequenceOfCommandsId() {
        currentSequenceOfCommandsId = sequenceOfCommandsQueue.remove(0);
        sequenceOfCommandsSubject.onNext(currentSequenceOfCommandsId);
    }

    protected synchronized Completable runSequenceOfCommands(Completable completable) {
        Integer id = getNextSequenceOfCommandsId();
        return observeSequenceOfCommandsById(id)
                .flatMapCompletable(__ -> completable
                        .doFinally(() -> onSequenceOfCommandsFinished(id))) // Check Next
                .doOnSubscribe(__ -> checkSequenceOfCommandsQueue(id)) // Check Queue
                .subscribeOn(BLUETOOTH_SINGLE_SCHEDULER);
    }

    protected synchronized <T> Single<T> runSequenceOfCommands(Single<T> single) {
        Integer id = getNextSequenceOfCommandsId();
        return observeSequenceOfCommandsById(id)
                .flatMap(__ -> single
                        .doFinally(() -> onSequenceOfCommandsFinished(id))) // Check Next
                .doOnSubscribe(__ -> checkSequenceOfCommandsQueue(id)) // Check Queue
                .subscribeOn(BLUETOOTH_SINGLE_SCHEDULER);
    }

    private Single<Integer> observeSequenceOfCommandsById(Integer id) {
        return sequenceOfCommandsSubject
                .filter(sequenceId -> sequenceId.equals(id))
                .firstOrError();
    }
}
