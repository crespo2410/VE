package Interfaces;


/**
 * Interace CallBack (s pripadajućom metodom) onSuccess() i onError() koji se koristi nakon što
 * je prihvacen odgovor iz poslužitelja
 */
public interface ProvjeraCallBack {
    void onSuccess(boolean value);
    void onError();
}


