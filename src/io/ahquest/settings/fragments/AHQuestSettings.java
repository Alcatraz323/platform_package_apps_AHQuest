/*
 * Copyright (C) 2020 DerpFest ROM
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.ahquest.settings.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.provider.SearchIndexableResource;
import android.provider.Settings;
import android.util.Log;

import androidx.preference.SwitchPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;

import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.R;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.Indexable;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settingslib.search.SearchIndexable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import io.ahquest.settings.AudioHQApis;
import io.ahquest.settings.AudioHQNativeInterface;

@SearchIndexable
public class AHQuestSettings extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener, Indexable {

    private static final String TAG = "AHQuestSettings";

    private static final String AHQ_SHOW_UI = "ahq_show_ui";
    private static final String AHQ_CLEAR_ALL= "ahq_clear";
    private static final String AHQ_PRESET = "ahq_preset";
    private static final String AHQ_INFO = "ahq_info";

    private SwitchPreference mShowUI;
    private PreferenceScreen mClear;
    private PreferenceScreen mPreset;
    private PreferenceScreen mAHQInfo;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        addPreferencesFromResource(R.xml.ahquest_settings);
        final PreferenceScreen prefScreen = getPreferenceScreen();
        ContentResolver resolver = getActivity().getContentResolver();
        Resources resources = getResources();

        mShowUI = (SwitchPreference)findPreference(AHQ_SHOW_UI);
        mClear = (PreferenceScreen)findPreference(AHQ_CLEAR_ALL);
        mPreset = (PreferenceScreen)findPreference(AHQ_PRESET);
        mAHQInfo = (PreferenceScreen)findPreference(AHQ_INFO);

        mShowUI.setOnPreferenceChangeListener(this);
        boolean showUIVal = Settings.System.getInt(resolver,
                AHQ_SHOW_UI, 1) == 1;
        mShowUI.setChecked(showUIVal);
        mClear.setOnPreferenceClickListener(preference -> {
            new AlertDialog.Builder(getContext())
                    .setTitle(R.string.pref_warning_title)
                    .setMessage(R.string.pref_clear_profile_confirm)
                    .setNegativeButton(R.string.cancel, null)
                    .setPositiveButton(R.string.ok, (dialogInterface, i) -> AudioHQApis.clearAllSetting(getContext())).show();
                return true;
        });

        AudioHQApis.getAudioHQNativeInfo(getContext(), new AudioHQNativeInterface<String[]>() {
            @Override
            public void onSuccess(String[] result) {
                if(result.length ==2) {
                    mAHQInfo.setSummary(result[0]+result[1]);
                }
                if(result.length == 1){
                    mAHQInfo.setSummary(result[0]);
                }
            }

            @Override
            public void onFailure(String reason) {
                mAHQInfo.setSummary(reason);
            }
        });
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();
        if (preference == mShowUI) {
            boolean value = (Boolean) newValue;
            Settings.System.putInt(getContentResolver(),
		            AHQ_SHOW_UI, value ? 1 : 0);
            return true;
        }
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return 128;
    }

    public static final Indexable.SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider() {
                @Override
                public List<SearchIndexableResource> getXmlResourcesToIndex(Context context,
                        boolean enabled) {
                    ArrayList<SearchIndexableResource> result =
                            new ArrayList<SearchIndexableResource>();
                     SearchIndexableResource sir = new SearchIndexableResource(context);
                    sir.xmlResId = R.xml.ahquest_settings;
                    result.add(sir);
                    return result;
                }
                @Override
                public List<String> getNonIndexableKeys(Context context) {
                    ArrayList<String> result = new ArrayList<String>();
                    return result;
                }
    };
}
