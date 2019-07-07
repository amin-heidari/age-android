package com.aminheidari.age.utils

import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream
import java.io.Serializable

// https://stackoverflow.com/a/33097652
fun Serializable.serializeToString(): String {
    val bo = ByteArrayOutputStream()
    val so = ObjectOutputStream(bo)
    so.writeObject(this)
    so.flush()
    return String(Base64.encode(bo.toByteArray(), 0))
}