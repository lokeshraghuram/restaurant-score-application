package com.nhs.inspection.bdd.helper;

import com.google.common.collect.Maps;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class TestHelper {

    public String readTextFileAsString(String featureNumber, String scenarioDirectory, String requestFileName) {

        String sourceAsString = null;
        try {
            sourceAsString = Files.readString(Paths.get("src", "test", "resources", "iofiles", "feature", featureNumber, scenarioDirectory, requestFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sourceAsString;
    }

    public Map<String, String> createHeaderForInspector() {
        Map<String, String> headers = Maps.newHashMap();
        headers.put("Authorization", "Basic aW5zcGVjdG9yOmluc3BlY3Rvcg==");
        return headers;
    }

    public Map<String, String> createHeaderForPublic() {
        Map<String, String> headers = Maps.newHashMap();
        headers.put("Authorization", "Basic cHVibGljOnB1YmxpYw==");
        return headers;
    }

    public String readFileFromSourceAsString(String featureNumber, String scenarioDirectory, String requestFileName) {
        Path filePath = null;
        try {
            filePath = Paths.get("src", "test", "resources", "iofiles", "feature", featureNumber, scenarioDirectory, requestFileName);
            return Files.readString(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
