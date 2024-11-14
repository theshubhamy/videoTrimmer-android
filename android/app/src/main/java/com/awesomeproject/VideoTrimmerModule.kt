package com.awesomeproject

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.facebook.react.bridge.ActivityEventListener
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod

class VideoTrimmerModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext), ActivityEventListener {

    private var promise: Promise? = null

    init {
        reactContext.addActivityEventListener(this)
    }

    override fun getName(): String {
        return "VideoTrimmer"
    }

    @ReactMethod
    fun openTrimView(videoUri: String, promise: Promise) {
        Log.e("FilePath", "File path: $videoUri")
        this.promise = promise
        val currentActivity = currentActivity
        if (currentActivity == null) {
            promise.reject("ACTIVITY_NOT_AVAILABLE", "Activity doesn't exist")
            return
        }
        VideoTrimmingActivity.startActivityForResult(currentActivity, videoUri, TRIM_VIDEO_REQUEST_CODE)
//        val intent = Intent(reactApplicationContext, VideoTrimmingActivity::class.java)
//        intent.putExtra("VIDEO_URI", videoUri)
//        currentActivity.startActivityForResult(intent, TRIM_VIDEO_REQUEST_CODE)
    }

    override fun onActivityResult(
        activity: Activity?,
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        if (requestCode == TRIM_VIDEO_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val newVideoUri = data?.getStringExtra("TRIMMED_VIDEO_URI") ?: ""
            promise?.resolve(newVideoUri)
        } else {
            promise?.reject("TRIM_FAILED", "Video trimming was not successful.")
        }
    }

    override fun onNewIntent(newIntent: Intent?) {
        // nothing to do
    }

    companion object {
        private const val TRIM_VIDEO_REQUEST_CODE = 1
    }
}
