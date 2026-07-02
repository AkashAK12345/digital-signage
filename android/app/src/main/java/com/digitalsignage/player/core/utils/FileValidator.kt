package com.digitalsignage.player.core.utils

import java.io.File
import java.io.FileInputStream
import java.security.MessageDigest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileValidator @Inject constructor() {
    
    fun validateFile(file: File, expectedMd5: String?, expectedSha256: String?, expectedSize: Long? = null): Boolean {
        if (!file.exists()) return false
        
        if (expectedSize != null && expectedSize > 0L && file.length() != expectedSize) {
            return false
        }
        
        if (!expectedSha256.isNullOrBlank()) {
            val sha256 = calculateHash(file, "SHA-256")
            if (!sha256.equals(expectedSha256, ignoreCase = true)) return false
        } else if (!expectedMd5.isNullOrBlank()) {
            val md5 = calculateHash(file, "MD5")
            if (!md5.equals(expectedMd5, ignoreCase = true)) return false
        }
        
        return true
    }
    
    private fun calculateHash(file: File, algorithm: String): String {
        val digest = MessageDigest.getInstance(algorithm)
        val inputStream = FileInputStream(file)
        val buffer = ByteArray(8192)
        var bytesRead: Int
        
        try {
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                digest.update(buffer, 0, bytesRead)
            }
        } finally {
            inputStream.close()
        }
        
        val md5Bytes = digest.digest()
        return md5Bytes.joinToString("") { "%02x".format(it) }
    }
}
