package com.borabora.bototorc.util

object CommandBuilder {
    fun buildCommand(cmd: String?, arg: Int): String {
        return "$cmd,$arg;"
    }
}