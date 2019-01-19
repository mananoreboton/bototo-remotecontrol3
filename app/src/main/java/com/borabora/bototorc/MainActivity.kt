package com.borabora.bototorc

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.borabora.bototorc.data.Vehicle
import com.borabora.bototorc.ui.main.SelectVehicleFragment

class MainActivity : AppCompatActivity(), SelectVehicleFragment.OnListFragmentInteractionListener {
    override fun onListFragmentInteraction(item: Vehicle?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }

}
