package ru.practicum.tracker.tasks.models;

public enum Endpoint {
    GET_TASKS,
    GET_TASK_BY_ID,
    POST_TASK,
    DELETE_TASK_BY_ID,
    DELETE_TASKS,

    GET_SUBTASKS,
    GET_SUBTASK_BY_ID,
    POST_SUBTASK,
    DELETE_SUBTASK_BY_ID,
    DELETE_SUBTASKS,

    GET_EPICS,
    GET_EPIC_BY_ID,
    POST_EPIC,
    DELETE_EPIC_BY_ID,
    DELETE_EPICS,

    GET_EPIC_SUBTASKS_BY_ID,
    GET_HISTORY,
    GET_PRIORITIZED_TASKS,

    UNKNOWN
}
