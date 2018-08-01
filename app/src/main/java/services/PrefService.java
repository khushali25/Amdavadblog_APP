package services;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefService {
    private SharedPreferences nameSharedPrefs;
    private SharedPreferences.Editor namePrefsEditor; //Declare Context,Prefrences name and Editor name
    private Context mContext;

    public PrefService(Context context)
    {
        this.mContext = context;
        nameSharedPrefs = android.preference.PreferenceManager.getDefaultSharedPreferences(mContext);
        namePrefsEditor = nameSharedPrefs.edit();
    }

    public void saveAccessKey(String key,String val) // Save data Values
    {
        namePrefsEditor.putString(key, val);
        namePrefsEditor.commit();
    }
    public String getAccessKey(String key) // Return Get the Value
    {
        return nameSharedPrefs.getString(key, "");
    }
}

