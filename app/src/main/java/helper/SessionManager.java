package helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

/**
 * Klasa koja je zadužena za regulaciju i pračenje prijave korisnika putem svojeg računa u aplikaciju.
 * Informacije o tome spremaju se u SharedPreference
 */
public class SessionManager {

    private static String TAG = SessionManager.class.getSimpleName();


    SharedPreferences pref;

    Editor editor;
    Context _context;

    int PRIVATE_MODE = 0;


    private static final String PREF_NAME = "AndroidLogin";

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String FIRST_TIME = "isFirstTime";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Postavi u SharedPreferences da li je korisnik prijavljen ili nije
     * @param isLoggedIn - boolean koji pokazuje da li ili nije prijavljen
     */
    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        editor.commit();
    }

    public void setFirstTime(boolean isLoggedIn) {

        editor.putBoolean(FIRST_TIME, isLoggedIn);
        editor.commit();
    }



    /**
     * metoda koja čita iz SharedPreferenca
     * @return - true ako je prijavljen, false ako nije
     */
    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    public boolean isFirstTime(){
        return pref.getBoolean(FIRST_TIME,true);
    }
}