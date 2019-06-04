package com.aminheidari.age.utils

import android.util.Log

class Logger {

    companion object {

        fun v(message: String) { }

        fun v(tag: String?, message: String, throwable: Throwable? = null) { }

        fun d(message: String) { }

        fun d(tag: String?, message: String, throwable: Throwable? = null) { }

        fun i(message: String) { }

        fun i(tag: String?, message: String, throwable: Throwable? = null) { }

        fun w(message: String) { }

        fun w(tag: String?, message: String, throwable: Throwable? = null) { }

        fun e(message: String) { }

        fun e(tag: String?, message: String, throwable: Throwable? = null) { }

        fun wtf(message: String) { }

        fun wtf(tag: String?, message: String, throwable: Throwable? = null) { }

    }

}