package br.org.cesar.discordtime.apkanalyzer

class ArgumentReader constructor() {
    companion object {
        const val APK_FILE_PATH_MACRO: String = "-a"
        const val DECODE_OUTPUT_MACRO: String = "-o"
        val ARGUMENT_MACROS: Array<String> = arrayOf<String>(
            APK_FILE_PATH_MACRO,
            DECODE_OUTPUT_MACRO
        )

        public const val USAGE_MESSAGE: String =
            "Usage: \n" +
            "  $APK_FILE_PATH_MACRO path: APK file path\n"+
            "  $DECODE_OUTPUT_MACRO path: Decode output directory\n"+
            "e.g:\n" +
            "  -a <APK path> -o <decode output>\n" +
            "  <APK path> <decode output>"
    }

    fun read(args: Array<String>): Argument = args.iterator().run {
        ArgumentBuilder().also { argumentbuilder ->
            while (this.hasNext()) {
                this.next().let { value ->
                    if (isArgumentMacro(value)) {
                        getArgumentMacroValue(argumentbuilder, value, this)
                    } else {
                        argumentbuilder.setValueForNextParameter(value)
                    }
                }
            }
        }.build()
    }

    fun isArgumentMacro(value: String): Boolean = ARGUMENT_MACROS.contains(value)

    fun getArgumentMacroValue(
        argument: ArgumentBuilder,
        value: String,
        iterator: Iterator<String>
    ) {
        if (iterator.hasNext()) {
            iterator.next().let { nextValue ->
                if (isArgumentMacro(nextValue)) {
                    getArgumentMacroValue(argument, nextValue, iterator)
                } else {
                    argument.setValueFromKey(value, nextValue)
                }
            }
        }
    }
}

class ArgumentBuilder {
    private var apkFilePath = ""
    private var decodeOutput = ""

    private var setValues = ArgumentReader.ARGUMENT_MACROS.toMutableList()

    fun setValueFromKey(key: String, value: String) {
        when (key) {
            ArgumentReader.APK_FILE_PATH_MACRO -> apkFilePath = value
            ArgumentReader.DECODE_OUTPUT_MACRO -> decodeOutput = value
        }

        setValues.indexOf(key).let {
            if (it != -1) {
                setValues.removeAt(it)
            }
        }
    }

    fun setValueForNextParameter(value: String) {
        setValueFromKey(setValues.get(0), value)
    }

    fun build(): Argument {
        return Argument(apkFilePath, decodeOutput)
    }
}

data class Argument(
    val apkFilePath: String = "",
    val decodeOutput: String = ""
) {
    fun isValid() = apkFilePath.isNotEmpty() && decodeOutput.isNotEmpty()
}