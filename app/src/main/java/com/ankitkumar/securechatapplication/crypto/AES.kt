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
    // Class private variables
    private val SECRET_KEY = "my_super_secret_key_ho_ho_ho"
    private val SALT = "ssshhhhhhhhhhh!!!!"

    // This method use to encrypt to string
    fun encrypt(strToEncrypt: String): String? {
        try {

            // Create default byte array
            val iv = byteArrayOf(
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
            )
            val ivspec = IvParameterSpec(iv)

            // Create SecretKeyFactory object
            val factory = SecretKeyFactory.getInstance(
                "PBKDF2WithHmacSHA256"
            )

            // Create KeySpec object and assign with
            // constructor
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
            // Return encrypted string
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

    // This method use to decrypt to string
    fun decrypt(strToDecrypt: String?): String? {
        try {

            // Default byte array
            val iv = byteArrayOf(
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
            )
            // Create IvParameterSpec object and assign with
            // constructor
            val ivspec = IvParameterSpec(iv)

            // Create SecretKeyFactory Object
            val factory = SecretKeyFactory.getInstance(
                "PBKDF2WithHmacSHA256"
            )

            // Create KeySpec object and assign with
            // constructor
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
            // Return decrypted string
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
