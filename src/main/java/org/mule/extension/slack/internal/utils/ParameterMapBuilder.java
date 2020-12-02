package org.mule.extension.slack.internal.utils;

import static org.mule.extension.slack.internal.connection.SlackConnection.ifPresent;
import static org.mule.extension.slack.internal.connection.SlackMethods.API_URI;
import static org.mule.extension.slack.internal.connection.SlackMethods.REACTIONS_GET;

import org.mule.runtime.api.util.MultiMap;

public class ParameterMapBuilder {

    private final MultiMap<String, String> parameterMap;

    public ParameterMapBuilder() {
        parameterMap = new MultiMap<>();
    }

    public ParameterMapBuilder addNullable(String name, Object value) {
        ifPresent(value, v -> parameterMap.put(name, value.toString()));
        return this;
    }

    public ParameterMapBuilder addNullable(String name, boolean value) {
        ifPresent(value, v -> parameterMap.put(name, String.valueOf(v)));
        return this;
    }

    public ParameterMapBuilder add(String name, Object value) {
        parameterMap.put(name, value.toString());
        return this;
    }

    public ParameterMapBuilder add(String name, boolean value) {
        parameterMap.put(name, String.valueOf(value));
        return this;
    }

    public MultiMap<String, String> build() {
        return parameterMap;
    }
}
