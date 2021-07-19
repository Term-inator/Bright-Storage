package com.example.bright_storage.recog.handler;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.baidu.speech.asr.SpeechConstant;
import com.example.bright_storage.R;
import com.example.bright_storage.recog.IStatus;

public class RecogHandler extends Handler {

    private Context context;

    public RecogHandler(Context context) {
        this.context = context;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        switch (msg.arg1){
            case IStatus.STATUS_FINISHED:
                // TODO 或者在这里做NLP
                Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_LONG).show();
                break;
            case IStatus.STATUS_PARTIAL:
                Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_LONG).show();
                break;
        }
    }
}
