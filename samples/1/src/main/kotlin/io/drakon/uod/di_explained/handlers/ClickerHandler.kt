package io.drakon.uod.di_explained.handlers

import io.drakon.uod.di_explained.stores.IClickerStore
import io.drakon.uod.di_explained.stores.SQLiteClickerStore

/**
 * Clicker Handler.
 */
class ClickerHandler: IClickerHandler {

    private val clickerStore: IClickerStore = SQLiteClickerStore()

    override fun click(responder: IResponder, uuid: String) {
        clickerStore.incrementScore(uuid)
        sendScore(responder, uuid)
    }

    override fun sendScore(responder: IResponder, uuid: String) {
        val score = clickerStore.getScore(uuid)
        responder.respond(WSResponseScore(score))
    }

}