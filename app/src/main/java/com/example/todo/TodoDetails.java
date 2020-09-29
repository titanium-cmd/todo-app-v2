package com.example.todo;

public class TodoDetails {
    private String todoId, todoTitle, todoDesc, isAccomplished;

    TodoDetails(){}

    public TodoDetails(String todoId, String todoTitle, String todoDesc, String isAccomplished) {
        this.todoId = todoId;
        this.todoTitle = todoTitle;
        this.todoDesc = todoDesc;
        this.isAccomplished = isAccomplished;
    }

    public String getTodoId() { return todoId; }

    public void setTodoId(String todoId) { this.todoId = todoId; }

    public String getTodoDesc() { return todoDesc; }

    public void setTodoDesc(String todoDesc) { this.todoDesc = todoDesc; }

    public String getAccomplished() { return isAccomplished; }

    public void setAccomplished(String accomplished) { isAccomplished = accomplished; }

    public String getTodoTitle() { return todoTitle; }

    public void setTodoTitle(String todoTitle) { this.todoTitle = todoTitle; }
}
