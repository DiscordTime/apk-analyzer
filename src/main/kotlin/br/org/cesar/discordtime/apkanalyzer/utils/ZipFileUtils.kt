package br.org.cesar.discordtime.apkanalyzer.utils

import java.io.IOException
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

object ZipFileUtils {
    @Throws(IOException::class)
    fun filterByFileExtention(zipPath: String, extesion: String): List<String> {
        return ArrayList<String>().also { files ->
            ZipFile(zipPath).entries().run {
                while(this.hasMoreElements()) {
                    this.nextElement().let { entry ->
                        if (!entry.isDirectory() && entry.name.endsWith(extesion)) {
                            files.add(entry.name)
                        }
                    }
                }
            }
        }
    }
}