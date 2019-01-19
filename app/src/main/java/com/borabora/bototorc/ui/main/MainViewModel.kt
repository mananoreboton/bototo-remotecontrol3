package com.borabora.bototorc.ui.main

import android.arch.lifecycle.ViewModel
import com.borabora.bototorc.data.Vehicle

class MainViewModel : ViewModel() {
    private val remotesVehicles: MutableList<Vehicle> = mutableListOf()

    fun getBTRemoteVehicles(): MutableList<Vehicle> {
        return remotesVehicles
    }
    // TODO: Implement the ViewModel
}
