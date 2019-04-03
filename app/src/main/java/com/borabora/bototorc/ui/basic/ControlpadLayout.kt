package com.borabora.bototorc.ui.basic

import android.content.Context
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Message
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.borabora.bototorc.R
import com.borabora.bototorc.ui.main.MainViewModel
import com.borabora.bototorc.util.CommandBuilder

class ControlpadLayout(context: Context, attrs: AttributeSet): LinearLayout(context, attrs) {
    private var viewModel: MainViewModel? = null
    private var cmd: String? = null
    var charge = 0

    private val upPad: ImageView
    private val centerPad: ImageView
    private val downPad: ImageView

    init {
        inflate(context, R.layout.controlpad_layout, this)

        upPad = findViewById(R.id.imageview_up_pad)
        centerPad = findViewById(R.id.imageview_center_pad)
        downPad = findViewById(R.id.imageview_down_pad)

        val messageLooperHandler = MessageLooperHandler(handlerThread.looper)

         val listener = object : View.OnTouchListener {
            override fun onTouch(v: View, m: MotionEvent): Boolean {
                var arg = 9
                when (m.actionMasked) {
                    MotionEvent.ACTION_UP -> {
                        arg = 0
                    }
                    MotionEvent.ACTION_DOWN -> {
                        arg = 150 + charge
                    }
                    MotionEvent.ACTION_MOVE -> {
                        arg = 150 + charge
                    }
                }
                if (v == downPad) {
                    arg *= -1
                }

                val commandMessage = messageLooperHandler.obtainMessage()
                commandMessage.obj = CommandBuilder.buildCommand(cmd, arg)
                messageLooperHandler.sendMessage(commandMessage)
                return true
            }
        }

        upPad.setOnTouchListener(listener)
        downPad.setOnTouchListener(listener)

        //val attributes = context.obtainStyledAttributes(attrs, R.styleable.BenefitView)
        //imageView.setImageDrawable(attributes.getDrawable(R.styleable.BenefitView_image))
        //attributes.recycle()
    }

    fun setViewModel(mainViewModel: MainViewModel, cmd: String) {
        this.viewModel = mainViewModel
        this.cmd = cmd
    }

    companion object {
        val handlerThread: HandlerThread = HandlerThread("Message Looper")
        init {
            handlerThread.start()
        }
    }

    inner class MessageLooperHandler(looper: Looper): Handler(looper) {
        override fun handleMessage(message: Message) {
            if (message.obj is String) {
                viewModel?.sendBTMessage(message.obj as String)
            }
        }

    }
}