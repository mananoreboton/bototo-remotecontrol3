package com.borabora.bototorc.ui.main

import android.app.Activity
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.util.Log
import app.akexorcist.bluetotohspp.library.BluetoothSPP
import app.akexorcist.bluetotohspp.library.BluetoothState
import app.akexorcist.bluetotohspp.library.DeviceList
import com.borabora.bototorc.data.Vehicle

val TAG = MainViewModel::class.toString()


class MainViewModel : ViewModel() {
    private val remotesVehicles: MutableList<Vehicle> = mutableListOf()
    private lateinit var bt: BluetoothSPP
    val bluetoothResposeLiveData: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun sendBTMessage(msg: String) {
        if (::bt.isInitialized) {
            if (bt.serviceState == BluetoothState.STATE_CONNECTED) {
                bt.send(msg, false)
            } else {
                bluetoothResposeLiveData.value = "Error: Disconnected BT"
            }
        }
    }

    fun getBTRemoteVehicles(): MutableList<Vehicle> {
        return remotesVehicles
    }

    fun tryEnableBT(context: Context?) {
        bt = BluetoothSPP(context)
        if (bt.serviceState != BluetoothState.STATE_CONNECTED) {
            if (bt.isBluetoothAvailable) {
                if (bt.isBluetoothEnabled) {
                    prepareToConnect(context)
                } else {
                    if (context is Activity) {
                        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                        context.startActivityForResult(enableBtIntent, BluetoothState.REQUEST_ENABLE_BT)
                    }
                }
            }
        } else {
            bt.disconnect()
        }
    }

    fun onEnableBTResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        context: Context?
    ) {
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK) {
                bt.connect(data)
                bt.send("mensaje de conectado", false)
            }
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                prepareToConnect(context)
                //bt.send("un mensaje desde android", true)
            } else {
                bluetoothResposeLiveData.value = "Error: Disabled BT"
            }
        }
    }

    private fun prepareToConnect(context: Context?) {
        bt.setupService()
        bt.startService(BluetoothState.DEVICE_OTHER)
        setupBT()
        selectRemoteDevice(context)
    }

    private fun setupBT() {
        bt.setOnDataReceivedListener(OnDataReceivedListener(bluetoothResposeLiveData))
        bt.setBluetoothConnectionListener(bluetoothConnectionListener(bluetoothResposeLiveData))
        bt.setBluetoothStateListener(bluetoothStateListener(bluetoothResposeLiveData))
    }

    private fun selectRemoteDevice(context: Context?) {
        val intent = Intent(context, DeviceList::class.java)
        if (context is Activity) {
            context.startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE)
        }
    }

    class OnDataReceivedListener(val liveData: MutableLiveData<String>) : BluetoothSPP.OnDataReceivedListener {

        override fun onDataReceived(data: ByteArray?, message: String?) {
            Log.i(TAG, "onDataReceived" + message)
            liveData.value = message
        }
    }

    class bluetoothStateListener(val liveData: MutableLiveData<String>) : BluetoothSPP.BluetoothStateListener {
        override fun onServiceStateChanged(state: Int) {
            Log.i(TAG, "onServiceStateChanged" + state)
            liveData.value = "state: " + state
        }
    }

    class bluetoothConnectionListener(val liveData: MutableLiveData<String>) : BluetoothSPP.BluetoothConnectionListener {
        override fun onDeviceDisconnected() {
                                                                                                                            Log.i(TAG, "onDeviceDisconnected")
            liveData.value = "onDeviceDisconnected"
        }

        override fun onDeviceConnected(name: String?, address: String?) {
            Log.i(TAG, "onDeviceConnected")
            liveData.value = "onDeviceConnected " + name
        }

        override fun onDeviceConnectionFailed() {
            Log.i(TAG, "onDeviceConnectionFailed")
            liveData.value = "onDeviceConnectionFailed"
        }
    }

}
