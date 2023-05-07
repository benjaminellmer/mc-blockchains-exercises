package com.ellmer.blockchain.controllers

import com.ellmer.blockchain.NodeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class NodesController {
    @Autowired
    private lateinit var nodeService: NodeService

    @GetMapping("nodes/register")
    fun newTransaction(@RequestParam nodeUrl: String): ResponseEntity<Iterable<String>> {
        nodeService.registerNode(nodeUrl)

        return ResponseEntity.ok(nodeService.getRegisteredNodes())
    }

    @GetMapping("nodes/resolve")
    fun newTransaction(): ResponseEntity<String> {
        val chainUrl = nodeService.resolve()

        return ResponseEntity.ok("The longest chain from $chainUrl was applied!")
    }
}
