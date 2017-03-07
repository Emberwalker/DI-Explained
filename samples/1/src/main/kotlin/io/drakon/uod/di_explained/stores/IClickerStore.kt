package io.drakon.uod.di_explained.stores

/**
 * Common interface for score-tracking stores.
 */
interface IClickerStore {

    /**
     * Gets the score for a given UUID.
     */
    fun getScore(uuid: String): Int

    /**
     * Increments the score of a given UUID, and returns the new score.
     */
    fun incrementScore(uuid: String): Int

}