package io.drakon.uod.di_explained.handlers

import com.google.gson.GsonBuilder
import com.google.inject.Inject
import io.drakon.uod.di_explained.send
import org.eclipse.jetty.websocket.api.Session
import org.eclipse.jetty.websocket.api.annotations.*
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap

/**
 * Web Socket handler for the Clicker game.
 */
@Suppress("unused", "UNUSED_PARAMETER")
@WebSocket
class ClickerWebSocket @Inject constructor(private val handler: IClickerHandler) {

    private val sessionUUIDs = ConcurrentHashMap<Session, String>()
    private val LOGGER = LoggerFactory.getLogger(this.javaClass)

    @OnWebSocketConnect
    fun connected(session: Session) {
        LOGGER.debug("WebSocket connected. (Session: {})", session.hashCode())
        val uuid = session.upgradeRequest.cookies.filter { it.name == "uuid" }.map { it.value }.firstOrNull()
        if (uuid != null) announce(session, uuid)
    }

    @OnWebSocketClose
    fun closed(session: Session, code: Int, reason: String) {
        if (sessionUUIDs.containsKey(session)) sessionUUIDs.remove(session)
        LOGGER.debug("WebSocket closed. (Session: {})", session.hashCode())
    }

    @OnWebSocketError
    fun error(session: Session, err: Throwable) {
        LOGGER.warn("WebSocket error", err)
    }

    @OnWebSocketMessage
    fun message(session: Session, msg: String) {
        try {
            val base = GSON.fromJson(msg, WSMessageBase::class.java)
            when (base.type.toLowerCase()) {
                "announce" -> announce(session, GSON.fromJson(msg, WSMessageAnnounce::class.java).uuid)
                "click" -> click(session)
                else -> session.send(WSResponseError("invalid_msg_type"))
            }
        } catch (ex: Exception) {
            session.send(WSResponseError("parse_error"))
            LOGGER.error("Error parsing JSON response.", ex)
        }
    }

    private fun click(session: Session) {
        val uuid = sessionUUIDs[session]
        if (uuid == null) {
            session.send(WSResponseError("unannounced"))
            return
        }
        handler.click(WSResponder(session), uuid)
    }

    private fun announce(session: Session, uuid: String) {
        sessionUUIDs.put(session, uuid)
        LOGGER.debug("WS Announce: {} -> {}", uuid, session.hashCode())
        session.send(WSResponseAnnounce())
        handler.sendScore(WSResponder(session), uuid)
    }

    companion object {
        private val GSON = GsonBuilder().setPrettyPrinting().create()
    }

}