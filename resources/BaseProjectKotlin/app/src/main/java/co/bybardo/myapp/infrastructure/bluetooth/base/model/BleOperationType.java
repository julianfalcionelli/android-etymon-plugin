/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.bluetooth.base.model;

import static java.lang.annotation.RetentionPolicy.SOURCE;

import static co.bybardo.myapp.infrastructure.bluetooth.base.model.BleOperationType.TYPE_READ;
import static co.bybardo.myapp.infrastructure.bluetooth.base.model.BleOperationType.TYPE_SUBSCRIBE;
import static co.bybardo.myapp.infrastructure.bluetooth.base.model.BleOperationType.TYPE_WRITE;

import java.lang.annotation.Retention;

import androidx.annotation.IntDef;

@Retention(SOURCE)
@IntDef({TYPE_SUBSCRIBE, TYPE_READ, TYPE_WRITE})
public @interface BleOperationType {
    int TYPE_SUBSCRIBE = 0;
    int TYPE_READ = 1;
    int TYPE_WRITE = 2;
}
