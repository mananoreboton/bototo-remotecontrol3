package com.borabora.bototorc.data

import android.bluetooth.BluetoothDevice

data class Vehicle(
    var device: BluetoothDevice,
    val id: CharSequence?,
    val content: CharSequence?
)