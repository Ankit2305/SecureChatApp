package com.ankitkumar.securechatapplication

import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ankitkumar.securechatapplication.databinding.FragmentImagePreviewBinding
import com.ankitkumar.securechatapplication.viewmodel.ChatViewModel
import com.bumptech.glide.Glide
import org.koin.android.ext.android.inject
import kotlin.Exception

class ImagePreviewFragment : Fragment(R.layout.fragment_image_preview) {
    val args by navArgs<ImagePreviewFragmentArgs>()
    lateinit var bindings: FragmentImagePreviewBinding
    val viewModel by inject<ChatViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindings = FragmentImagePreviewBinding.bind(view)

        val uri = args.imageUri
        val isGroupChat = args.isGroupChat
        val receiverAuth = args.receiverAuth
        val url = args.url

        if (uri != null && receiverAuth != null) {

            viewModel.uploadInProgress.observe(viewLifecycleOwner) {
                bindings.apply {
                    if (it) {
                        progressBar.visibility = View.VISIBLE
                    } else {
                        progressBar.visibility = View.GONE
                    }
                }
            }

            viewModel.imageDownloadLink.observe(viewLifecycleOwner) {
                it?.let { url ->
                    if (url.isNotBlank()) {
                        val message = url.toMessage(
                            isGroupChat = isGroupChat,
                            receiverAuth = receiverAuth,
                            context = requireContext(),
                            isImage = true
                        )
                        viewModel.sendMessage(message)
                        findNavController().popBackStack()
                    }
                }
            }

            try {
                val bitmap =
                    MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)
                bindings.apply {
                    previewImage.setImageBitmap(bitmap)
                    sendButton.setOnClickListener {
                        viewModel.uploadFile(uri)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else if(url != null) {
            bindings.apply {
                sendButton.visibility = View.GONE
                Glide.with(requireContext()).load(url).into(previewImage)
            }
        }
    }
}