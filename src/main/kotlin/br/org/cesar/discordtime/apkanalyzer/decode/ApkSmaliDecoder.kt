package br.org.cesar.discordtime.apkanalyzer.decode

import java.io.File
import java.io.IOException

import org.jf.baksmali.Baksmali;
import org.jf.baksmali.BaksmaliOptions;
import org.jf.dexlib2.analysis.InlineMethodResolver;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.DexBackedOdexFile;
import org.jf.dexlib2.DexFileFactory;
import org.jf.dexlib2.Opcodes;

import br.org.cesar.discordtime.apkanalyzer.utils.ZipFileUtils

class ApkSmaliDecoder {
    companion object {
        const val WARNING_DISASSEMBLING_ODEX_FILE =
            "Warning: You are disassembling an odex file without deodexing it.";
        const val WARNING_FILE_IS_NOT_FOUND =
            "Apk file is not found!";

        private const val DEX_FILE_EXTENSION = ".dex"
        private const val MAXIMUM_NUMBER_OF_PROCESSORS = 6
    }

    @Throws(IOException::class)
    public fun decode(apkPath: String, outputDir: String, apiVersion: Int) {
        val apkFile = File(apkPath)
        if (!apkFile.exists()) {
            throw IOException(WARNING_FILE_IS_NOT_FOUND);
        }

        val outputFile: File = File(outputDir)
        getDexFiles(apkPath).forEach { dexFile ->
            decodeDexFile(apkFile, dexFile, apiVersion, outputFile)
        }
    }

    @Throws(IOException::class)
    private fun decodeDexFile(
        apkFile: File,
        dexFileName: String,
        apiVersion: Int,
        outputDir: File
    ) {
        loadDexFile(apkFile, dexFileName, apiVersion).let { dexFile ->
            try {
                Baksmali.disassembleDexFile(
                    dexFile,
                    outputDir,
                    getNumberOfAvailableProcessors(),
                    getSmaliOptions(dexFile)
                )
            } catch(e: Exception) {
                throw IOException(e);
            }
        }
    }

    @Throws(IOException::class)
    private fun getDexFiles(apkPath: String): List<String> {
        return ZipFileUtils.filterByFileExtention(apkPath, DEX_FILE_EXTENSION)
    }

    @Throws(IOException::class)
    private fun loadDexFile(
        apkFile: File, dexFilePath: String, api: Int
    ): DexBackedDexFile =
        DexFileFactory.loadDexEntry(
            apkFile,
            dexFilePath,
            true,
            Opcodes.forApi(api)
        ).also { 
            if (it == null || it.isOdexFile) {
                throw IOException(WARNING_DISASSEMBLING_ODEX_FILE)
            }
        }
    
    private fun getNumberOfAvailableProcessors(): Int =
        Math.min(
            Runtime.getRuntime().availableProcessors(),
            MAXIMUM_NUMBER_OF_PROCESSORS
        )
    
    private fun getSmaliOptions(dexFile: DexBackedDexFile) =
        BaksmaliOptions().also {
            it.deodex = false;
            it.implicitReferences = false;
            it.parameterRegisters = true;
            it.localsDirective = true;
            it.sequentialLabels = true;
            it.debugInfo = false;
            it.codeOffsets = false;
            it.accessorComments = false;
            it.registerInfo = 0;

            if (dexFile is DexBackedOdexFile) {
                it.inlineResolver = InlineMethodResolver
                    .createInlineMethodResolver((dexFile).getOdexVersion());
            } else {
                it.inlineResolver = null;
            }
        }
}