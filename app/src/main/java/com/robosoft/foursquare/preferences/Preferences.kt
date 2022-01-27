package com.robosoft.foursquare.preferences

import android.content.Context
import android.content.SharedPreferences
import com.robosoft.foursquare.helper.Constants


object Preferences {
    fun setPrefs(key: String?, value: String?, context: Context) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(Constants.SP_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getPrefs(key: String?, context: Context): String? {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(Constants.SP_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(key, "notFound")
    }

    fun setArrayPrefs(arrayName: String, array: ArrayList<String?>, mContext: Context) {
        val prefs: SharedPreferences = mContext.getSharedPreferences(Constants.sharedPrefs, 0)
        val editor = prefs.edit()
        editor.putInt(arrayName + "_size", array.size)
        for (i in 0 until array.size) editor.putString(arrayName + "_" + i, array[i])
        editor.apply()
    }

    fun getArrayPrefs(arrayName: String, mContext: Context): ArrayList<String?> {
        val prefs: SharedPreferences = mContext.getSharedPreferences(Constants.sharedPrefs, 0)
        val size = prefs.getInt(arrayName + "_size", 0)
        val array: ArrayList<String?> = ArrayList(size)
        for (i in 0 until size) array.add(prefs.getString(arrayName + "_" + i, null))
        return array
    }
}