package jp.furyu.satohjohn.creator

import java.io.InputStream
import java.nio.ByteBuffer
import javax.sound.sampled.AudioFormat
import kotlin.math.pow

class SampleWavFile(
    freq: Double,
    seconds: Double
) : InputStream() {
    private val signed = true
    private val bigEndian = true
    private val sampleRate = 48000f
    private val sampleSizeByte = 2
    private val channels = 1
    private var list: List<Byte> = listOf()

    private val period: Double = sampleRate / freq
    private val volume: Double = 2.0.pow((sampleSizeByte * 8 - 1).toDouble()) - 1
    val format: AudioFormat = AudioFormat(sampleRate, sampleSizeByte * 8, channels, signed, bigEndian)
    val length: Long = (sampleRate.toDouble() * channels.toDouble() * seconds).toLong()

    var index = 0.0
    override fun read(): Int {
        if (list.isEmpty()) {
            val value =
            if (index / period < 0.5) {
                volume.toLong()
            } else {
                (-volume).toLong()
            }

            index = index + 1
            index = index % period
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