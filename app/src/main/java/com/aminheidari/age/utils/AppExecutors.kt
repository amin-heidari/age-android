package com.aminheidari.age.utils

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

object AppExecutors {

    val main: Executor by lazy {
        Executor { runnable ->
            Handler(Looper.getMainLooper()).post(runnable)
        }
    }

    val worker: Executor by lazy {
        Executors.newCachedThreadPool()
    }

}