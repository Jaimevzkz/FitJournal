package com.vzkz.fitjournal.data.database.entities

import android.net.Uri
import androidx.room.TypeConverter
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.TypeAdapter
import com.vzkz.fitjournal.domain.model.UserModel
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class Converters {
    @TypeConverter
    fun fromUserModel(userModel: UserModel): String {
        return GsonProvider.gson.toJson(userModel)
    }

    @TypeConverter
    fun toUserModel(userModelString: String): UserModel {
        val type = object : TypeToken<UserModel>() {}.type
        return GsonProvider.gson.fromJson(userModelString, type)
    }
}

class LocalDateAdapter : JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {

    private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    override fun serialize(
        src: LocalDate?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(formatter.format(src))
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): LocalDate {
        return LocalDate.parse(json?.asString, formatter)
    }
}

class UriListAdapter : JsonSerializer<List<Uri>>, JsonDeserializer<List<Uri>> {

    override fun serialize(
        src: List<Uri>?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        val uriStrings = src?.map { it.toString() } ?: emptyList()
        return JsonPrimitive(GsonProvider.gson.toJson(uriStrings))
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): List<Uri> {
        val uriStrings = GsonProvider.gson.fromJson(json?.asString, List::class.java)
        return uriStrings.map { Uri.parse(it.toString()) }
    }
}

class UriTypeAdapter : TypeAdapter<Uri>(), JsonSerializer<Uri>, JsonDeserializer<Uri> {

    override fun write(out: com.google.gson.stream.JsonWriter?, value: Uri?) {
        out?.value(value?.toString())
    }

    override fun read(`in`: com.google.gson.stream.JsonReader?): Uri? {
        val uriString = `in`?.nextString()
        return uriString?.let { Uri.parse(it) }
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src?.toString())
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri? {
        if (json is JsonPrimitive && json.isString) {
            return Uri.parse(json.asString)
        } else {
            throw JsonParseException("Invalid JSON format for Uri")
        }
    }
}

object GsonProvider {
    val gson: Gson = GsonBuilder()
        .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
        .registerTypeAdapter(object : TypeToken<List<Uri>>() {}.type, UriListAdapter())
        .registerTypeAdapter(Uri::class.java, UriTypeAdapter())
        .create()
}