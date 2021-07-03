package com.example.bright_storage;

import android.os.Bundle;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;

import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;


import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static com.iflytek.cloud.SpeechConstant.*;

public class VoiceTest {

    private static final String TAG = "VoiceTest";

    private SpeechRecognizer recognizer;

    private RecognizerListener listener = new RecognizerListener() {
        @Override
        public void onVolumeChanged(int i, byte[] bytes) {

        }

        @Override
        public void onBeginOfSpeech() {

        }

        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {

            Log.i(TAG, "onResult: "+recognizerResult.getResultString());
        }

        @Override
        public void onError(SpeechError speechError) {

        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };

    @Test
    public void before(){

        recognizer = SpeechRecognizer.createRecognizer(
                InstrumentationRegistry.getInstrumentation().getContext(),
                i -> {
                    Log.i(TAG, "Init:" + i);
                    recognizer.setParameter(PARAMS, "iat");
                    recognizer.setParameter(LANGUAGE, "zh_cn");
                    recognizer.setParameter(ACCENT, "mandarin");
                    recognizer.setParameter(ENGINE_TYPE, TYPE_CLOUD);
                    recognizer.setParameter(RESULT_TYPE, "plain");
                    recognizer.setParameter(VAD_BOS, "4000");
                    recognizer.setParameter(VAD_EOS, "2000");
                    recognizer.setParameter(ASR_PTT, "0");
                    recognizer.setParameter(TEXT_ENCODING, "utf-8");

                    recognizer.startListening(listener);
                });
    }

    @Test
    public void voiceTest(){
    }
}
