import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

val headerService = HeaderService()

fun main() {
    sendHashCashRequest("http://localhost:8080/greet", 1)
    sendHashCashRequest("http://localhost:8080/list", 2)
    sendHashCashRequest("http://localhost:8080/upload", 3)

//    Still calculating on my machine...
//    sendHashCashRequest("http://localhost:8080/esoklausur", Int.MAX_VALUE)
}

fun sendHashCashRequest(requestUrl: String, difficulty: Int) {
    val url = URL(requestUrl)
    val conn = url.openConnection() as HttpURLConnection

    conn.requestMethod = "GET"
    conn.setRequestProperty("Hash-REST", headerService.generateHashCashHeader(url.toString(), difficulty))

    println("Sending request to: ${conn.url} ...........")
    println("Response Status: " + conn.responseCode)

    val stream = if (conn.responseCode == 200) conn.inputStream else conn.errorStream
    BufferedReader(InputStreamReader(stream)).use { br ->
        var line: String?
        while (br.readLine().also { line = it } != null) {
            println(line)
        }
    }
    println()
}
