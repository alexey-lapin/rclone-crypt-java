package com.github.alexeylapin;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IniFile {

    private static final Pattern PATTERN_SECTION = Pattern.compile("\\s*\\[([^]]*)]\\s*");
    private static final Pattern PATTERN_KEY_VALUE = Pattern.compile("\\s*([^=]*)=(.*)");

    private final Map<String, Map<String, String>> entries = new ConcurrentHashMap<>();

    public IniFile(String path) throws IOException {
        load(path);
    }

    public void load(String path) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            String section = null;
            while ((line = br.readLine()) != null) {
                Matcher m = PATTERN_SECTION.matcher(line);
                if (m.matches()) {
                    section = m.group(1).trim();
                } else if (section != null) {
                    m = PATTERN_KEY_VALUE.matcher(line);
                    if (m.matches()) {
                        String key = m.group(1).trim();
                        String value = m.group(2).trim();
                        Map<String, String> kv = entries.computeIfAbsent(section, k -> new ConcurrentHashMap<>());
                        kv.put(key, value);
                    }
                }
            }
        }
    }

    public Map<String, Map<String, String>> getEntries() {
        return entries;
    }

    public String getString(String section, String key, String defaultValue) {
        Map<String, String> kv = entries.get(section);
        if (kv == null) {
            return defaultValue;
        }
        return kv.get(key);
    }

    public int getInt(String section, String key, int defaultValue) {
        Map<String, String> kv = entries.get(section);
        if (kv == null) {
            return defaultValue;
        }
        return Integer.parseInt(kv.get(key));
    }

    public float getFloat(String section, String key, float defaultValue) {
        Map<String, String> kv = entries.get(section);
        if (kv == null) {
            return defaultValue;
        }
        return Float.parseFloat(kv.get(key));
    }

    public double getDouble(String section, String key, double defaultValue) {
        Map<String, String> kv = entries.get(section);
        if (kv == null) {
            return defaultValue;
        }
        return Double.parseDouble(kv.get(key));
    }

}
