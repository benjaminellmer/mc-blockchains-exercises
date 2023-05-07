package com.ellmer.blockchain.util

import com.ellmer.blockchain.extensions.numberOfLeadingZeros
import com.ellmer.blockchain.extensions.sha256

class Block(
    val index: Int,
    val timestamp: Long,
    val transactions: List<Transaction>,
    val previousHash: String,
    var proof: Long = 0,
) {
    val hash: String
        get() {
            return this.toString().sha256()
        }

    fun proofOfWork(difficulty: Int) {
        while (!proofIsValid(difficulty)) {
            proof++
        }
    }

    fun proofIsValid(difficulty: Int): Boolean {
        return this.toString().sha256().numberOfLeadingZeros() == difficulty
    }

    override fun toString(): String {
        return "Block(index=$index, timestamp=$timestamp, proof=$proof, transactions=$transactions, previousHash='$previousHash')"
    }
}
