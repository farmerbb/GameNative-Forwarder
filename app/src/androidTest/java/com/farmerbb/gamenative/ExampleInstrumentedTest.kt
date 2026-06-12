package com.farmerbb.gamenative

import android.net.Uri
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

/**
 * Instrumented test, which will execute on an Android device/emulator.
 * Verifies the file processing and parsing logic in [FileParser].
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @Test
    fun useAppContext() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.farmerbb.gamenative", appContext.packageName)
    }

    @Test
    fun testParseValidInteger(): Unit = runBlocking {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val file = File(appContext.cacheDir, "valid_int.txt")
        file.writeText("42")
        val uri = Uri.fromFile(file)

        val result = FileParser.parseGameIdFromUri(appContext.contentResolver, uri)
        assertTrue(result.isSuccess)
        assertEquals(42, result.getOrNull())

        file.delete()
    }

    @Test
    fun testParseValidIntegerWithWhitespace(): Unit = runBlocking {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val file = File(appContext.cacheDir, "whitespace_int.txt")
        file.writeText(" \n  999 \r\n ")
        val uri = Uri.fromFile(file)

        val result = FileParser.parseGameIdFromUri(appContext.contentResolver, uri)
        assertTrue(result.isSuccess)
        assertEquals(999, result.getOrNull())

        file.delete()
    }

    @Test
    fun testParseEmptyFile(): Unit = runBlocking {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val file = File(appContext.cacheDir, "empty.txt")
        file.writeText("")
        val uri = Uri.fromFile(file)

        val result = FileParser.parseGameIdFromUri(appContext.contentResolver, uri)
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)

        file.delete()
    }

    @Test
    fun testParseNonInteger(): Unit = runBlocking {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val file = File(appContext.cacheDir, "non_int.txt")
        file.writeText("not_a_number")
        val uri = Uri.fromFile(file)

        val result = FileParser.parseGameIdFromUri(appContext.contentResolver, uri)
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is NumberFormatException)

        file.delete()
    }

    @Test
    fun testParseNonExistentFile(): Unit = runBlocking {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val uri = Uri.fromFile(File(appContext.cacheDir, "non_existent_file_12345.txt"))

        val result = FileParser.parseGameIdFromUri(appContext.contentResolver, uri)
        assertTrue(result.isFailure)
    }
}
