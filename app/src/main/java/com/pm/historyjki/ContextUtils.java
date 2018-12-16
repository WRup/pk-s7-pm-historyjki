package com.pm.historyjki;

import android.content.Context;

public class ContextUtils {

    public static String getStringResource(Context context, String name) {
        if (context == null) {
            return "$" + name + "$";
        }

        String packageName = context.getPackageName();
        int resId = context.getResources().getIdentifier(name, "string", packageName);
        return context.getString(resId);
    }
}
