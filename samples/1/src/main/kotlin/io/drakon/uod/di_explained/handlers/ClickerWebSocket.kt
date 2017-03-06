package io.drakon.uod.di_explained.handlers

import com.google.gson.GsonBuilder
import org.eclipse.jetty.websocket.api.Session
import org.eclipse.jetty.websocket.api.annotations.*
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap

/**
 * Web Socket handler for the Clicker game.
 */
@WebSocket
class ClickerWebSocket {

    private val LOGGER = LoggerFactory.getLogger(this.javaClass)
    private val sessionUUIDs = ConcurrentHashMap<Session, String>()

    @OnWebSocketConnect
    fun connected(session: Session) {
        LOGGER.debug("WebSocket connected. (Session: {})", session.hashCode())
        val uuid = session.upgradeRequest.cookies.filter { it.name == "uuid" }.map { it.value }.firstOrNull()
        if (uuid != null) {
            sessionUUIDs.put(session, uuid)
            LOGGER.debug("WS Request contained UUID cookie: {} -> {}", uuid, session.hashCode())
        }
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
                "announce" -> handleAnnounce(session, GSON.fromJson(msg, WSMessageAnnounce::class.java))
                "click" -> handleClick(session)
                else -> session.send(WSResponseError("invalid_msg_type"))
            }
        } catch (ex: Exception) {
            // TODO make more specific.
            session.send(WSResponseError("parse_error"))
            LOGGER.error("Error parsing JSON response.", ex)
        }
    }

    private fun handleAnnounce(session: Session, annMsg: WSMessageAnnounce) {
        sessionUUIDs.put(session, annMsg.uuid)
        LOGGER.debug("WS Announce: {} -> {}", annMsg.uuid, session.hashCode())
        session.send(WSResponseAnnounce())
    }

    private fun handleClick(session: Session) {
        val uuid = sessionUUIDs[session]
        if (uuid == null) {
            session.send(WSResponseError("unannounced"))
            return
        }
        // TODO
    }

    companion object {
        private val GSON = GsonBuilder().setPrettyPrinting().create()

        private data class WSMessageBase(val type: String)
        private data class WSMessageAnnounce(val type: String, val uuid: String)
        private data class WSResponseAnnounce(val type: String = "announce_ack")
        private data class WSResponseError(val reason: String, val type: String = "error")

        // Extension methods are awesome.
        private fun Session.send(obj: Any) {
            this.remote.sendString(GSON.toJson(obj))
        }
    }

}