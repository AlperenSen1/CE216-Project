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
import java.util.regex.Pattern;

public class JSONFileHandler {
    public static List<Artifact> getArtifactsFromJSONFile(File dbFile) {
        String inputString=null;
        try {
            inputString= Files.readString(dbFile.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ObjectMapper om=new ObjectMapper();
        om.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        List<Artifact> artifacts=null;
        try {
            artifacts=om.readValue(inputString, new TypeReference<ArrayList<Artifact>>() {
            });
        } catch (JsonProcessingException e) {
            //throw new RuntimeException(e);
        }

        return artifacts;
    }

    public static String saveArtifacts(List<Artifact> artifacts, File dbFile) {
        String result="0";
        String inputString=null;
        try {
            inputString= Files.readString(dbFile.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ObjectMapper om=new ObjectMapper();
        om.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        try {
            om.writeValue(dbFile, artifacts);
        } catch (IOException e) {
            result="0";
            //throw new RuntimeException(e);
        }
        result="1";

        return result;
    }
}
