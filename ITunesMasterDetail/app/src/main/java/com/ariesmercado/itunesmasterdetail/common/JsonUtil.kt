package com.ariesmercado.itunesmasterdetail.common

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JsonUtil @Inject constructor() {

    internal val jsonFormat = Json {
        useAlternativeNames = false
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
        prettyPrint = true
    }

    internal inline fun <reified T> encodeToString(data: T): String {
        return try {
            jsonFormat.encodeToString(data)
        } catch (e: Exception) {
            ""
        }

    }

    internal inline fun <reified T> decodeFromString(string: String): T {
        return try {
            jsonFormat.decodeFromString<T>(string)
        } catch (e: Exception) {
            throw e
        }
    }

}