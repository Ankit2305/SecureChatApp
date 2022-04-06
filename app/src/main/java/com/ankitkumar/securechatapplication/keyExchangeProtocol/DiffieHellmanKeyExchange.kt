package com.ankitkumar.securechatapplication.keyExchangeProtocol

import java.lang.Math.pow

class DiffieHellmanKeyExchange {

    companion object{
        fun generatePublicKeyForExchange(privateKey : Long, g: Int, n: Int): Pair<Long, Long>{
            //returns a pair of private and publicKey
            return Pair( privateKey, (pow(g.toDouble(), privateKey.toDouble())%n).toLong())
        }

        fun computeCommonSecretKey(publicKeyReceived : Long, privateKey: Long, n: Int ): Long{
            return (pow(publicKeyReceived.toDouble(),privateKey.toDouble())%n).toLong()
        }
    }

}


