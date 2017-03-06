package io.drakon.uod.di_explained

import io.drakon.uod.di_explained.handlers.ClickerWebSocket
import org.slf4j.LoggerFactory
import spark.Filter
import spark.Spark.*
import spark.route.RouteOverview
import java.util.*
import kotlin.jvm.javaClass

/**
 * Application entrypoint.
 */
class Main {

    private val LOGGER = LoggerFactory.getLogger(this.javaClass)

    /**
     * Main place to start the application. Register routes here.
     */
    fun start() {
        // Load static files in src/main/resources/public
        staticFiles.location("/public")

        // Setup WebSocket routes
        webSocket("/ws", ClickerWebSocket::class.java)

        // Setup before filter, which gets/sets client UUID
        before(Filter { request, response ->
            var uuid = request.cookie("uuid")
            if (uuid == null) {
                uuid = UUID.randomUUID().toString()
                response.cookie("/", "uuid", uuid, -1, false, false)
                LOGGER.debug("Generated new UUID: {}", uuid)
            } else {
                LOGGER.debug("Using existing UUID: {}", uuid)
            }
            request.session().attribute("uuid", uuid)
        })

        LOGGER.debug("Registering debug route at /debug/routes ( http://localhost:4567/debug/routes )")
        RouteOverview.enableRouteOverview("/debug/routes")

        LOGGER.debug("Initialising routes.")
        get("/ping", { req, _ -> "{\"type\": \"ping\", \"uuid\": \"${req.session().attribute<String>("uuid")}\"}" })

        LOGGER.info("Startup finished. The server will normally be exposed at: http://localhost:4567/")
    }

    companion object {
        /**
         * This is equivalent to a Java public static main method. @JvmStatic makes sure it's exposed properly.
         *
         * This is <b>not</b> recommended style for Kotlin projects.
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val m = Main()
            m.start()
        }
    }

}
