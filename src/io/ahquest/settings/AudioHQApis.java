package io.ahquest.settings;

import android.content.Context;

import io.ahquest.settings.AudioHQRaw;
import io.ahquest.settings.ShellUtils;
import io.ahquest.settings.AudioHQNativeInterface;

public class AudioHQApis {
    public static void clearAllSetting(Context context) {
        AudioHQRaw.clearAllNativeSettings();
    }

    public static void getAudioHQNativeInfo(Context context, AudioHQNativeInterface<String[]> nativeInterface){
        ShellUtils.CommandResult result = AudioHQRaw.getAudioHqNativeInfo();
        nativeInterface.onSuccess(result.responseMsg.split(";"));
        if(result.errorMsg != null && !result.errorMsg.equals("")){
            nativeInterface.onFailure(result.errorMsg);
        }
    }
}
