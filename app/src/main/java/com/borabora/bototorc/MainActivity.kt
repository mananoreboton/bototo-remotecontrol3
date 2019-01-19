package com.borabora.bototorc

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.borabora.bototorc.ui.main.ControlPanelFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ControlPanelFragment.newInstance())
                .commitNow()
        }
    }

}
