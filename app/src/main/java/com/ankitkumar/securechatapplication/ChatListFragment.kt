package com.ankitkumar.securechatapplication

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ankitkumar.securechatapplication.Daos.UserDao
import com.ankitkumar.securechatapplication.adapter.ChatListAdapter
import com.ankitkumar.securechatapplication.databinding.FragmentChatListBinding
import com.ankitkumar.securechatapplication.model.User
import com.ankitkumar.securechatapplication.util.AUTH_CODE
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class ChatListFragment: Fragment(R.layout.fragment_chat_list) {

    lateinit var bindings: FragmentChatListBinding
    lateinit var chatListAdapter : ChatListAdapter
    private val viewModel : SharedViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindings = FragmentChatListBinding.bind(view)
        setupChatListAdapter()
    }

    private fun setupChatListAdapter() {
        val sharedPref = activity?.getSharedPreferences(resources.getString(R.string.app_name),
            Context.MODE_PRIVATE)
        val authCode = sharedPref?.getString(AUTH_CODE,"12345")

        val userCollection = UserDao().userCollection       //get userList from db collection
        var query = userCollection.orderBy("uid").whereNotEqualTo("uid", authCode)      //sort users based on their name
        query.orderBy("name")  //not working , only sorted according to "uid"

        val recyclerViewOption = FirestoreRecyclerOptions.Builder<User>().setQuery(query,User::class.java).build()
        chatListAdapter = ChatListAdapter(recyclerViewOption)
        bindings.chatListRecyclerView.adapter = chatListAdapter
        bindings.chatListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        chatListAdapter.onItemClick = { User ->
         //   viewModel.select(User)

            val bundle=  Bundle()
            bundle.putString("name",User.name)
            bundle.putString("phoneNo",User.phoneNo)
            bundle.putString("UID",User.uid)
            fragmentManager?.beginTransaction()?.replace(R.id.nav_host_fragment_container,ChatFragment())?.commit()

//            view?.findNavController()?.navigate(R.id.action_chatListFragment_to_chatFragment)
        }
    }

    override fun onStart() {
        super.onStart()
        chatListAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        chatListAdapter.stopListening()
    }

}