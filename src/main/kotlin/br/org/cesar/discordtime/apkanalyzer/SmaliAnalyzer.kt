package br.org.cesar.discordtime.apkanalyzer

import java.io.BufferedReader;
import java.io.File
import java.io.FileNotFoundException;
import java.io.FileReader;

class SmaliAnalyzer {
    private lateinit var invokedMethods: HashSet<String>

    fun getInvokedMethods(decodeApkDirPath: String): Collection<String> {
        invokedMethods = HashSet<String>()

        File(decodeApkDirPath).let { 
            if (it.isDirectory() && it.exists()) {
                readSmaliFilesInDir(it)
            }
        }

        return invokedMethods
    }

    private fun readSmaliFilesInDir(dir: File) {
        dir.listFiles().forEach { currentFile ->
            if (isSmaliFormat(currentFile)) {
                processSmaliFile(currentFile)

            } else if (currentFile.isDirectory()) {
                readSmaliFilesInDir(currentFile)
            }
        }
    }

    private fun processSmaliFile(smaliFile: File) {
        BufferedReader(FileReader(smaliFile)).let { bufferReader ->
            try {
                bufferReader.forEachLine { line ->
                    if(isInvokeMethod(line)) {
                        getInvokedMethod(line).let { 
                            if (!invokedMethods.contains(it)) {
                                invokedMethods.add(it)
                            }
                        }
                    }
                }
            } catch(e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getInvokedMethod(text: String) =
        text.substring(text.indexOf("L")).replace("/", ".")

    private fun isSmaliFormat(file: File): Boolean =
        file.isFile() && file.getName().endsWith(".smali")

    private fun isInvokeMethod(text: String): Boolean =
        text.contains("L") && text.contains("->") &&
        text.contains("(") && text.contains(")")
}