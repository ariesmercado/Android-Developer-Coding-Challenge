package com.ariesmercado.itunesmasterdetail.common

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.ariesmercado.itunesmasterdetail.common.constant.PreferenceKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStorageHelper @Inject constructor(
    @ApplicationContext val context: Context,
    val jsonUtil: JsonUtil
) {
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(PreferenceKey.PREF_STORAGE_KEY)

    internal suspend inline fun <reified T> saveValue(key: String, value: T) {
        withContext(Dispatchers.IO) {
            try {
                context.dataStore.edit { pref ->
                    pref[stringPreferencesKey(key)] = jsonUtil.encodeToString(value)
                }
            } catch (_: Throwable) {}
        }
    }

    internal suspend inline fun <reified T> getValue(key: String, defaultValue: T? = null): T? {
        return withContext(Dispatchers.IO) {
            try {
                context.dataStore.data.map { preferences ->
                    preferences[stringPreferencesKey(key)]?.let {
                        jsonUtil.decodeFromString<T>(it)
                    } ?: defaultValue
                }.firstOrNull()
            } catch (e: Throwable) {
                null
            }
        }
    }
}