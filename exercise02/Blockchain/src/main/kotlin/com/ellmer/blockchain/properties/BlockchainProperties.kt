package com.ellmer.blockchain.properties

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "blockchain")
class BlockchainProperties {
    @Value("\${blockchain.adjustableDifficulty:true}")
    var adjustableDifficulty: Boolean = true

    @Value("\${blockchain.maxDifficulty:255}")
    var maxDifficulty: Int = 255

    @Value("\${blockchain.targetBlockDurationSeconds:10}")
    var targetBlockDurationSeconds: Int = 10
}
