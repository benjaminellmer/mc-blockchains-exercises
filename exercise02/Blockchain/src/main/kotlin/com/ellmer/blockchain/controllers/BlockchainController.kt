package com.ellmer.blockchain.controllers

import com.ellmer.blockchain.extensions.sha256
import com.ellmer.blockchain.util.Block
import com.ellmer.blockchain.util.Blockchain
import com.ellmer.blockchain.util.Transaction
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.net.URI

@Controller
class BlockchainController(
    private val blockChain: Blockchain,
) {

    @PostMapping("transaction")
    fun newTransaction(@RequestBody transaction: Transaction, httpServletRequest: HttpServletRequest): ResponseEntity<String> {
        val valid = blockChain.addTransaction(transaction)
        if(!valid) {
            return ResponseEntity.badRequest().build()
        }

        val transactionHash = transaction.toString().sha256()
        val location = URI("${httpServletRequest.requestURL}/$transactionHash")

        return ResponseEntity.created(location).build()
    }

    @GetMapping("transaction/{hash}")
    fun newTransaction(@PathVariable hash: String): ResponseEntity<Transaction> {
        val transaction = blockChain.findTransactionByHash(hash)

        transaction?.let {
            return ResponseEntity.ok(transaction)
        }

        return ResponseEntity.notFound().build()
    }

    @GetMapping("transactions")
    fun newTransaction(): ResponseEntity<List<Transaction>> {
        return ResponseEntity.ok(blockChain.getTransactions())
    }

    @GetMapping("chain")
    fun chain(): ResponseEntity<List<Block>> {
        return ResponseEntity.ok(blockChain.chain)
    }

    @GetMapping("mine")
    fun mine(@RequestParam user: String?): ResponseEntity<Block> {
        blockChain.createNewBlock(user)
        return ResponseEntity.ok(blockChain.chain.last)
    }

    @GetMapping("balance")
    fun balance(@RequestParam user: String): ResponseEntity<Double> {
        return ResponseEntity.ok(blockChain.getBalanceForUser(user))
    }
}
