package com.ankitkumar.securechatapplication.keyExchangeProtocol

import android.content.Context

class GenerateKeyBundle(
    val identityKey: Long
) {

    fun generateKeyBundleAtInstall(g: Int, n: Int): List<Pair<Long, Long>> {
        val identityKeyPair =
            DiffieHellmanKeyExchange.generatePublicKeyForExchange(identityKey, g, n)

        val signedPreKeyPair = DiffieHellmanKeyExchange.generatePublicKeyForExchange((System.currentTimeMillis())%identityKey, g,n)

        return listOf(identityKeyPair, signedPreKeyPair)
    }


}