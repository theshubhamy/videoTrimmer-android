package com.awesomeproject

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.awesomeproject.databinding.ActivityVideoTrimmingBinding
import com.redevrx.video_trimmer.event.OnVideoEditedEvent
import java.io.File


class VideoTrimmingActivity : AppCompatActivity(), OnVideoEditedEvent {
    private lateinit var FOLDER_PATH_TRIM_VIDEO_SAVER: File
    private var _binding: ActivityVideoTrimmingBinding? = null
    private val binding get() = _binding!!
    private lateinit var progressDialog: ProgressDialog

    companion object {
        @JvmStatic
        fun startActivityForResult(context: Activity, path: String, resultCode: Int) {
            val starter = Intent(context, VideoTrimmingActivity::class.java)
                .putExtra("videoPath", path)
            context.startActivityForResult(starter, resultCode)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityVideoTrimmingBinding.inflate(LayoutInflater.from(this))
        setContentView(_binding?.root)

        val path = intent.getStringExtra("videoPath") ?: ""
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Crop")
        progressDialog.setCancelable(false)

//        FOLDER_PATH_TRIM_VIDEO_SAVER = File(
//            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES),
//            "Hiyvee"
//        )

        FOLDER_PATH_TRIM_VIDEO_SAVER = this.getExternalFilesDir(null)!!

        if (!FOLDER_PATH_TRIM_VIDEO_SAVER.exists())
            FOLDER_PATH_TRIM_VIDEO_SAVER.mkdir()

        try {
            setupVideoTrimmer(path)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        binding.btnSaveVideo.setOnClickListener {
            binding.videoTrimmer.saveVideo()
            progressDialog.show()
        }

    }

    private fun setupVideoTrimmer(path: String) {
        val selectedUri = Uri.parse(path)
        binding.videoTrimmer.apply {
            setVideoBackgroundColor(Color.WHITE)
            setOnTrimVideoListener(this@VideoTrimmingActivity)
            setVideoURI(selectedUri)
            setDestinationPath(FOLDER_PATH_TRIM_VIDEO_SAVER.absolutePath)
            setVideoInformationVisibility(true)
            setMaxDuration(30)
            setMinDuration(0)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    fun getUriForFile(context: Context, file: File): Uri {
        return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    }
    override fun getResult(uri: Uri) {
        progressDialog.dismiss()
        val intent = Intent()
        intent.putExtra("TRIMMED_VIDEO_URI", uri.toString())
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onError(message: String) {
        progressDialog.dismiss()
        setResult(Activity.RESULT_CANCELED)
        finish()
    }
}