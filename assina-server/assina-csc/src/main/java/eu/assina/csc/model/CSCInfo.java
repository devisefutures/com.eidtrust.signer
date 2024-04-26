package eu.assina.csc.model;

import java.util.List;

public interface CSCInfo {
    String getSpecs();

    String getName();

    String getLogo();

    String getRegion();

    String getLang();

    String getDescription();

    List<String> getAuthType();

    String getOauth2();

    List<String> getMethods();
}
