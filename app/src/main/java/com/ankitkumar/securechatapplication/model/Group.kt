package com.ankitkumar.securechatapplication.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Group(
    val name: String = "",
    val groupId: String = "",
    val members: List<String> = emptyList()
): Parcelable