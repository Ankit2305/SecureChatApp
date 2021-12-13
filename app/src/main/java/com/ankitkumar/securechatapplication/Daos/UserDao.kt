package com.ankitkumar.securechatapplication.Daos

import com.ankitkumar.securechatapplication.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserDao {
    val db = FirebaseFirestore.getInstance()
    val userCollection = db.collection("user")

    fun addUser(user : User?){
        user?.let {
            GlobalScope.launch (Dispatchers.IO){
                userCollection.document(user.uid).set(it)


            }

        }
    }
}