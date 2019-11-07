package manager.file;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.SourceFolder;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.io.FileUtils;
import org.jetbrains.jps.model.serialization.PathMacroUtil;

import java.io.*;

public class FileManagerImpl implements FileManager {
    @Override
    public File getFile(String path) {
        return new File(path);
    }

    @Override
    public File getResourceFile(String path) {
        return new File(getClass().getClassLoader().getResource(path).getPath());
    }

    private InputStream getResourceAsStream(String resource) {
        return getClass().getClassLoader().getResourceAsStream(resource);
    }

    @Override
    public String getFileContent(File file) {
        String fileAsString = "";
        InputStream inputStream;

        try {
            inputStream = new FileInputStream(file);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            fileAsString = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileAsString;
    }

    @Override
    public void copyFilesToDir(File file, File dirDestination) {
        try {
            if (file.isDirectory()) {
                FileUtils.copyDirectoryToDirectory(file, dirDestination);
            } else {
                FileUtils.copyFileToDirectory(file, dirDestination);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void copyFilesToDir(File file, String pathDestination) {
        copyFilesToDir(file,  new File(pathDestination));
    }

    @Override
    public void addFilesToSource(String path, Module module) {
        ModifiableRootModel rootModel = ModuleRootManager.getInstance(module).getModifiableModel();
        File directory = new File(PathMacroUtil.getModuleDir(module.getModuleFilePath()), path);

        VirtualFile virtualFile = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(directory);
        ContentEntry content = rootModel.addContentEntry(virtualFile);
        content.addSourceFolder(virtualFile.getUrl(), false);

        rootModel.commit();
    }

    @Override
    public void removeFilesFromSource(String path, Module module) {
        // TODO Check
        ModifiableRootModel rootModel = ModuleRootManager.getInstance(module).getModifiableModel();
        File directory = new File(PathMacroUtil.getModuleDir(module.getModuleFilePath()), path);

        VirtualFile virtualFile = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(directory);
        ContentEntry content = rootModel.addContentEntry(virtualFile);

        for (SourceFolder sourceFolder: content.getSourceFolders()) {
            content.removeSourceFolder(sourceFolder);
        }

        rootModel.commit();
    }
}
