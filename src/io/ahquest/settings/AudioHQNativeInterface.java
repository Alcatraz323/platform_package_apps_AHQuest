package io.ahquest.settings;

public interface AudioHQNativeInterface<T> {
    void onSuccess(T result);
    void onFailure(String reason);
}
