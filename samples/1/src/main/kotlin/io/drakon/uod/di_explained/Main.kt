package io.drakon.uod.di_explained

import spark.Spark.*
import spark.route.RouteOverview

/**
 * Application entrypoint.
 */
class Main {

    /**
     * Main place to start the application. Register routes here.
     */
    fun start() {
        RouteOverview.enableRouteOverview("/debug/routes")
        get("/", { _, _ -> "Hello, World!" })
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
