package com.aminheidari.age.utils

import android.util.Log

class Logger {

    companion object {

        private const val genericTag = "Generic"

        fun v(message: String) {
            v(genericTag, message)
        }

        fun v(tag: String, message: String, throwable: Throwable? = null) {
            Log.v(tag, message, throwable)
        }

        fun d(message: String) {
            d(genericTag, message)
        }

        fun d(tag: String, message: String, throwable: Throwable? = null) {
            Log.d(tag, message, throwable)
        }

        fun i(message: String) {
            i(genericTag, message)
        }

        fun i(tag: String, message: String, throwable: Throwable? = null) {
            Log.i(tag, message, throwable)
        }

        fun w(message: String) {
            w(genericTag, message)
        }

        fun w(tag: String, message: String, throwable: Throwable? = null) {
            Log.w(tag, message, throwable)
        }

        fun e(message: String) {
            e(genericTag, message)
        }

        fun e(tag: String, message: String, throwable: Throwable? = null) {
            Log.e(tag, message, throwable)
        }

        fun wtf(message: String) {
            wtf(genericTag, message)
        }

        fun wtf(tag: String, message: String, throwable: Throwable? = null) {
            Log.wtf(tag, message, throwable)
        }

    }

}