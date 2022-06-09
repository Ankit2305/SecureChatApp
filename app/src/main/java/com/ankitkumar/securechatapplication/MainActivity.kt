package com.ankitkumar.securechatapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.ankitkumar.securechatapplication.network.WebSocketWrapper
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.koin.android.ext.android.inject
import java.util.*


class MainActivity : AppCompatActivity() {
    val webSocketWrapper by inject<WebSocketWrapper>()

    companion object {
        const val PICK_IMAGE_REQUEST_CODE = 1000
    }

    var filePath: Uri? = null
    var fragment: ChatFragment? = null
    private var storageReference: StorageReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_SecureChatApplication)
        setContentView(R.layout.activity_main)
        storageReference = FirebaseStorage.getInstance().reference
        webSocketWrapper.connect()
    }

    override fun onDestroy() {
        webSocketWrapper.close()
        super.onDestroy()
    }

    fun showFileSelector() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(
            Intent.createChooser(intent, "Select an image"),
            MainActivity.PICK_IMAGE_REQUEST_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            PICK_IMAGE_REQUEST_CODE -> {
                if (resultCode == RESULT_OK && data != null && data.data != null) {
                    filePath = data.data
                    fragment?.handleFileUri(filePath)
                }
            }
        }

    }
}