package com.ankitkumar.securechatapplication

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.ankitkumar.securechatapplication.adapter.GroupDetailListAdapter
import com.ankitkumar.securechatapplication.databinding.FragmentGroupDetailBinding
import com.ankitkumar.securechatapplication.viewmodel.ChatViewModel
import org.koin.android.ext.android.inject

class GroupDetailFragment: Fragment(R.layout.fragment_group_detail) {
    lateinit var bindings: FragmentGroupDetailBinding
    val arg by navArgs<GroupDetailFragmentArgs>()
    val chatViewModel by inject<ChatViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindings = FragmentGroupDetailBinding.bind(view)
        val groupId = arg.groupId
        bindings.toolbar.logoImageView.setImageResource(R.drawable.ic_group)
        chatViewModel.fetchGroupById(groupId)
        chatViewModel.groupDetailLiveData.observe(viewLifecycleOwner) {
            it?.let {
                bindings?.apply {
                    toolbar.titleTextView.text = it.name
                    progressBar.visibility = View.GONE
                    memberList.layoutManager = LinearLayoutManager(requireContext())
                    memberList.adapter = GroupDetailListAdapter(it.members)
                    groupName.text = it.name
                }
            }
        }
    }
}