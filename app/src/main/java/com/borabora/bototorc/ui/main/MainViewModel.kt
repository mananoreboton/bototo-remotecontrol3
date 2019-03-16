package com.borabora.bototorc.ui.main

import android.app.Activity
import android.arch.lifecycle.ViewModel
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.os.Handler
import android.support.v4.app.Fragment
import androidx.navigation.findNavController
import com.borabora.bototorc.R
import com.borabora.bototorc.data.Vehicle
import com.borabora.bototorc.service.BTServiceImpl

class MainViewModel : ViewModel() {
    private val TAG = MainViewModel::class.toString()
    private val remotesVehicles: MutableList<Vehicle> = mutableListOf()
    private val REQUEST_ENABLE_BT = 22;
    var mBluetoothAdapter: BluetoothAdapter? = null
    lateinit var btServiceImpl: BTServiceImpl

    fun getBTRemoteVehicles(): MutableList<Vehicle> {
        return remotesVehicles
    }

    fun connectBTRemoteDevice(remoteBluetoothDevice: BluetoothDevice?) {
        btServiceImpl = BTServiceImpl(Handler(), mBluetoothAdapter)
        btServiceImpl.connect(remoteBluetoothDevice)
    }

    fun tryEnableBT(fragment: Fragment?) {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (mBluetoothAdapter == null) {
            // Device doesn't support Bluetooth
        } else {
            if (mBluetoothAdapter?.isEnabled == false) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                fragment?.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
            } else {
                selectBTRemoteDevice(fragment)
            }
        }
    }

    private fun selectBTRemoteDevice(fragment: Fragment?) {
        val pairedDevices: Set<BluetoothDevice>? = mBluetoothAdapter?.bondedDevices
        pairedDevices?.forEach { device ->
            val deviceName = device.name
            val deviceHardwareAddress = device.address // MAC address
            println("deviceName = ${deviceName}")
            this.getBTRemoteVehicles().add(Vehicle(device, device.address, device.name))
        }
        fragment?.view?.findNavController()?.navigate(R.id.action_controlPanelFragment_to_selectVehicleFragment)
    }

    fun onEnableBTResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        fragment: Fragment?
    ) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                selectBTRemoteDevice(fragment)
            }
        }
    }

}
