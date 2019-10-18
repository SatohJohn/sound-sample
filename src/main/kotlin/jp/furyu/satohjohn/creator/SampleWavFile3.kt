package jp.furyu.satohjohn.creator

import java.io.InputStream
import java.nio.ByteBuffer
import javax.sound.sampled.AudioFormat
import kotlin.math.pow

class SampleWavFile3(
    freq: List<Double>,
    seconds: Double
) : InputStream() {
    private val signed = true
    private val bigEndian = true
    private val sampleRate = 48000f
    private val sampleSizeByte = 2
    private val channels = 1
    private var list: List<Byte> = listOf()

    private val period: List<Double> = freq.map {f -> sampleRate / f}
    private val volume: Double = 2.0.pow((sampleSizeByte * 8 - 1).toDouble()) - 1
    val format: AudioFormat = AudioFormat(sampleRate, sampleSizeByte * 8, channels, signed, bigEndian)
    val length: Long = (sampleRate.toDouble() * channels.toDouble() * seconds).toLong()

    private var index = 0.0
    private var counter = 0
    override fun read(): Int {
        if (list.isEmpty()) {
            val indexPeriod = period[counter]
            val value =
            if (index / indexPeriod < 0.3) {
                volume.toLong() / 2
            } else if (index / indexPeriod < 0.4) {
                (-volume).toLong() / 2
            } else if (index / indexPeriod < 0.6) {
                volume.toLong() / 2
            } else if (index / indexPeriod < 0.8) {
                (-volume).toLong() / 2
            } else {
                volume.toLong() / 2
            }

            index = index + 1
            if (index / indexPeriod >= 1.0) {
                counter++
                if (counter >= period.size) {
                    println("----------------------------------------------------------------------------------------------------")
                    counter = 0
                }
                index = 0.0
            }
            val buffer = ByteBuffer.allocate(8)
            buffer.putLong(value)
            val array = buffer.array()
            for (i in (8 - sampleSizeByte)..7) {
                list = list.plus(array[i])
            }
        }

        val el = list.first()
        list = list.drop(1)
        return java.lang.Byte.toUnsignedInt(el)
    }

}