package com.borabora.bototorc.ui.main

import android.app.Activity
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
    private val REQUEST_ENABLE_BT = 22;
    private lateinit var bt: BluetoothSPP

    fun getBTRemoteVehicles(): MutableList<Vehicle> {
        return remotesVehicles
    }

/*    fun connectBTRemoteDevice(remoteBluetoothDevice: BluetoothDevice?) {
        btServiceImpl = BTServiceImpl(Handler(), mBluetoothAdapter)
        btServiceImpl.connect(remoteBluetoothDevice)
    }*/

    fun tryEnableBT(context: Context?) {
        bt = BluetoothSPP(context)
        if (bt.isBluetoothAvailable) {
            if (bt.isBluetoothEnabled) {
                bt.setupService()
                bt.startService(BluetoothState.DEVICE_OTHER)
                setupBT()
                val intent = Intent(context, DeviceList::class.java)
                if (context is Activity) {
                    context.startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE)
                }
            } else {
                if (context is Activity) {
                    val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    context.startActivityForResult(enableBtIntent, BluetoothState.REQUEST_ENABLE_BT)
                }
            }
        }
    }

    /*private fun selectBTRemoteDevice(fragment: Fragment?) {
        val pairedDevices: Set<BluetoothDevice>? = mBluetoothAdapter?.bondedDevices
        pairedDevices?.forEach { device ->
            val deviceName = device.name
            val deviceHardwareAddress = device.address // MAC address
            println("deviceName = ${deviceName}")
            this.getBTRemoteVehicles().add(Vehicle(device, device.address, device.name))
        }
        fragment?.view?.findNavController()?.navigate(R.id.action_controlPanelFragment_to_selectVehicleFragment)
    }
*/
    fun onEnableBTResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK)
                bt.connect(data)
            bt.send("mensaje de conectado", false)
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
                setupBT()
                bt.send("un mensaje desde android", true)
            } else {
                // Do something if user doesn't choose any device (Pressed back)
            }
        }
    }

    private fun setupBT() {
        bt.setOnDataReceivedListener(onDataReceivedListener)
        bt.setBluetoothConnectionListener(bluetoothConnectionListener)
        bt.setBluetoothStateListener(bluetoothStateListener)
    }

    object onDataReceivedListener : BluetoothSPP.OnDataReceivedListener {
        override fun onDataReceived(data: ByteArray?, message: String?) {
            Log.i(TAG, "onDataReceived" + message)
        }

    }

    object bluetoothStateListener : BluetoothSPP.BluetoothStateListener {
        override fun onServiceStateChanged(state: Int) {
            Log.i(TAG, "onServiceStateChanged" + state)
        }
    }

    object bluetoothConnectionListener : BluetoothSPP.BluetoothConnectionListener {
        override fun onDeviceDisconnected() {
            Log.i(TAG, "onDeviceDisconnected")
        }

        override fun onDeviceConnected(name: String?, address: String?) {
            Log.i(TAG, "onDeviceConnected")
        }

        override fun onDeviceConnectionFailed() {
            Log.i(TAG, "onDeviceConnectionFailed")
        }
    }

}
