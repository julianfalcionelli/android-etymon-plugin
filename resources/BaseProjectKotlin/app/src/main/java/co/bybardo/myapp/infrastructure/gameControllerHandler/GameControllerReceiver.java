/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.gameControllerHandler;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class GameControllerReceiver extends BroadcastReceiver {
    private static final String TAG = GameControllerReceiver.class.getSimpleName();
    private IGameControllerReceiver mGameControllerReceiverListener;

    public GameControllerReceiver(IGameControllerReceiver GameControllerReceiverListener) {
        mGameControllerReceiverListener = GameControllerReceiverListener;
    }

    public static IntentFilter getIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);

        return filter;
    }

    @SuppressLint("LogNotTimber")
    @Override
    public void onReceive(Context context, Intent intent) {
        BluetoothDevice btDevice = intent.getExtras().getParcelable(BluetoothDevice.EXTRA_DEVICE);

        Log.i(TAG, intent.getAction() + " Device name " + btDevice.getName());

        if (GameControllerUtils.isGameControllerConnected()) {
            if (intent.getAction().equals(BluetoothDevice.ACTION_ACL_CONNECTED)) {
                mGameControllerReceiverListener.OnGameControllerConnected();
            } else {
                mGameControllerReceiverListener.OnGameControllerDisconnected();
            }
        }
    }

    public interface IGameControllerReceiver {
        void OnGameControllerConnected();

        void OnGameControllerDisconnected();
    }
}
