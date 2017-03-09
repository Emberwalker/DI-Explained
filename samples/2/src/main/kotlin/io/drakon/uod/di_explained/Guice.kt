package io.drakon.uod.di_explained

import com.google.inject.AbstractModule
import io.drakon.uod.di_explained.handlers.ClickerHandler
import io.drakon.uod.di_explained.handlers.IClickerHandler
import io.drakon.uod.di_explained.stores.IClickerStore
import io.drakon.uod.di_explained.stores.SQLiteClickerStore

/*
 * Guice modules.
 */

/**
 * Guice module for production (non-test impls)
 */
val GUICE_PRODUCTION = object : AbstractModule() {
    override fun configure() {
        // bind(abstract).to(concrete) binds a given abstract/interface class to a concrete class. It will use a
        // zero-argument constructor where possible, or one marked with @Inject if available.
        bind(IClickerHandler::class.java).to(ClickerHandler::class.java)
        bind(IClickerStore::class.java).to(SQLiteClickerStore::class.java)
    }
}