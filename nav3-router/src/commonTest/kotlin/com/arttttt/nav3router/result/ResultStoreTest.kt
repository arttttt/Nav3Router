package com.arttttt.nav3router.result

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ResultStoreTest {

    @Test
    fun `GIVEN an empty store WHEN storing a value THEN it can be taken exactly once`() {
        // GIVEN
        val store = ResultStore.empty()

        // WHEN
        store.store("k", "v")

        // THEN
        assertTrue(store.has("k"))
        assertEquals("v", store.take("k"))
        // consume-once: gone after take
        assertFalse(store.has("k"))
        assertNull(store.take("k"))
    }

    @Test
    fun `GIVEN a stored value WHEN storing again under the same key THEN it is overwritten`() {
        // GIVEN
        val store = ResultStore.empty()
        store.store("k", "a")

        // WHEN
        store.store("k", "b")

        // THEN
        assertEquals("b", store.take("k"))
    }

    @Test
    fun `GIVEN a value under one key WHEN reading a different key THEN it is empty`() {
        // GIVEN
        val store = ResultStore.empty()
        store.store("x", "1")

        // WHEN / THEN
        assertFalse(store.has("y"))
        assertNull(store.take("y"))
        assertEquals("1", store.take("x"))
    }

    @Test
    fun `GIVEN a populated store WHEN restoring from its snapshot THEN the contents survive`() {
        // GIVEN
        val store = ResultStore.empty()
        store.store("a", "1")
        store.store("b", "2")

        // WHEN
        val restored = ResultStore.restore(store.snapshot())

        // THEN
        assertEquals("1", restored.take("a"))
        assertEquals("2", restored.take("b"))
    }
}
