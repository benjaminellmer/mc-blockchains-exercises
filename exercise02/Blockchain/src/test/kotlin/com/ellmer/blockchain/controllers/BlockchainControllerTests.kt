package com.ellmer.blockchain.controllers

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest
@AutoConfigureMockMvc
class BlockchainControllerTests {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun testGetEmptyChain() {
        mockMvc.get("/chain").andDo {
            print()
        }.andExpect {
            status { isOk() }
        }
    }
}
