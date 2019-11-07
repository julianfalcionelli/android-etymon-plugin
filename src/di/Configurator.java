package di;

import com.google.gson.Gson;
import manager.file.FileManager;
import manager.file.FileManagerImpl;
import manager.parser.ParserManager;
import manager.parser.ParserManagerImpl;

public class Configurator {
    private Configurator() {
    }

    public static ParserManager getParserManager() {
        return new ParserManagerImpl(new Gson());
    }

    public static FileManager getFileManager() {
        return new FileManagerImpl();
    }
}
