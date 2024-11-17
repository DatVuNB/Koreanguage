package test.vtd.koreanguage.sharedPreference;

import android.content.Context;
import android.content.SharedPreferences;

public class sharedPreference {
    private static final String PREF_NAME = "button_state_pref";
    private static final String BUTTON_STATE_KEY = "is_button_pressed";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public sharedPreference(Context context) {
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
