package dev.thebathduck.duckypets.utils.command;

import lombok.Getter;

public class Argument {
    private final @Getter String arguments;
    private final @Getter String argumentsRaw;
    private final @Getter String description;
    private final @Getter String permission;

    public Argument(String argumentsRaw, String arguments, String description, String permission) {
        this.argumentsRaw = argumentsRaw;
        this.arguments = arguments;
        this.description = description;
        this.permission = permission;
    }

    public Argument(String argumentsRaw, String arguments, String description) {
        this.argumentsRaw = argumentsRaw;
        this.arguments = arguments;
        this.description = description;
        this.permission = null;
    }
}