package helper;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import java.util.Locale;

/**
 * ContextWrapper koji se koristi zbog toga što su metode zastarjele, a vezane uz promjenu jezika, Locale
 * i updateConfiguration
 */
public class ConfigurationWrapper {
    private ConfigurationWrapper() {
    }


    public static Context wrapConfiguration(@NonNull final Context context,
                                            @NonNull final Configuration config) {
        return context.createConfigurationContext(config);
    }

    public static Context wrapLocale(@NonNull final Context context,
                                     @NonNull final Locale locale) {
        final Resources res = context.getResources();
        final Configuration config = res.getConfiguration();
        config.setLocale(locale);
        return wrapConfiguration(context, config);
    }
}