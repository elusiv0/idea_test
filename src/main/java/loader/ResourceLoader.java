package loader;

import java.io.InputStream;

public class ResourceLoader {
    private String filePath;

    public ResourceLoader(String filePath) {
        this.filePath = filePath;
    }

    public InputStream getResourceInputStream() throws IllegalArgumentException {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(filePath);

        if (inputStream == null) {
            throw new IllegalArgumentException("file " + filePath + " not found in resource folder");
        }

        return inputStream;
    }
}
