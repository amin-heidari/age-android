package com.aminheidari.age.utils

import java.security.MessageDigest

// https://gist.github.com/lovubuntu/164b6b9021f5ba54cefc67f60f7a1a25
val ByteArray.sha512: ByteArray
    get() = MessageDigest.getInstance("SHA-512").digest(this)

val ByteArray.raw: String
    get() = this.fold("", { str, byte -> str + "%02x".format(byte) })