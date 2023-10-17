package ru.practicum.tracker.utils;

import com.google.gson.*;
import ru.practicum.tracker.tasks.Subtask;
import ru.practicum.tracker.tasks.models.Status;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;

public class SubtaskConverterFromGson implements JsonDeserializer<Subtask> {
    @Override
    public Subtask deserialize(JsonElement jsonElement, Type type,
                               JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        Subtask subtask = new Subtask();
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        subtask.setName(jsonObject.get("name").getAsString());
        subtask.setDescription(jsonObject.get("description").getAsString());
        subtask.setId(jsonObject.get("id").getAsLong());
        subtask.setStatus(Status.valueOf(jsonObject.get("status").getAsString()));
        if (!(jsonObject.get("epicId") == null)) {
            subtask.setEpicId(Long.parseLong(jsonObject.get("epicId").getAsString()));
        }
        if (!(jsonObject.get("startTime") == null)) {
            subtask.setStartTime(LocalDateTime.parse(jsonObject.get("startTime").getAsString()));
        }
        if (!(jsonObject.get("duration") == null)) {
            subtask.setDuration(Long.parseLong(jsonObject.get("duration").getAsString()));
        }
        return subtask;
    }
}
