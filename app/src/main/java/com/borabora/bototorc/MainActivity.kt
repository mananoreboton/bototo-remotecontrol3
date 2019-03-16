package com.borabora.bototorc

import android.arch.lifecycle.ViewModelProviders
import android.bluetooth.BluetoothDevice
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.borabora.bototorc.data.Vehicle
import com.borabora.bototorc.ui.main.MainViewModel
import com.borabora.bototorc.ui.main.SelectVehicleFragment

class MainActivity : AppCompatActivity(), SelectVehicleFragment.OnListFragmentInteractionListener {
    private lateinit var viewModel: MainViewModel

    override fun onListFragmentInteraction(item: BluetoothDevice?) {
        viewModel.connectBTRemoteDevice(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

}
