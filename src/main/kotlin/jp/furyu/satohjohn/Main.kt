package jp.furyu.satohjohn

import jp.furyu.satohjohn.creator.SampleWavFile3
import java.io.File
import java.util.stream.IntStream
import javax.sound.sampled.AudioFileFormat
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem
import kotlin.streams.toList

fun main() {
    val data = IntStream.range(1, 8000).mapToDouble {
        (it / 10.0) * 440.0
    }.toList()
    println(data)
    val ss = SampleWavFile3(data, 3.0)
//    val ss = SampleWavFile(data, 3.0)
//    val ss = SampleWavFile3(listOf(440.0), 3.0)
//    val ss = SampleWavFile2(440.0, 3.0)
    println(ss.format)
    AudioSystem.write(
        AudioInputStream(ss, ss.format, ss.length),
        AudioFileFormat.Type.WAVE, File("./build/output_440.wav")
    )

}
