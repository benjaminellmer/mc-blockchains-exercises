package com.ellmer.blockchain.util

import com.ellmer.blockchain.extensions.sha256
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
class Blockchain(
    private var currentTransactions: LinkedList<Transaction> = LinkedList(),
    val chain: LinkedList<Block> = LinkedList(),
) {
    private val difficulty = 2
    private val blockFee = 10.0

    init {
        val genesisBlock = Block(1, Instant.now().toEpochMilli(), LinkedList(), "1", 1)
        chain.add(genesisBlock)
    }

    fun createNewBlock(user: String? = null) {
        val lastBlock = chain.last
        val newTransactions = currentTransactions.toMutableList()

        if (user != null) {
            val minerTransaction = Transaction("", user, blockFee)
            newTransactions.add(minerTransaction)
        }

        val newBlock = Block(lastBlock.index + 1, Instant.now().toEpochMilli(), newTransactions, lastBlock.hash)
        newBlock.proofOfWork(difficulty)

        if (newBlock.proofIsValid(difficulty)) {
            chain.add(newBlock)
            currentTransactions.clear()
        }
    }

    fun validateTransaction(transaction: Transaction): Boolean {
        val balanceOfSender = getBalanceForUser(transaction.sender)
        return balanceOfSender >= transaction.amount
    }

    fun addTransaction(transaction: Transaction): Boolean {
        if (validateTransaction(transaction)) {
            currentTransactions.add(transaction)
            return true
        }
        return false
    }

    fun findTransactionByHash(hash: String): Transaction? {
        val allTransactions = getTransactions()
        return allTransactions.firstOrNull { it.toString().sha256() == hash }
    }

    fun getTransactions(): List<Transaction> {
        return currentTransactions
    }

    fun getBalanceForUser(user: String): Double {
        val inputs = getAllTransactions().filter { it.receiver == user }
        val outputs = getAllTransactions().filter { it.sender == user }

        return inputs.sumOf { it.amount } - outputs.sumOf { it.amount }
    }

    private fun getAllTransactions() = chain.flatMap { it.transactions } + currentTransactions
}
