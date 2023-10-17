package ru.practicum.tracker.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import ru.practicum.tracker.tasks.Subtask;

import java.lang.reflect.Type;

public class SubtaskConverterToGson implements JsonSerializer<Subtask> {
    @Override
    public JsonElement serialize(Subtask subtask, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", subtask.getName());
        jsonObject.addProperty("description", subtask.getDescription());
        jsonObject.addProperty("id", subtask.getId());
        jsonObject.addProperty("status", subtask.getStatus().toString());
        jsonObject.addProperty("epicId", subtask.getEpicId());
        if (subtask.getStartTime() != null) {
            jsonObject.addProperty("startTime", subtask.getStartTime().toString());
        }
        if (subtask.getDuration() != null) {
            jsonObject.addProperty("duration", subtask.getDuration().toString());
        }
        return jsonObject;
    }
}
