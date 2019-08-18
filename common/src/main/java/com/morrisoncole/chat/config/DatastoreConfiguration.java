package com.morrisoncole.chat.config;

import javax.annotation.Nullable;

public interface DatastoreConfiguration {

    @Nullable
    String getProjectId();

    @Nullable
    String getHost();

    boolean getUseCredentials();

    String getCredentialsPath();
}
