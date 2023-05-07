package com.ellmer.blockchain.util

import com.ellmer.blockchain.extensions.sha256
import com.ellmer.blockchain.properties.BlockchainProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
class Blockchain(
    var chain: ArrayList<Block> = arrayListOf(
        Block(0, Instant.now().toEpochMilli(), LinkedList(), "1", 1),
    ),
    private var currentTransactions: LinkedList<Transaction> = LinkedList(),
) {
    @Autowired
    private lateinit var blockchainProperties: BlockchainProperties

    private var difficulty = 2
    private val blockFee = 10.0

    fun resetWithNewChain(chain: ArrayList<Block>) {
        this.chain = chain
        currentTransactions.clear()
    }

    fun createNewBlock(user: String? = null) {
        val lastBlock = chain.last()
        val newTransactions = currentTransactions.toMutableList()

        if (user != null) {
            val minerTransaction = Transaction("", user, blockFee)
            newTransactions.add(minerTransaction)
        }

        val newBlock = Block(lastBlock.index + 1, Instant.now().toEpochMilli(), newTransactions, lastBlock.hash)
        newBlock.proofOfWork(difficulty)

        if (newBlock.proofIsValid(difficulty)) {
            adjustDifficulty()
            chain.add(newBlock)
            currentTransactions.clear()
        }
    }

    fun adjustDifficulty() {
        if (blockchainProperties.adjustableDifficulty) {
            val blockDurationSeconds = (Instant.now().toEpochMilli() - chain.last().timestamp) / 1000
            if (blockDurationSeconds < blockchainProperties.targetBlockDurationSeconds && difficulty < blockchainProperties.maxDifficulty) {
                difficulty++
            } else if (difficulty > 1) {
                difficulty--
            }
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

    fun validate(): Boolean {
        val genesisBlock = chain.first()
        val genesisBlockIsValid = genesisBlock.index == 0 && genesisBlock.transactions.isEmpty() && genesisBlock.previousHash == "1" && genesisBlock.proof == 1L
        val hashesAreValid = chain.all { it.hash == it.toString().sha256() }
        val indexesAreValid = chain.all { it.index == chain.indexOf(it) }
        val previousHashesAreValid = chain.all {
            it.index == 0 || it.previousHash == chain[it.index - 1].hash
        }
        val validationBlockchain = Blockchain(arrayListOf())
        val transactionsAreValid = chain.all { block ->
            block.transactions.all { validationBlockchain.addTransaction(it) }
            validationBlockchain.chain.add(block)
        }

        return genesisBlockIsValid && hashesAreValid && indexesAreValid && previousHashesAreValid && transactionsAreValid
    }

    private fun getAllTransactions() = chain.flatMap { it.transactions } + currentTransactions
}
