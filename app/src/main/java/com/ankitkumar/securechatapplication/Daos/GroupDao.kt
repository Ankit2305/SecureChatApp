package com.ankitkumar.securechatapplication.Daos

import com.ankitkumar.securechatapplication.model.Group
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class GroupDao {

    val db = FirebaseFirestore.getInstance()
    val groupCollection = db.collection("groups")

    fun addGroup(group : Group?){
        group?.let {
            GlobalScope.launch (Dispatchers.IO){
                groupCollection.document(group.groupId).set(it)
            }
        }
    }
}