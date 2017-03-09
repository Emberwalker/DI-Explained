package io.drakon.uod.di_explained.handlers

import io.drakon.uod.di_explained.send
import org.eclipse.jetty.websocket.api.Session

/**
 * WebSocket implementation of IResponder.
 */
class WSResponder(val session: Session): IResponder {

    override fun respond(obj: Any) {
        session.send(obj)
    }

}