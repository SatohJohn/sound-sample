package jp.furyu.satohjohn

import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import javax.sound.sampled.AudioFileFormat
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem


fun main() {
    println("main")

    val sample_rate = 48000.0f
    val sample_size_byte = 2
    val channels = 1 // mono

    val length = (sample_rate * channels * 2)
    val format = AudioFormat(sample_rate, sample_size_byte * 8, channels, true, true)

    AudioSystem.write(
        AudioInputStream(Files.newInputStream(Paths.get("./input/test.wav")), format, length.toLong()),
        AudioFileFormat.Type.WAVE,
        File("./build/output.wav"))

}
