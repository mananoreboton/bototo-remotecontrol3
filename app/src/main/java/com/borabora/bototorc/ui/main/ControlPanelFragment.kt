package com.borabora.bototorc.ui.main

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.borabora.bototorc.R

class ControlPanelFragment : Fragment() {

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
        viewModel.tryEnableBT(this.context)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        viewModel.onEnableBTResult(requestCode, resultCode, data)
    }

}
