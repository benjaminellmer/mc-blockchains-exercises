package com.ellmer.blockchains

import jakarta.servlet.http.HttpServletRequest
import lombok.AllArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader

@SpringBootApplication
class HashCashApplication

fun main(args: Array<String>) {
    runApplication<HashCashApplication>(*args)
}

@Controller
@AllArgsConstructor
class HashCashController {
    @Autowired
    private lateinit var headerService: HeaderService

    @GetMapping("greet")
    fun greet(httpServletRequest: HttpServletRequest, @RequestHeader("Hash-REST") hashRestValue: String): ResponseEntity<String> {
        return validateHashCashRequest(hashRestValue, 1)
    }

    @GetMapping("list")
    fun list(httpServletRequest: HttpServletRequest, @RequestHeader("Hash-REST") hashRestValue: String): ResponseEntity<String> {
        return validateHashCashRequest(hashRestValue, 2)
    }

    @GetMapping("upload")
    fun upload(httpServletRequest: HttpServletRequest, @RequestHeader("Hash-REST") hashRestValue: String): ResponseEntity<String> {
        return validateHashCashRequest(hashRestValue, 3)
    }

    @GetMapping("esoklausur")
    fun esoklausur(httpServletRequest: HttpServletRequest, @RequestHeader("Hash-REST") hashRestValue: String): ResponseEntity<String> {
        return validateHashCashRequest(hashRestValue, Int.MAX_VALUE)
    }

    fun validateHashCashRequest(hashRestValue: String, difficulty: Int): ResponseEntity<String> {
        val validHash: Boolean = headerService.validateHeader(hashRestValue, difficulty)
        if (validHash) {
            return ResponseEntity.ok("Valid request!\nYour Header: $hashRestValue.")
        }
        return ResponseEntity("Your hash is invalid!\nYour Header: $hashRestValue", HttpStatus.FORBIDDEN)
    }
}
