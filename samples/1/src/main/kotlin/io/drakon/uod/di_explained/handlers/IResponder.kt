package io.drakon.uod.di_explained.handlers

/**
 * Interface for objects which allow return-to-sender.
 */
interface IResponder {

    fun respond(obj: Any)

}