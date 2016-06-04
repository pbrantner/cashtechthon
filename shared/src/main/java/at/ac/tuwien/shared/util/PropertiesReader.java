package at.ac.tuwien.shared.util;

import java.util.ResourceBundle;

public class PropertiesReader {
    private ResourceBundle bundle;

    public PropertiesReader() {
        this.bundle = ResourceBundle.getBundle("application");
    }

    public PropertiesReader(String name) {
        this.bundle = ResourceBundle.getBundle(name);
    }

    public String getString(String key) {
        return this.bundle.getString(key);
    }

    public int getInt(String key) {
        return Integer.parseInt(getString(key));
    }
}
