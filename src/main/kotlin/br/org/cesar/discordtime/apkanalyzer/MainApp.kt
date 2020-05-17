package br.org.cesar.discordtime.apkanalyzer

import java.io.IOException

import br.org.cesar.discordtime.apkanalyzer.decode.ApkSmaliDecoder

class MainApp {
    companion object {
        const val DEFAULT_ANDROID_VERSION = 28;
    }

    fun run(args: Array<String>) {
        ArgumentReader().read(args).let { argument ->
            if (!argument.isValid()) {
                println(ArgumentReader.USAGE_MESSAGE)
            } else {
                decodeApk(argument)
                // TODO: Get all the invoke methods in the project.
            }
        }
    }

    private fun decodeApk(argument: Argument): Boolean {
        println("Decode APK: ${argument.apkFilePath}")
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

        println("Decode APK completed: $result")
        return result
    }
}

fun main(args: Array<String>) = MainApp().run(args)

