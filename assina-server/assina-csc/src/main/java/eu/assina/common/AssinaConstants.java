package eu.assina.common;

public interface AssinaConstants {
    String CSC_VERSION = "v1";
    String API_VERSION = "v1";
    // These constants MUST match those in the client in constants/index.js
    String CSC_URL_ROOT = "/csc/" +  CSC_VERSION;
    String API_URL_ROOT = "/api/" + API_VERSION;
}
