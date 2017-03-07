package io.drakon.uod.di_explained.test.store

import io.drakon.uod.di_explained.stores.IClickerStore
import org.junit.Test
import kotlin.test.assertEquals

/**
 * Common tests for all ClickerStore impls.
 */
abstract class TestClickerStore {

    companion object {
        protected const val DUMMY_UUID = "0xDEADBEEF"
    }

    protected abstract fun getNewStore(): IClickerStore

    @Test fun testNonexistantUUID() {
        val store = getNewStore()
        val score = store.getScore(DUMMY_UUID)
        assertEquals(0, score)
    }

    @Test fun testNewUUIDSubmission() {
        val store = getNewStore()
        val score = store.incrementScore(DUMMY_UUID)
        assertEquals(1, score)
        val fetchScore = store.getScore(DUMMY_UUID)
        assertEquals(1, fetchScore)
    }

    @Test fun testIncrementExisting() {
        val store = getNewStore()
        store.incrementScore(DUMMY_UUID)
        val score = store.incrementScore(DUMMY_UUID)
        assertEquals(2, score)
        val fetchScore = store.getScore(DUMMY_UUID)
        assertEquals(2, fetchScore)
    }

}