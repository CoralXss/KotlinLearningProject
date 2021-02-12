package com.google.sample.sunflower.utils

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader

fun getContentFromAsserts(context: Context?, fileName: String): String {
    val inputStream = context?.assets?.open(fileName)
    val bufferReader = BufferedReader(InputStreamReader(inputStream))
    var sb = StringBuffer()
    var line = ""
    while ((line == bufferReader.readLine()) != null) {
        sb.append(line)
    }
    bufferReader.close()
    inputStream?.close()
    return sb.toString()
}