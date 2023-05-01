import com.ellmer.blockchains.extensions.sha256
import kotlin.random.Random

class HeaderService {
    val randomLength = 6
    val charPool: List<Char> = ('a'..'z').toList()
    fun generateHashCashHeader(requestUrl: String, difficulty: Int): String {
        val timestamp = System.currentTimeMillis() / 1000
        var header = ""
        val random = generateRandomChars()
        var counter = 0

        do {
            header = "HashREST: $timestamp;$requestUrl;$random;${counter++}"
            val sha = header.sha256()
            val zeros = getNumberOfLeadingZeros(sha)
        } while (zeros < difficulty)

        return header
    }

    fun validateHeader(headerValue: String, difficulty: Int): Boolean {
        val sha = headerValue.sha256()
        return getNumberOfLeadingZeros(sha) >= difficulty
    }

    private fun generateRandomChars(): String {
        return (1..randomLength)
            .map { Random.nextInt(0, charPool.size).let { charPool[it] } }
            .joinToString("")
    }

    private fun getNumberOfLeadingZeros(input: String): Int {
        val regex = Regex("^0+")
        val matchResult = regex.find(input)
        return matchResult?.value?.length ?: 0
    }
}
