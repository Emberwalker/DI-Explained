package io.drakon.uod.di_explained

import com.google.gson.GsonBuilder
import org.eclipse.jetty.websocket.api.Session

/**
 * Extension methods not provided by Kotlin stdlib.
 */

// Patch of https://github.com/JetBrains/kotlin/blob/323df9498a5818ff04a7ab98190805dcfebfd5b6/libraries/stdlib/src/kotlin/io/ReadWrite.kt#L247
// which works with AutoCloseable rather than just Closeable.
inline fun <T : AutoCloseable, R> T.use(block: (T) -> R): R {
    var closed = false
    try {
        return block(this)
    } catch (e: Exception) {
        closed = true
        try {
            this.close()
        } catch (closeException: Exception) {
            e.addSuppressed(closeException)
        }
        throw e
    } finally {
        if (!closed) {
            this.close()
        }
    }
}


// Extension method to make sending messages back over websockets simpler.
private val GSON = GsonBuilder().setPrettyPrinting().create()
fun Session.send(obj: Any) {
    this.remote.sendString(GSON.toJson(obj))
}