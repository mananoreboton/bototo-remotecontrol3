package com.borabora.bototorc.ui.main

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import com.borabora.bototorc.R
import com.borabora.bototorc.data.Vehicle

class ControlPanelFragment : Fragment() {

    private val REQUEST_ENABLE_BT = 22;
    var mBluetoothAdapter: BluetoothAdapter? = null
    var connectionStatusLabel: TextView? = null

    companion object {
        fun newInstance() = ControlPanelFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.control_panel_fragment, container, false)

        connectionStatusLabel = view.findViewById(R.id.label_connection_status)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(MainViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        connectionStatusLabel?.setOnClickListener { tryEnableBT() }
    }

    private fun tryEnableBT() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (mBluetoothAdapter == null) {
            // Device doesn't support Bluetooth
        } else {
            if (mBluetoothAdapter?.isEnabled == false) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
            } else {
                selectBTRemoteDevice()
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_ENABLE_BT) {
            if(resultCode == Activity.RESULT_OK) {
                selectBTRemoteDevice()
            }
        }
    }

    private fun selectBTRemoteDevice() {
        val pairedDevices: Set<BluetoothDevice>? = mBluetoothAdapter?.bondedDevices
        pairedDevices?.forEach { device ->
            val deviceName = device.name
            val deviceHardwareAddress = device.address // MAC address
            println("deviceName = ${deviceName}")
            viewModel.getBTRemoteVehicles().add(Vehicle(device, device.address, device.name))
        }
        view?.findNavController()?.navigate(R.id.action_controlPanelFragment_to_selectVehicleFragment)
    }

}
