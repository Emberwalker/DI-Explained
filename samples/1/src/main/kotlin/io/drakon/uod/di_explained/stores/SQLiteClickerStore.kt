package io.drakon.uod.di_explained.stores

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import org.slf4j.LoggerFactory

import io.drakon.uod.di_explained.use

/**
 * Clicker store based on the SQLite embedded database.
 *
 * Note that this is *not* meant to be efficient, just very simple!
 *
 * Passing SQLiteClickerStore.DB_MEMORY as the dbPath will use an entirely in-memory database.
 */
class SQLiteClickerStore(dbPath: String = SQLiteClickerStore.DEFAULT_DB_PATH): IClickerStore {

    private val LOGGER = LoggerFactory.getLogger(this.javaClass)
    private val conn = DriverManager.getConnection("jdbc:sqlite:$dbPath")

    init {
        setupDB()
    }

    private fun setupDB() {
        val stmt = conn.createStatement()
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS scores (uuid string PRIMARY KEY, score int);")
    }

    override fun getScore(uuid: String): Int {
        conn.prepareStatement("SELECT score FROM scores WHERE uuid = ?;").use {
            it.setString(1, uuid)
            it.executeQuery().use {
                try {
                    if (it.isClosed) return 0
                    return it.getInt("score")
                } catch (ex: SQLException) {
                    LOGGER.error("SQL Exception in getScore.", ex)
                    return 0
                }
            }
        }
    }

    override fun incrementScore(uuid: String): Int {
        val score = getScore(uuid) + 1
        conn.prepareStatement("INSERT OR REPLACE INTO scores (uuid, score) VALUES (?, ?);").use {
            it.setString(1, uuid)
            it.setInt(2, score)
            it.executeUpdate()
        }
        return score
    }

    @Suppress("unused")
    companion object {
        private const val DEFAULT_DB_PATH = "clicker.sqlite"
        const val DB_MEMORY = ":memory:"
    }

}
