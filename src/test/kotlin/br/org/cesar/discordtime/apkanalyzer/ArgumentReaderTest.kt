package br.org.cesar.discordtime.apkanalyzer

import kotlin.test.Test
import kotlin.test.assertNotNull
import org.junit.Assert

class ArgumentReaderTest {
    
    @Test fun `should return invalid argument when passing empty args`() {
        ArgumentReader()
            .read(arrayOf())
            .let { 
                Assert.assertNotNull(it)
                Assert.assertFalse(it.isValid())
                Assert.assertEquals("", it.apkFilePath)
                Assert.assertEquals("", it.decodeOutput)
                Assert.assertEquals(false, it.clearDecodeOutputDir)
            }
    }
    
    @Test fun `should return invalid argument when passing apk macro only`() {
        ArgumentReader()
            .read(arrayOf<String>(
                "-a"
            ))
            .let { 
                Assert.assertNotNull(it)
                Assert.assertFalse(it.isValid())
                Assert.assertEquals("", it.apkFilePath)
                Assert.assertEquals("", it.decodeOutput)
                Assert.assertEquals(false, it.clearDecodeOutputDir)
            }
    }

    @Test fun `should return invalid argument when passing apk parameter`() {
        val apkFilePath: String = "apk file path"

        ArgumentReader()
            .read(arrayOf<String>(
                "-a", apkFilePath
            ))
            .let { 
                Assert.assertNotNull(it)
                Assert.assertFalse(it.isValid())
                Assert.assertEquals(apkFilePath, it.apkFilePath)
                Assert.assertEquals("", it.decodeOutput)
                Assert.assertEquals(false, it.clearDecodeOutputDir)
            }
    }

    @Test fun `should return invalid argument when passing output macro only`() {
        ArgumentReader()
            .read(arrayOf<String>(
                "-o"
            ))
            .let {
                Assert.assertNotNull(it)
                Assert.assertFalse(it.isValid())
                Assert.assertEquals("", it.apkFilePath)
                Assert.assertEquals("", it.decodeOutput)
                Assert.assertEquals(false, it.clearDecodeOutputDir)
            }
    }

    @Test fun `should return invalid argument when passing decode parameter`() {
        val decodeOutput: String = "decode output path"

        ArgumentReader()
            .read(arrayOf<String>(
                "-o", decodeOutput
            )).let {
                Assert.assertNotNull(it)
                Assert.assertFalse(it.isValid())
                Assert.assertEquals(decodeOutput, it.decodeOutput)
                Assert.assertEquals("", it.apkFilePath)
                Assert.assertEquals(false, it.clearDecodeOutputDir)
            }
    }

    @Test fun `should return invalid argument when passing two macros`() {
        ArgumentReader()
            .read(arrayOf<String>(
                "-o",
                "-a"
            ))
            .let {
                Assert.assertNotNull(it)
                Assert.assertFalse(it.isValid())
                Assert.assertEquals("", it.decodeOutput)
                Assert.assertEquals("", it.apkFilePath)
                Assert.assertEquals(false, it.clearDecodeOutputDir)
            }
    }

    @Test fun `should return invalid argument when passing two macros and one value`() {
        val apkFilePath: String = "apk file path"

        ArgumentReader()
            .read(arrayOf<String>(
                "-o",
                "-a", apkFilePath
            ))
            .let {
                Assert.assertNotNull(it)
                Assert.assertFalse(it.isValid())
                Assert.assertEquals("", it.decodeOutput)
                Assert.assertEquals(apkFilePath, it.apkFilePath)
                Assert.assertEquals(false, it.clearDecodeOutputDir)
            }
    }

    @Test fun `should return valid argument when passing correct values with macros`() {
        val apkFilePath: String = "apk file path"
        val decodeOutput: String = "decode output"

        ArgumentReader()
            .read(arrayOf<String>(
                "-o", decodeOutput,
                "-a", apkFilePath
            ))
            .let {
                Assert.assertNotNull(it)
                Assert.assertTrue(it.isValid())
                Assert.assertEquals(decodeOutput, it.decodeOutput)
                Assert.assertEquals(apkFilePath, it.apkFilePath)
                Assert.assertEquals(false, it.clearDecodeOutputDir)
            }
    }

    @Test fun `should return valid argument when passing correct values without macros`() {
        val apkFilePath: String = "apk file path"
        val decodeOutput: String = "decode output"

        ArgumentReader()
            .read(arrayOf<String>(
                apkFilePath, decodeOutput
            ))
            .let {
                Assert.assertNotNull(it)
                Assert.assertTrue(it.isValid())
                Assert.assertEquals(decodeOutput, it.decodeOutput)
                Assert.assertEquals(apkFilePath, it.apkFilePath)
                Assert.assertEquals(false, it.clearDecodeOutputDir)
            }
    }

