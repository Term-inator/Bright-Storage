package com.example.bright_storage.util;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import lombok.SneakyThrows;

public class AudioRecordUtil {

    private static final String TAG = "AudioRecordUtil";

    private final static int SAMPLE_RATE = 16000;
    private final static int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_STEREO;
    private final static int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;

    private volatile boolean recorderState = true;
    private byte[] buffer;
    private ByteArrayOutputStream byteArrayOutputStream;
    private AudioRecord audioRecord;

    private static AudioRecordUtil audioRecordUtil = new AudioRecordUtil();

    private AudioRecordUtil(){
        int recordBufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT);
        buffer = new byte[recordBufferSize];
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT, recordBufferSize);
        byteArrayOutputStream = new ByteArrayOutputStream();
    }

    public static AudioRecordUtil getInstance(){
        return audioRecordUtil;
    }

    public boolean startRecord(){
        if(audioRecord.getState() != AudioRecord.RECORDSTATE_STOPPED){
            return false;
        }
        byteArrayOutputStream.reset();
        recorderState = true;
        audioRecord.startRecording();
        new RecordThread().start();
        return true;
    }

    public byte[] stopRecord(){
        recorderState = false;
        if(audioRecord.getState() == AudioRecord.RECORDSTATE_RECORDING){
            audioRecord.stop();
        }
        audioRecord.release();
        return byteArrayOutputStream.toByteArray();
    }

    private class RecordThread extends Thread{

        @Override
        public void run() {
            try{
                while(recorderState){
                    int read = audioRecord.read(buffer,0,buffer.length);
                    if(AudioRecord.ERROR_INVALID_OPERATION != read){
                        byteArrayOutputStream.write(buffer,0,read);
                    }
                }
            } catch (Exception e){
                Log.e(TAG, "RecordUtilThread: ", e);
            }
            int i = 0;
        }
    }
}
