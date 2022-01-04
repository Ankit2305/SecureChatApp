package com.ankitkumar.securechatapplication.crypto

import java.lang.Exception
import java.nio.charset.StandardCharsets
import java.security.spec.KeySpec
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

internal object AES {
    private val SECRET_KEY = "my_super_secret_key_ho_ho_ho"
    private val SALT = "ssshhhhhhhhhhh!!!!"

    fun encrypt(strToEncrypt: String): String? {
        try {
            val iv = byteArrayOf(
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
            )
            val ivspec = IvParameterSpec(iv)

            val factory = SecretKeyFactory.getInstance(
                "PBKDF2WithHmacSHA256"
            )

            val spec: KeySpec = PBEKeySpec(
                AES.SECRET_KEY.toCharArray(), AES.SALT.toByteArray(),
                65536, 256
            )
            val tmp = factory.generateSecret(spec)
            val secretKey = SecretKeySpec(
                tmp.encoded, "AES"
            )
            val cipher = Cipher.getInstance(
                "AES/CBC/PKCS5Padding"
            )
            cipher.init(
                Cipher.ENCRYPT_MODE, secretKey,
                ivspec
            )

            return Base64.getEncoder().encodeToString(
                cipher.doFinal(
                    strToEncrypt.toByteArray(
                        StandardCharsets.UTF_8
                    )
                )
            )
        } catch (e: Exception) {
            println(
                "Error while encrypting: "
                        + e.toString()
            )
        }
        return null
    }


    fun decrypt(strToDecrypt: String?): String? {
        try {


            val iv = byteArrayOf(
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
            )

            val ivspec = IvParameterSpec(iv)


            val factory = SecretKeyFactory.getInstance(
                "PBKDF2WithHmacSHA256"
            )


            val spec: KeySpec = PBEKeySpec(
                AES.SECRET_KEY.toCharArray(), AES.SALT.toByteArray(),
                65536, 256
            )
            val tmp = factory.generateSecret(spec)
            val secretKey = SecretKeySpec(
                tmp.encoded, "AES"
            )
            val cipher = Cipher.getInstance(
                "AES/CBC/PKCS5PADDING"
            )
            cipher.init(
                Cipher.DECRYPT_MODE, secretKey,
                ivspec
            )

            return String(
                cipher.doFinal(
                    Base64.getDecoder().decode(strToDecrypt)
                )
            )
        } catch (e: Exception) {
            println(
                ("Error while decrypting: "
                        + e.toString())
            )
        }
        return null
    }
}
