package ru.practicum.tracker.utils;


import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonObject;
import ru.practicum.tracker.tasks.Task;
import ru.practicum.tracker.tasks.models.Status;

import java.lang.reflect.Type;
import java.time.LocalDateTime;

public class TaskConverterFromGson implements JsonDeserializer<Task> {

    @Override
    public Task deserialize(JsonElement jsonElement, Type type,
                            JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        Task task = new Task();
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        task.setName(jsonObject.get("name").getAsString());
        task.setDescription(jsonObject.get("description").getAsString());
        task.setId(jsonObject.get("id").getAsLong());
        task.setStatus(Status.valueOf(jsonObject.get("status").getAsString()));
        if (!(jsonObject.get("startTime") == null)) {
            task.setStartTime(LocalDateTime.parse(jsonObject.get("startTime").getAsString()));
        }
        if (!(jsonObject.get("duration") == null)) {
            task.setDuration(Long.parseLong(jsonObject.get("duration").getAsString()));
        }
        return task;
    }
}