    @Test fun `should return valid argument when passing a macro and direct apk value`() {
        val apkFilePath: String = "apk file path"
        val decodeOutput: String = "decode output"

        ArgumentReader()
            .read(arrayOf<String>(
                apkFilePath, "-o", decodeOutput
            ))
            .let {
                Assert.assertNotNull(it)
                Assert.assertTrue(it.isValid())
                Assert.assertEquals(decodeOutput, it.decodeOutput)
                Assert.assertEquals(apkFilePath, it.apkFilePath)
                Assert.assertEquals(false, it.clearDecodeOutputDir)
            }
    }

    @Test fun `should return valid argument when passing a macro and direct decode value`() {
        val apkFilePath: String = "apk file path"
        val decodeOutput: String = "decode output"

        ArgumentReader()
            .read(arrayOf<String>(
                "-a", apkFilePath, decodeOutput
            ))
            .let {
                Assert.assertNotNull(it)
                Assert.assertTrue(it.isValid())
                Assert.assertEquals(decodeOutput, it.decodeOutput)
                Assert.assertEquals(apkFilePath, it.apkFilePath)
                Assert.assertEquals(false, it.clearDecodeOutputDir)
            }
    }

    @Test fun `should return true for clear output value when passing true`() {
        val apkFilePath: String = "apk file path"
        val decodeOutput: String = "decode output"

        ArgumentReader()
            .read(arrayOf<String>(
                "-a", apkFilePath, "-o", decodeOutput, "-f"
            ))
            .let {
                Assert.assertNotNull(it)
                Assert.assertTrue(it.isValid())
                Assert.assertEquals(decodeOutput, it.decodeOutput)
                Assert.assertEquals(apkFilePath, it.apkFilePath)
                Assert.assertEquals(true, it.clearDecodeOutputDir)
            }
    }

    @Test fun `should return true for clear outpur dir when passing macro only`() {
        val apkFilePath: String = "apk file path"
        val decodeOutput: String = "decode output"

        ArgumentReader()
            .read(arrayOf<String>(
                apkFilePath, decodeOutput, "-f"
            ))
            .let {
                Assert.assertNotNull(it)
                Assert.assertTrue(it.isValid())
                Assert.assertEquals(decodeOutput, it.decodeOutput)
                Assert.assertEquals(apkFilePath, it.apkFilePath)
                Assert.assertEquals(true, it.clearDecodeOutputDir)
            }
    }

    @Test fun `should return true for clean output when passing true as value without macro`() {
        val apkFilePath: String = "apk file path"
        val decodeOutput: String = "decode output"

        ArgumentReader()
            .read(arrayOf<String>(
                apkFilePath, decodeOutput, "true"
            ))
            .let {
                Assert.assertNotNull(it)
                Assert.assertTrue(it.isValid())
                Assert.assertEquals(decodeOutput, it.decodeOutput)
                Assert.assertEquals(apkFilePath, it.apkFilePath)
                Assert.assertEquals(true, it.clearDecodeOutputDir)
            }
    }

    @Test fun `should return false for clean output when passing false as value without macro`() {
        val apkFilePath: String = "apk file path"
        val decodeOutput: String = "decode output"

        ArgumentReader()
            .read(arrayOf<String>(
                apkFilePath, decodeOutput, "false"
            ))
            .let {
                Assert.assertNotNull(it)
                Assert.assertTrue(it.isValid())
                Assert.assertEquals(decodeOutput, it.decodeOutput)
                Assert.assertEquals(apkFilePath, it.apkFilePath)
                Assert.assertEquals(false, it.clearDecodeOutputDir)
            }
    }

    @Test fun `should not throw expection when passing values more than limit`() {
        val apkFilePath: String = "apk file path"
        val decodeOutput: String = "decode output"

        ArgumentReader()
            .read(arrayOf<String>(
                apkFilePath, decodeOutput, "false", "extra"
            ))
            .let {
                Assert.assertNotNull(it)
                Assert.assertTrue(it.isValid())
                Assert.assertEquals(decodeOutput, it.decodeOutput)
                Assert.assertEquals(apkFilePath, it.apkFilePath)
                Assert.assertEquals(false, it.clearDecodeOutputDir)
            }
    }
    @Test fun `should not throw expection when passing values more than limit with macros`() {
        val apkFilePath: String = "apk file path"
        val decodeOutput: String = "decode output"

        ArgumentReader()
            .read(arrayOf<String>(
                "-a", apkFilePath, "-o", decodeOutput, "-f", "extra"
            ))
            .let {
                Assert.assertNotNull(it)
                Assert.assertTrue(it.isValid())
                Assert.assertEquals(decodeOutput, it.decodeOutput)
                Assert.assertEquals(apkFilePath, it.apkFilePath)
                Assert.assertEquals(true, it.clearDecodeOutputDir)
            }
    }
}