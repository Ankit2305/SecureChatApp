package com.ankitkumar.securechatapplication.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val uid : String ="",
    val name : String= "",
    val phoneNo : String=""
): Parcelable


@Parcelize
data class Group(
    val groupId : String = "",
    val name  : String =  "",
    val members : List<String> = listOf<String>("")
): Parcelable