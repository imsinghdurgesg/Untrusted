package app.untrusted.utils;

/**
 * Created by DSingh on 6/28/2017.
 */

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AppSharedPreference implements AppConstants{

    private SharedPreferences sharedpreferences;

    public AppSharedPreference(Context context){
        sharedpreferences = context.getSharedPreferences(APP_PREF_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Save anonymous Integer data in shared preferences
     * @param intData Data to be stored in shared preference
     * @param key Respective key
     */
    public void putLongData(String key, long intData){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putLong(key, intData);
        editor.apply();
    }

    public long getLongData(String key,long defaultValue){

        return  sharedpreferences.getLong(key, defaultValue);
    }


    /**
     * Save anonymous String data in shared preferences
     * @param stringData Data to be stored in shared preference
     * @param key Respective key
     */
    public void putStringData(String key, String stringData){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, stringData);
        editor.apply();
    }
    /**
     * Get String data according to key
     * @param key key from data to be fetched
     * @return return the data
     */

    public String getStringData(String key){
        return sharedpreferences.getString(key, null);
    }

    /**
     * Save anonymous Integer data in shared preferences
     * @param intData Data to be stored in shared preference
     * @param key Respective key
     */
    public void putIntData(String key, int intData){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(key, intData);
        editor.apply();
    }

    public int getIntData(String key,int defaultValue){
        return sharedpreferences.getInt(key, defaultValue);
    }


    public void putBooleanData(String key, boolean intData){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(key, intData);
        editor.apply();
    }

    public boolean getBooleanData(String key,boolean defaultValue){
        return sharedpreferences.getBoolean(key, defaultValue);
    }

    public void removeArrayData(String key)
    {
        int arraySize =  sharedpreferences.getInt(key + "_size", 0);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        for(int i=0;i<arraySize;i++)
        {
            editor.remove(key + "_" + i);
        }
        editor.remove(key + "_size");
        editor.apply();
    }

    public void putArrayList(String key,ArrayList<String> arraylistfile)
    {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        Set<String> set = new HashSet<>();
        set.addAll(arraylistfile);
        editor.putStringSet(key, set);
        editor.apply();
    }
   /* public void putList(String key,ArrayList<String> arraylistfile)
    {
        SharedPreferences.Editor editor = sharedpreferences.edit();
       *//* Set<String> set = new HashSet<>();
        set.addAll(arraylistfile);*//*
        editor.putString(key,);
        editor.apply();
    }*/
    public Set<String> getArrayList(String key)
    {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        Set<String> set = sharedpreferences.getStringSet(key, null);
        return set;
    }

    public void saveStringList(String arrayName,ArrayList<String> array) {

        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putInt(arrayName + "_size", array.size());
        for(int i=0;i<array.size();i++)
            editor.putString(arrayName + "_" + i, array.get(i));

        editor.apply();
    }

    public ArrayList<String> loadStringList(String arrayName) {
        int size = sharedpreferences.getInt(arrayName + "_size", 0);


        ArrayList<String> list = new ArrayList<>(size);
        for(int i=0;i<size;i++)
            list.add(sharedpreferences.getString(arrayName + "_" + i, ""));

        return list;

    }
    //Integer array list
    public void removeSharedPreferences(String Key){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.remove(Key);
        editor.apply();

    }


}
