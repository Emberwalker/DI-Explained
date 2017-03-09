package io.drakon.uod.di_explained.handlers

import com.google.inject.Inject
import io.drakon.uod.di_explained.stores.IClickerStore

/**
 * Clicker Handler.
 */
class ClickerHandler @Inject constructor(private val clickerStore: IClickerStore): IClickerHandler {

    override fun click(responder: IResponder, uuid: String) {
        clickerStore.incrementScore(uuid)
        sendScore(responder, uuid)
    }

    override fun sendScore(responder: IResponder, uuid: String) {
        val score = clickerStore.getScore(uuid)
        responder.respond(WSResponseScore(score))
    }

}