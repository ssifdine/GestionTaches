package ma.saifdine.hd.infra.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

/**
 * Utility class for managing SharedPreferences in a clean and simple way.
 * Supports String, int, long, and boolean data types.
 */
public class PrefUtils {

    private static final String DEFAULT_STRING_VALUE = "";
    private static final long DEFAULT_LONG_VALUE = -1L;
    private static final int DEFAULT_INT_VALUE = -1;
    private static final boolean DEFAULT_BOOLEAN_VALUE = false;

    private static PrefUtils instance;
    private final SharedPreferences sharedPreferences;

    /**
     * Private constructor to initialize SharedPreferences.
     *
     * @param context         Application context
     * @param preferencesName Name of the preferences file
     */
    private PrefUtils(@NonNull Context context, @NonNull String preferencesName) {
        this.sharedPreferences = context.getApplicationContext()
                .getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
    }

    /**
     * Initializes or retrieves the singleton instance of PrefUtils with the default preferences file.
     *
     * @param context Application context
     * @return Singleton instance of PrefUtils
     */
    public static PrefUtils getInstance(@NonNull Context context) {
        return getInstance(context, context.getPackageName() + "_preferences");
    }

    /**
     * Initializes or retrieves the singleton instance of PrefUtils with a custom preferences file.
     *
     * @param context         Application context
     * @param preferencesName Custom preferences file name
     * @return Singleton instance of PrefUtils
     */
    public static synchronized PrefUtils getInstance(@NonNull Context context, @NonNull String preferencesName) {
        if (instance == null) {
            instance = new PrefUtils(context, preferencesName);
        }
        return instance;
    }

    // ===================== Generic Methods =====================

    /**
     * Reads a value from SharedPreferences with a default value.
     *
     * @param key          Key of the preference
     * @param defaultValue Default value if the key does not exist
     * @param <T>          Data type (String, Integer, Long, Boolean)
     * @return Stored value or the default value
     */
    @SuppressWarnings("unchecked")
    public <T> T read(String key, T defaultValue) {
        if (defaultValue instanceof String) {
            return (T) sharedPreferences.getString(key, (String) defaultValue);
        } else if (defaultValue instanceof Integer) {
            return (T) (Integer) sharedPreferences.getInt(key, (Integer) defaultValue);
        } else if (defaultValue instanceof Long) {
            return (T) (Long) sharedPreferences.getLong(key, (Long) defaultValue);
        } else if (defaultValue instanceof Boolean) {
            return (T) (Boolean) sharedPreferences.getBoolean(key, (Boolean) defaultValue);
        } else {
            throw new IllegalArgumentException("Unsupported data type");
        }
    }

    /**
     * Writes a value to SharedPreferences.
     *
     * @param key   Key of the preference
     * @param value Value to store (String, Integer, Long, Boolean)
     * @param <T>   Data type
     */
    public <T> void write(String key, T value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else {
            throw new IllegalArgumentException("Unsupported data type");
        }

        editor.apply();
    }

    // ===================== Utility Methods =====================

    /**
     * Checks if a key exists in SharedPreferences.
     *
     * @param key Key to check
     * @return True if the key exists, false otherwise
     */
    public boolean contains(String key) {
        return sharedPreferences.contains(key);
    }

    /**
     * Clears all preferences stored in SharedPreferences.
     */
    public void clear() {
        sharedPreferences.edit().clear().apply();
    }
}
