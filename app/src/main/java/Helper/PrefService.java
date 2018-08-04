package Helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

public class PrefService
{
    private SharedPreferences nameSharedPrefs;
    private SharedPreferences.Editor namePrefsEditor; //Declare Context,Prefrences name and Editor name
    private Context mContext;

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
}
