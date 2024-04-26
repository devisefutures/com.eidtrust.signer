package eu.assina.common;

public interface ApiError {
    String getCode();

    int getHttpCode();

    String getDescription();
}
