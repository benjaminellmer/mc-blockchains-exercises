package com.ellmer.blockchain.extensions

import java.security.MessageDigest

fun String.sha256(): String {
    return hashString(this, "SHA-256")
}

private fun hashString(input: String, algorithm: String): String {
    return MessageDigest
        .getInstance(algorithm)
        .digest(input.toByteArray())
        .fold("") { str, it -> str + "%02x".format(it) }
}

fun String.numberOfLeadingZeros(): Int {
    val regex = Regex("^0+")
    val matchResult = regex.find(this)
    return matchResult?.value?.length ?: 0
}
