package br.org.cesar.discordtime.apkanalyzer

class MainApp {
    fun run(args: Array<String>) {
        ArgumentReader().read(args).let { argument ->
            if (!argument.isValid()) {
                println(ArgumentReader.USAGE_MESSAGE)
            } else {
                // TODO: Implement
            }
        }
    }
}

fun main(args: Array<String>) {
    MainApp().run(args)
}
