package manager.file;

import com.intellij.openapi.module.Module;

import java.io.File;

public interface FileManager {
    File getFile(String path);

    File getResourceFile(String path);

    String getFileContent(File file);

    void copyFilesToDir(File file, File dirDestination);

    void copyFilesToDir(File file, String pathDestination);

    void addFilesToSource(String path, Module module);

    void removeFilesFromSource(String path, Module module);

}