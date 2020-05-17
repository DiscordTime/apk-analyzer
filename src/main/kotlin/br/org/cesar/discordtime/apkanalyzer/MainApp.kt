package br.org.cesar.discordtime.apkanalyzer

import java.io.IOException

import br.org.cesar.discordtime.apkanalyzer.decode.ApkSmaliDecoder

class MainApp {
    companion object {
        const val DEFAULT_ANDROID_VERSION = 28;
        const val DEBUG: Boolean = false;
    }

    fun run(args: Array<String>) {
        ArgumentReader().read(args).let { argument ->
            if (!argument.isValid()) {
                println(ArgumentReader.USAGE_MESSAGE)
            } else {
                val success = decodeApk(argument)
                if (success) {
                    getInvokedMethods(argument)
                }
            }
        }
    }

    private fun decodeApk(argument: Argument): Boolean {
        if (DEBUG) println("Decode APK: ${argument.apkFilePath}")
        var result: Boolean = true
        try {
            ApkSmaliDecoder().decode(
                argument.apkFilePath,
                argument.decodeOutput,
                DEFAULT_ANDROID_VERSION
            )
        } catch(e: IOException) {
            result = false
            e.printStackTrace()
        }

        if (DEBUG) println("Decode APK completed: $result")
        return result
    }

    private fun getInvokedMethods(argument: Argument) {
        SmaliAnalyzer()
            .getInvokedMethods(argument.decodeOutput)
            .forEach { println(it) }
    }
}

fun main(args: Array<String>) = MainApp().run(args)

