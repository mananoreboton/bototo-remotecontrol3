package com.borabora.bototorc

import android.arch.lifecycle.ViewModelProviders
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.borabora.bototorc.ui.main.MainViewModel
import com.borabora.bototorc.ui.main.SelectVehicleFragment


class MainActivity : AppCompatActivity(), SelectVehicleFragment.OnListFragmentInteractionListener {
    private lateinit var viewModel: MainViewModel

    override fun onListFragmentInteraction(item: BluetoothDevice?) {
        //viewModel.connectBTRemoteDevice(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        viewModel.onEnableBTResult(requestCode, resultCode, data, this)
    }

/*fun showDialog(msg: String) {
        if (alertDialog == null) {
            alertDialog = AlertDialog.Builder(this@MainActivity).create()
        } else {
            alertDialog?.hide()
        }
        alertDialog?.setTitle(getString(R.string.info_dialog_title))
        alertDialog?.setMessage(msg)
        alertDialog?.setButton(1, msg) { dialog: DialogInterface, which: Int ->
            alertDialog?.hide()
        }
        alertDialog?.show()
    }*/


}
