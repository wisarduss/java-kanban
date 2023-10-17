package ru.practicum.tracker.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import ru.practicum.tracker.tasks.Task;

import java.lang.reflect.Type;

public class TaskConverterToGson implements JsonSerializer<Task> {


    @Override
    public JsonElement serialize(Task task, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", task.getName());
        jsonObject.addProperty("description", task.getDescription());
        jsonObject.addProperty("id", task.getId());
        jsonObject.addProperty("status", task.getStatus().toString());
        if (task.getStartTime() != null) {
            jsonObject.addProperty("startTime", task.getStartTime().toString());
        }
        if (task.getEndTime() != null) {
            jsonObject.addProperty("endTime", task.getEndTime().toString());
        }
        return jsonObject;
    }
}
