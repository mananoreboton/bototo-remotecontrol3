package com.borabora.bototorc.ui.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.borabora.bototorc.R
import com.borabora.bototorc.ui.basic.ControlpadLayout

class ControlPanelFragment : Fragment() {

    var connectionStatusLabel: TextView? = null

    private lateinit var viewModel: MainViewModel
    private lateinit var labelConnectionStatus: TextView
    private lateinit var leftControlPad: ControlpadLayout
    private lateinit var rightControlPad: ControlpadLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.control_panel_fragment, container, false)
        connectionStatusLabel = view.findViewById(R.id.label_connection_status)
        labelConnectionStatus = view.findViewById(R.id.label_connection_status)
        leftControlPad = view.findViewById(R.id.leftControlPad)
        rightControlPad = view.findViewById(R.id.rightControlPad)
        return view
    }

    private fun initControls() {
        connectionStatusLabel?.setOnClickListener {
            tryEnableBT()
        }
        leftControlPad.setViewModel(viewModel, "1")
        rightControlPad.setViewModel(viewModel, "0")
        rightControlPad.charge = 50


        val resposeObserver = Observer<String> { response ->
            if (response?.contains("onDeviceConnected")!!) {
                labelConnectionStatus.text = getText(R.string.label_click_to_disconnect)
            }
            if (response.contains("onDeviceDisconnected")) {
                labelConnectionStatus.text = getText(R.string.label_click_to_connect)
            }
            val toast = Toast.makeText(this@ControlPanelFragment.context, StringBuilder(response), Toast.LENGTH_SHORT)
            toast.show()
        }

        viewModel.bluetoothResposeLiveData.observe(this, resposeObserver)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(MainViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        initControls()
    }

    private fun tryEnableBT() {
        viewModel.tryEnableBT(this.context)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        viewModel.onEnableBTResult(requestCode, resultCode, data, this.context)
    }

}
