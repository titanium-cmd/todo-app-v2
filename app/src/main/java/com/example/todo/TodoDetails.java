package com.example.todo;

public class TodoDetails {
    private String todoId, todoTitle, todoDesc, isAccomplished, timeToAccomplish, currentTime;

    TodoDetails(){}

    public TodoDetails(String todoId, String todoTitle, String todoDesc, String isAccomplished) {
        this.todoId = todoId;
        this.todoTitle = todoTitle;
        this.todoDesc = todoDesc;
        this.isAccomplished = isAccomplished;
    }

    public String getIsAccomplished() { return isAccomplished; }

    public void setIsAccomplished(String isAccomplished) { this.isAccomplished = isAccomplished; }

    public String getTimeToAccomplish() { return timeToAccomplish; }

    public void setTimeToAccomplish(String timeToAccomplish) { this.timeToAccomplish = timeToAccomplish; }

    public String getCurrentTime() { return currentTime; }

    public void setCurrentTime(String currentTime) { this.currentTime = currentTime; }

    public String getTodoId() { return todoId; }

    public void setTodoId(String todoId) { this.todoId = todoId; }

    public String getTodoDesc() { return todoDesc; }

    public void setTodoDesc(String todoDesc) { this.todoDesc = todoDesc; }

    public String getTodoTitle() { return todoTitle; }

    public void setTodoTitle(String todoTitle) { this.todoTitle = todoTitle; }
}
