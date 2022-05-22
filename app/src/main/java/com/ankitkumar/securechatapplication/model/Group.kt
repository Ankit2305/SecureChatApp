package com.ankitkumar.securechatapplication.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Group(
    val groupTitle: String,
    val members: List<User>
): Parcelable