package com.ellmer.blockchain

import com.ellmer.blockchain.util.Block
import com.ellmer.blockchain.util.Blockchain
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.ParameterizedTypeReference
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import java.util.*

@Service
class NodeService {
    @Autowired
    private lateinit var blockchain: Blockchain

    @Autowired
    private lateinit var environment: Environment

    private val registeredNodes: LinkedList<String> = LinkedList()

    fun registerNode(url: String) {
        registeredNodes.add(url)
    }

    fun getRegisteredNodes(): Iterable<String> {
        return registeredNodes
    }

    fun resolve(): String? {
        var longestChain = blockchain.chain
        var longestChainUrl = "localhost:${environment["server.port"]}"

        registeredNodes.forEach { url ->
            getChainForNode(url)?.let { chain ->
                if (chain.size > longestChain.size && Blockchain(chain).validate()) {
                    longestChain = chain
                    longestChainUrl = url
                }
            }
        }

        if (longestChain != blockchain.chain) {
            blockchain.resetWithNewChain(longestChain)
        }

        return longestChainUrl
    }

    private fun getChainForNode(url: String): ArrayList<Block>? {
        return WebClient.builder()
            .baseUrl(url)
            .build()
            .get()
            .uri("chain")
            .retrieve()
            .bodyToMono(object : ParameterizedTypeReference<ArrayList<Block>>() {})
            .block()
    }
}
