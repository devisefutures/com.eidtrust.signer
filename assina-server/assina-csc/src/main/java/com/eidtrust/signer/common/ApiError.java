package com.eidtrust.signer.common;

public interface ApiError {
    String getCode();

    int getHttpCode();

    String getDescription();
}
