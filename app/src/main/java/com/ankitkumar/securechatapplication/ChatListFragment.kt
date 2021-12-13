package com.ankitkumar.securechatapplication

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
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
    //private val viewModel : SharedViewModel by viewModels()
    lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindings = FragmentChatListBinding.bind(view)
        navController = findNavController()
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
        chatListAdapter = ChatListAdapter(recyclerViewOption, object: ChatListAdapter.OnItemClickListener{
            override fun onClick(user: User) {
                val action = ChatListFragmentDirections.actionChatListFragmentToChatFragment(user)
                navController.navigate(action)
            }

        })
        bindings.chatListRecyclerView.adapter = chatListAdapter
        bindings.chatListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
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