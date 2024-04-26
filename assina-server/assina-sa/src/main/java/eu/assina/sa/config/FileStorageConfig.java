package eu.assina.sa.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@ConfigurationProperties(prefix = "file")
public class FileStorageConfig {
    private String uploadDir;
    private ArrayList<String> extensions;

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }

    public ArrayList<String> getExtensions() {
        return extensions;
    }

    public void setExtensions(ArrayList<String> extensions) {
        this.extensions = extensions;
    }
}
