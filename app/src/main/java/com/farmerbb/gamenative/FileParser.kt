package com.farmerbb.gamenative

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

object FileParser {
    private const val TAG = "FileParser"

    suspend fun parseGameIdFromUri(contentResolver: ContentResolver, uri: Uri): Result<Int> = withContext(Dispatchers.IO) {
        try {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                BufferedReader(InputStreamReader(inputStream)).use { reader ->
                    val fileContent = reader.readText().trim()
                    Log.d(TAG, "File content read: '$fileContent'")
                    
                    if (fileContent.isEmpty()) {
                        return@withContext Result.failure(IllegalArgumentException("File is empty"))
                    }
                    
                    fileContent.toIntOrNull()
                        ?.let { Result.success(it) }
                        ?: Result.failure(NumberFormatException("Content '$fileContent' is not a valid integer"))
                }
            } ?: Result.failure(Exception("Failed to open input stream for URI: $uri"))
        } catch (e: Exception) {
            Log.e(TAG, "Error reading or parsing URI: $uri", e)
            Result.failure(e)
        }
    }
}
