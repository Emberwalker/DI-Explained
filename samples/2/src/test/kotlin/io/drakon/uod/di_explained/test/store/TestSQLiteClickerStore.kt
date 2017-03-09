package io.drakon.uod.di_explained.test.store

import io.drakon.uod.di_explained.stores.IClickerStore
import io.drakon.uod.di_explained.stores.SQLiteClickerStore

class TestSQLiteClickerStore: TestClickerStore() {

    override fun getNewStore(): IClickerStore {
        return SQLiteClickerStore(SQLiteClickerStore.DB_MEMORY)
    }

}