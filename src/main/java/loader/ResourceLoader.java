package loader;

import java.io.InputStream;

public class ResourceLoader {
    public InputStream getResourceInputStream(String filePath) throws IllegalArgumentException {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(filePath);

        if (inputStream == null) {
            throw new IllegalArgumentException("file " + filePath + " not found in resource folder");
        }

        return inputStream;
    }
}
