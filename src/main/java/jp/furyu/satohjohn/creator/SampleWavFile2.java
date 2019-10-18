package jp.furyu.satohjohn.creator;

import javax.sound.sampled.AudioFormat;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class SampleWavFile2 extends InputStream {
    boolean signed = true;
    boolean big_endian = true;
    float sample_rate = 48000;
    int sample_size_byte = 2;
    int channels = 1;
    double freq;
    double seconds;

    ArrayList<Byte> list;
    double index;
    double period;
    double volume;

    public SampleWavFile2(double freq, double seconds){
        this.freq = freq;
        this.seconds = seconds;
        list = new ArrayList<>();
        index = 0;
        period = sample_rate / freq;
        volume = Math.pow(2, sample_size_byte * 8 - 1) - 1;
    }
    @Override
    public int read() throws IOException {
        if(list.isEmpty()){
            long value;

            if(index / period < 0.5){
                value = (long)volume;
            }else{
                value = (long)-volume;
            }

            index++;
            index %= period;
            ByteBuffer buffer = ByteBuffer.allocate(8);
            buffer.putLong(value);
            byte[] array = buffer.array();
            for (int i = 8 - sample_size_byte; i < 8; i++) {
                list.add(array[i]);
            }
        }
        return Byte.toUnsignedInt(list.remove(0));
    }
    public long length(){
        return (long)(sample_rate * channels * seconds);
    }
    public AudioFormat getFormat(){
        return new AudioFormat(sample_rate, sample_size_byte * 8, channels, signed, big_endian);
    }
}