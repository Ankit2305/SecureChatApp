package com.ankitkumar.securechatapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.ankitkumar.securechatapplication.model.User


class SharedViewModel : ViewModel() {
    val selected = MutableLiveData<User>()

    fun select(user: User) {
        selected.value = user
    }
}