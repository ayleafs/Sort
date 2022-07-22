package me.sixteen_.sort.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.*;
import java.nio.file.Files;

/**
 * @author ayleafs
 * @param configPath Config file path
 */
public record ConfigHandler<T>(@Getter File configPath, @Getter Class<T> clazz) {
    private final static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @SneakyThrows
    public T readConfig() {
        T config = clazz.getDeclaredConstructor().newInstance();

        // make sure the config exists
        if (!configPath.exists()) {
            // write and return a new one
            populateConfig(config);
            return config;
        }

        // read the file and parse json
        try {
            InputStream in = new FileInputStream(configPath);
            config = GSON.fromJson(new InputStreamReader(in), clazz);
        } catch (FileNotFoundException ignored) { // we literally check but ok
        }

        return config;
    }

    public void populateConfig(T config) {
        String jsonString = GSON.toJson(config);

        try {
            Files.write(configPath.toPath(), jsonString.getBytes());
        } catch (IOException e) {
            System.out.println("Failed to write config file.");
        }
    }
}
