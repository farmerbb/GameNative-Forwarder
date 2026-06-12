package com.farmerbb.gamenative

import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.core.net.toFile
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intentData: Uri? = intent?.data
        val finalUri = if (intentData?.scheme == null) {
            intentData?.buildUpon()
                ?.scheme("file")
                ?.build()
        } else intentData

        Log.d(TAG, "onCreate: Received intent with data: $finalUri")

        if (finalUri == null) {
            Log.e(TAG, "No data URI provided in the triggering intent.")
            finish()
            return
        }

        lifecycleScope.launch {
            FileParser.parseGameIdFromUri(contentResolver, finalUri)
                .onSuccess { appId ->
                    Log.i(TAG, "Successfully parsed game ID: $appId. Forwarding intent...")

                    // See IntentLaunchManager.kt and FrontendSyncManagerTest.kt in the GameNative source
                    val extension = finalUri.toFile().extension
                    val gameSource = when (extension) {
                        "pcgame" -> "custom_game"
                        else -> extension
                    }

                    forwardToGame(appId, gameSource)
                }
                .onFailure { exception ->
                    Log.e(TAG, "Failed to parse game ID from URI: $finalUri", exception)
                }

            finish()
        }
    }

    private fun forwardToGame(appId: Int, gameSource: String) = try {
        val componentName = "app.gamenative/.MainActivity"
        Intent().apply {
            component = ComponentName.unflattenFromString(componentName)
            action = "app.gamenative.LAUNCH_GAME"
            putExtra("app_id", appId)
            putExtra("game_source", gameSource)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }.also { targetIntent ->
            startActivity(targetIntent)
            Log.i(TAG, "Target game launcher intent dispatched successfully for app_id: $appId, game_source: $gameSource")
        }
    } catch (e: Exception) {
        Log.e(TAG, "Failed to launch target activity: $componentName", e)
    }

    companion object {
        private const val TAG = "GameNativeForwarder"
    }
}
