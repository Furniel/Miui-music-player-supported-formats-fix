package com.furniel.miuimusicplayerfix;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class formats_fix implements IXposedHookLoadPackage {
    private final Set<String> mSupportedAudioExt = new HashSet<>();

    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals("com.miui.player"))
            return;

        XposedBridge.log("MIUI PLAYER FOUND!");

        findAndHookMethod("com.miui.player.scanner.FileScanStrategy", lpparam.classLoader, "isAudioExtension", String.class , new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                // this will be called before the clock was updated by the original method
            }
            @Override
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                // this will be called after the clock was updated by the original method
                String theReturnVal= (String) param.args[0];
                final String[] VALUE_SCAN_EXT_ARRAY = new String[]{"mp3", "mpga", "m4a", "wav", "awr", "awb", "qcp", "ogg", "oga", "aac", "mka", "mid", "midi", "xmf", "rtttl", "smf", "imy", "rtx", "ota", "mxmf", "ape", "flac", "ec3", "dsf", "dff", "amr"};
                mSupportedAudioExt.addAll(Arrays.asList(VALUE_SCAN_EXT_ARRAY));
                param.setResult(mSupportedAudioExt.contains(getExtension(theReturnVal).toLowerCase()));
                XposedBridge.log("MIUI PLAYER FORMATS SHOULD BE FIXED NOW!");
            }
        });
    }

    private static String getExtension(String str) {
        if (str == null || str.length() == 0) {
            return "";
        }
        int lastIndexOf = str.lastIndexOf(46);
        return lastIndexOf > -1 ? str.substring(lastIndexOf + 1) : "";
    }
}