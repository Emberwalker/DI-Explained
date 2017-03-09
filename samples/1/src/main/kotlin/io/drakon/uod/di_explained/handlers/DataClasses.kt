package io.drakon.uod.di_explained.handlers

/**
 * Common data classes.
 */

data class WSMessageBase(val type: String)
data class WSMessageAnnounce(val type: String, val uuid: String)
data class WSResponseAnnounce(val type: String = "announce_ack")
data class WSResponseError(val reason: String, val type: String = "error")
data class WSResponseScore(val score: Int, val type: String = "score")
