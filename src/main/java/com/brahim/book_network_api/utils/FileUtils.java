package com.brahim.book_network_api.utils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.lang3.StringUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUtils {

    public static byte[] readFileFromLocation(String fileUrl) {
        if (StringUtils.isBlank(fileUrl))
            return null;
        try {
            Path path = new File(fileUrl).toPath();
            return Files.readAllBytes(path);
        } catch (Exception e) {
            log.warn("No file found in the path: " + fileUrl);
        }
        return null;
    }
}
