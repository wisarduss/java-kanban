package ru.practicum.tracker.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonObject;

import ru.practicum.tracker.tasks.Epic;
import ru.practicum.tracker.tasks.models.Status;

import java.lang.reflect.Type;

import java.time.LocalDateTime;

public class EpicConverterFromGson implements JsonDeserializer<Epic> {
    @Override
    public Epic deserialize(JsonElement jsonElement, Type type,
                            JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        Epic epic = new Epic();
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        epic.setName(jsonObject.get("name").getAsString());
        epic.setDescription(jsonObject.get("description").getAsString());
        epic.setId(jsonObject.get("id").getAsLong());
        epic.setStatus(Status.valueOf(jsonObject.get("status").getAsString()));
        if (!(jsonObject.get("startTime") == null)) {
            epic.setStartTime(LocalDateTime.parse(jsonObject.get("startTime").getAsString()));
        }
        if (!(jsonObject.get("duration") == null)) {
            epic.setDuration(Long.parseLong(jsonObject.get("duration").getAsString()));
        }
        if (!(jsonObject.get("endTime") == null)) {
            epic.setEndTime(LocalDateTime.parse(jsonObject.get("endTime").getAsString()));
        }
        return epic;
    }
}
