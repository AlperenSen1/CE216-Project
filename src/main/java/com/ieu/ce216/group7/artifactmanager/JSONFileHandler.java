package com.ieu.ce216.group7.artifactmanager;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ieu.ce216.group7.artifactmanager.model.Artifact;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class JSONFileHandler {
    public static List<Artifact> getArtifactsFromJSONFile(File dbFile) {
        try {
            if (!dbFile.exists()) {
                // Klasörü oluştur (yoksa)
                File parentDir = dbFile.getParentFile();
                if (parentDir != null && !parentDir.exists()) {
                    parentDir.mkdirs();
                }

                // Dosyayı oluştur ve boş liste yaz
                Files.writeString(dbFile.toPath(), "[]");
            }
        } catch (IOException e) {
            throw new RuntimeException("A problem occured when file in creation: " + e.getMessage());
        }

        String inputString;
        try {
            inputString = Files.readString(dbFile.toPath());
        } catch (IOException e) {
            throw new RuntimeException("File can't be read:" + e.getMessage());
        }

        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        List<Artifact> artifacts = null;
        try {
            artifacts = om.readValue(inputString, new TypeReference<ArrayList<Artifact>>() {});
        } catch (JsonProcessingException e) {
            artifacts = new ArrayList<>(); // JSON bozuksa en azından boş liste dön
        }

        return artifacts;
    }


    public static void saveArtifacts(List<Artifact> artifacts, File file) {
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        try {
            om.writerWithDefaultPrettyPrinter().writeValue(file, artifacts);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not write file during export.");
        }
    }

}
