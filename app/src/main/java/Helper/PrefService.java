package Helper;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefService
{
    private SharedPreferences nameSharedPrefs;
    private SharedPreferences.Editor namePrefsEditor; //Declare Context,Prefrences name and Editor name
    private Context mContext;
    private static final String PREF_NAME = "Intro_Activity";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    public PrefService(Context context)
    {
        this.mContext = context;
        nameSharedPrefs = android.preference.PreferenceManager.getDefaultSharedPreferences(mContext);
        namePrefsEditor = nameSharedPrefs.edit();
    }
    public String getAccessKey(String key) // Return Get the Value
    {
        return nameSharedPrefs.getString(key, "");
    }
    public void saveAccessKey(String key, String value) {
        namePrefsEditor.putString(key,value);
        namePrefsEditor.commit();
    }
    public void setFirstTimeLaunch(boolean isFirstTime) {
        namePrefsEditor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        namePrefsEditor.commit();
    }
}
