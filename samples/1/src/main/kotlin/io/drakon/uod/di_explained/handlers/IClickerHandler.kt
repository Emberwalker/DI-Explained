package io.drakon.uod.di_explained.handlers

/**
 * Interface for clicker handlers.
 */
interface IClickerHandler {

    fun click(responder: IResponder, uuid: String)
    fun sendScore(responder: IResponder, uuid: String)

}