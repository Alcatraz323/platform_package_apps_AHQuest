package io.ahquest.settings;

import android.util.Log;

public class AudioHQRaw {
    public static void clearAllNativeSettings() {
        runAudioHqCmd(AudioHqCmds.CLEAR_ALL_SETTINGS);
    }

    public static ShellUtils.CommandResult getAudioHqNativeInfo() {
        return runAudioHqCmd(AudioHqCmds.GET_NATIVE_ELF_INFO);
    }

    public static ShellUtils.CommandResult runAudioHqCmd(AudioHqCmds audioHqCmds, String... params) {
        String cmd;
        if (audioHqCmds.hasParams())
            cmd = audioHqCmds.createCmd(params);
        else
            cmd = audioHqCmds.getCmd_raw();
        ShellUtils.CommandResult result = ShellUtils.execCommand(cmd, audioHqCmds.requiresRoot());
        if (result.responseMsg != null && result.responseMsg.length() != 0) {
            result.responseMsg = result.responseMsg.substring(0, result.responseMsg.length() - 1);
        }
        return result;
    }

    public enum AudioHqCmds {
        SET_PROFILE("audiohq --set-profile \"%s\" %s %s %s %s %s", false, true),
        UNSET_PROFILE("audiohq --unset-profile \"%s\" %s", false, true),
        GET_SWITCHES("audiohq --switches", false, false),
        SET_DEFAULT_SILENT_STATE("audiohq --def-silent %s", false, true),
        GET_DEFAULT_PROFILE("audiohq --def-profile", false, false),
        SET_DEFAULT_PROFILE("audiohq --def-profile %s %s %s %s", false, true),
        MUTE_PROCESS("audiohq --mute \"%s\" %s", false, true),
        UNMUTE_PROCESS("audiohq --unmute \"%s\" %s", false, true),
        CLEAR_ALL_SETTINGS("audiohq --clear", false, false),
        LIST_ALL_BUFFER("audiohq --list-buffers %s", false, true),
        GET_NATIVE_ELF_INFO("audiohq --elf-info", false, false),
        SET_WEAK_KEY_ADJUST("audiohq --weak-key %s", false, true),
        START_NATIVE_SERVICE("audiohq --service", true, false),
        GET_OVR_STATUS("audiohq --ovr-status",false,false),
        GET_PROFILE("audiohq --get-profile \"%s\" %s",false,true),
        LIST_PROFILE("audiohq --list-profile",false,false),
        LIST_MUTED("audiohq --list-muted",false,false);


        private String cmd_raw;
        private boolean require_root;
        private boolean has_params;

        AudioHqCmds(String cmd_raw, boolean require_root, boolean has_params) {
            this.cmd_raw = cmd_raw;
            this.require_root = require_root;
            this.has_params = has_params;
        }

        protected boolean requiresRoot() {
            return require_root;
        }

        protected boolean hasParams() {
            return has_params;
        }

        protected String getCmd_raw() {
            return cmd_raw;
        }

        @SuppressWarnings("ConfusingArgumentToVarargsMethod")
        protected String createCmd(String... params) {
            return String.format(cmd_raw, params);
        }
    }
}
