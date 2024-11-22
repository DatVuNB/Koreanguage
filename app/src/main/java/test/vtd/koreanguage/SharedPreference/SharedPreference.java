package test.vtd.koreanguage.SharedPreference;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreference {
    private static final String PREF_NAME = "button_state_pref";
    private static final String BUTTON_STATE_KEY = "is_button_pressed";

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public SharedPreference(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setButtonPressed(boolean isPressed) {
        editor.putBoolean(BUTTON_STATE_KEY, isPressed);
        editor.apply();
    }

    public boolean isButtonPressed() {
        return sharedPreferences.getBoolean(BUTTON_STATE_KEY, false);
    }
}
