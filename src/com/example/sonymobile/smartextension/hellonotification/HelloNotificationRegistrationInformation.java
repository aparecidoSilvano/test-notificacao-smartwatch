/*
Copyright (c) 2011, Sony Ericsson Mobile Communications AB
Copyright (c) 2011-2013, Sony Mobile Communications AB

 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer.

 * Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution.

 * Neither the name of the Sony Ericsson Mobile Communications AB / Sony Mobile
 Communications AB nor the names of its contributors may be used to endorse or promote
 products derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.example.sonymobile.smartextension.hellonotification;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.sonyericsson.extras.liveware.aef.notification.Notification;
import com.sonyericsson.extras.liveware.aef.registration.Registration;
import com.sonyericsson.extras.liveware.extension.util.ExtensionUtils;
import com.sonyericsson.extras.liveware.extension.util.registration.RegistrationInformation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Provides information needed during extension registration.
 */
public class HelloNotificationRegistrationInformation extends RegistrationInformation {

    final Context mContext;
    private String extensionKey;
    private static final String EXTENSION_KEY_PREF = "EXTENSION_KEY_PREF";


    /**
     * Creates a notification registration object.
     *
     * @param context The context.
     */
    protected HelloNotificationRegistrationInformation(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context == null");
        }
        mContext = context;
    }

    @Override
    public int getRequiredNotificationApiVersion() {
        // This extension supports all accessories from Notification API level
        // 1 and up.
        return 1;
    }

    @Override
    public int getRequiredWidgetApiVersion() {
        return API_NOT_REQUIRED;
    }

    @Override
    public int getRequiredControlApiVersion() {
        return API_NOT_REQUIRED;
    }

    @Override
    public int getRequiredSensorApiVersion() {
        return API_NOT_REQUIRED;
    }

    /**
     * Returns the properties of this extension.
     *
     * @return The registration configuration.
     */
    @Override
    public ContentValues getExtensionRegistrationConfiguration() {
        String extensionIcon = ExtensionUtils.getUriString(mContext, R.drawable.icon_extension);
        String iconHostapp = ExtensionUtils.getUriString(mContext, R.drawable.icon);
        String extensionIcon48 = ExtensionUtils
                .getUriString(mContext, R.drawable.icon_extension_48);

        String configurationText = mContext.getString(R.string.configuration_text);
        String extensionName = mContext.getString(R.string.extension_name);

        ContentValues values = new ContentValues();
        values.put(Registration.ExtensionColumns.CONFIGURATION_ACTIVITY,
                HelloNotificationPreferenceActivity.class.getName());
        values.put(Registration.ExtensionColumns.CONFIGURATION_TEXT, configurationText);
        values.put(Registration.ExtensionColumns.EXTENSION_ICON_URI, extensionIcon);
        values.put(Registration.ExtensionColumns.EXTENSION_48PX_ICON_URI, extensionIcon48);
        values.put(Registration.ExtensionColumns.EXTENSION_KEY, getExtensionKey());
        values.put(Registration.ExtensionColumns.HOST_APP_ICON_URI, iconHostapp);
        values.put(Registration.ExtensionColumns.NAME, extensionName);
        values.put(Registration.ExtensionColumns.NOTIFICATION_API_VERSION,
                getRequiredNotificationApiVersion());
        values.put(Registration.ExtensionColumns.PACKAGE_NAME, mContext.getPackageName());

        return values;
    }

    @Override
    public ContentValues[] getSourceRegistrationConfigurations() {
        // This sample only adds one source but it is possible to add more
        // sources if needed.
        List<ContentValues> bulkValues = new ArrayList<ContentValues>();
        bulkValues
                .add(getSourceRegistrationConfiguration(HelloNotificationExtensionService.EXTENSION_SPECIFIC_ID));
        return bulkValues.toArray(new ContentValues[bulkValues.size()]);
    }

    /**
     * Returns the properties of a source.
     *
     * @param extensionSpecificId The id of the extension to associate the source
     * with.
     * @return The source configuration.
     */
    public ContentValues getSourceRegistrationConfiguration(String extensionSpecificId) {
        ContentValues sourceValues = null;

        String iconSource1 = ExtensionUtils.getUriString(mContext,
                R.drawable.icn_30x30_message_notification);
        String iconSource2 = ExtensionUtils.getUriString(mContext,
                R.drawable.icn_18x18_message_notification);
        String iconBw = ExtensionUtils.getUriString(mContext,
                R.drawable.icn_18x18_black_white_message_notification);
        String textToSpeech = mContext.getString(R.string.text_to_speech);
        sourceValues = new ContentValues();
        sourceValues.put(Notification.SourceColumns.ENABLED, true);
        sourceValues.put(Notification.SourceColumns.ICON_URI_1, iconSource1);
        sourceValues.put(Notification.SourceColumns.ICON_URI_2, iconSource2);
        sourceValues.put(Notification.SourceColumns.ICON_URI_BLACK_WHITE, iconBw);
        sourceValues.put(Notification.SourceColumns.UPDATE_TIME, System.currentTimeMillis());
        sourceValues.put(Notification.SourceColumns.NAME, mContext.getString(R.string.source_name));
        sourceValues.put(Notification.SourceColumns.EXTENSION_SPECIFIC_ID, extensionSpecificId);
        sourceValues.put(Notification.SourceColumns.PACKAGE_NAME, mContext.getPackageName());
        sourceValues.put(Notification.SourceColumns.TEXT_TO_SPEECH, textToSpeech);
        // It is possible to connect actions to notifications. These will be
        // accessible when tapping the options menu on the SmartWatch 2.
        sourceValues.put(Notification.SourceColumns.ACTION_1,
                mContext.getString(R.string.action_event_1));
        sourceValues.put(Notification.SourceColumns.ACTION_2,
                mContext.getString(R.string.action_event_2));
        sourceValues.put(Notification.SourceColumns.ACTION_3,
                mContext.getString(R.string.action_event_3));
        sourceValues.put(Notification.SourceColumns.ACTION_ICON_1,
                ExtensionUtils.getUriString(mContext, R.drawable.actions_1));
        sourceValues.put(Notification.SourceColumns.ACTION_ICON_2,
                ExtensionUtils.getUriString(mContext, R.drawable.actions_2));
        sourceValues.put(Notification.SourceColumns.ACTION_ICON_3,
                ExtensionUtils.getUriString(mContext, R.drawable.actions_3));

        // You can set a colour for a source. On the SmartWatch 2 this will
        // be visible in the title bar when opening the extension or a
        // notification.
        // This is an API 2.0 feature and will be ignored where not supported.
        // The API supports one colour per Source but SmartWatch 2 uses the
        // colour of the first source registered for all notifications.
        sourceValues.put(Notification.SourceColumns.COLOR,
                mContext.getResources().getColor(R.color.smart_watch_2_text_color_orange));
        return sourceValues;
    }

    /**
     * A basic implementation of getExtensionKey
     * Returns and saves a random string based on UUID.randomUUID()
     *
     * Note that this implementation doesn't guarantee random numbers on Android 4.3 and older. See <a href="https://android-developers.blogspot.com/2013/08/some-securerandom-thoughts.html">https://android-developers.blogspot.com/2013/08/some-securerandom-thoughts.html</a>
     *
     * @return A saved key if it exists, otherwise a randomly generated one.
     * @see com.sonyericsson.extras.liveware.extension.util.registration.RegistrationInformation#getExtensionKey()
     */
    @Override
    public synchronized String getExtensionKey() {
        if (TextUtils.isEmpty(extensionKey)) {
            // Retrieve key from preferences
            SharedPreferences pref = mContext.getSharedPreferences(EXTENSION_KEY_PREF,
                    Context.MODE_PRIVATE);
            extensionKey = pref.getString(EXTENSION_KEY_PREF, null);
            if (TextUtils.isEmpty(extensionKey)) {
                // Generate a random key if not found
                extensionKey = UUID.randomUUID().toString();
                pref.edit().putString(EXTENSION_KEY_PREF, extensionKey).commit();
            }
        }
        return extensionKey;
    }

}
