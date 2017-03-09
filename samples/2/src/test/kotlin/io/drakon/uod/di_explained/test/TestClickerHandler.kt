package io.drakon.uod.di_explained.test

import com.google.inject.*
import io.drakon.uod.di_explained.handlers.ClickerHandler
import io.drakon.uod.di_explained.handlers.IResponder
import io.drakon.uod.di_explained.handlers.WSResponseScore
import io.drakon.uod.di_explained.stores.IClickerStore
import io.drakon.uod.di_explained.stores.SQLiteClickerStore
import org.junit.Assert.*
import org.junit.Test

/**
 * Clicker Handler tests.
 */
class TestClickerHandler {

    private val DUMMY_UUID = "0xDEADBEEF"
    private val injector = Guice.createInjector(Module {
        it.bind(IClickerStore::class.java).toProvider(Provider { SQLiteClickerStore(SQLiteClickerStore.DB_MEMORY) })
    })

    inner class TestResponder: IResponder {
        var obj: Any? = null
        override fun respond(obj: Any) { this.obj = obj }
    }

    @Test
    fun testClick() {
        val inst = injector.getInstance(ClickerHandler::class.java)
        val resp = TestResponder()

        inst.click(resp, DUMMY_UUID)
        val respMsgA = resp.obj as? WSResponseScore
        assertNotNull(respMsgA)
        assertEquals(respMsgA!!.score, 1)

        inst.click(resp, DUMMY_UUID)
        val respMsgB = resp.obj as? WSResponseScore
        assertNotNull(respMsgB)
        assertEquals(respMsgB!!.score, 2)
    }

    @Test
    fun testSendScore() {
        val inst = injector.getInstance(ClickerHandler::class.java)
        val resp = TestResponder()

        inst.click(resp, DUMMY_UUID)
        inst.sendScore(resp, DUMMY_UUID)
        val respMsg = resp.obj as? WSResponseScore
        assertNotNull(respMsg)
        assertEquals(respMsg!!.score, 1)
    }

}