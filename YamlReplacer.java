import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.wnameless.json.flattener.JsonFlattener;
import com.github.wnameless.json.unflattener.JsonUnflattener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class YamlReplacer {

  private String path;
  private String yaml;

  public YamlReplacer(String path) {
    this.path = path;
    try {
      yaml = new String(this.getClass().getClassLoader().getResourceAsStream(path).readAllBytes());
      if (yaml.isEmpty()) {
        throw new IllegalStateException("File is empty.");
      }
    } catch (IOException | NullPointerException exception) {
      throw new IllegalStateException("File does not exist.");
    }
  }

  public <T> void replace(Replacement<T> replacement) {
    Map<String, Object> oldMap = JsonFlattener.flattenAsMap(toJsonFromYaml(yaml));
    Map<String, Object> newMap = new HashMap<>();
    oldMap.forEach((key, value) -> {
      if (replacement.shouldApply(key)) {
        if (value instanceof Integer) {
          newMap.put(key, replacement.apply((T) Double.valueOf((Integer) value)));
        } else {
          newMap.put(key, replacement.apply((T) value));
        }
      } else {
        newMap.put(key, value);
      }
    });
    yaml = toYamlFromJson(JsonUnflattener.unflatten(newMap));
  }

  public String getYaml() {
    return yaml;
  }

  public void writeYamlToFile(String path) {
    File file = new File(path);
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
      writer.write(getYaml());
    } catch (IOException exception) {
      throw new IllegalStateException("Unable to write YAML to file.");
    }
  }

  public void writeYamlToFile() {
    writeYamlToFile(new File("src/main/resources").getAbsolutePath() + "/new_" + path);
  }

  private String toJsonFromYaml(String data) {
    try {
      return new ObjectMapper(new JsonFactory()).writeValueAsString(new ObjectMapper(new YAMLFactory()).readValue(data, Object.class));
    } catch (JsonProcessingException exception) {
      throw new IllegalStateException("Invalid YAML.");
    }
  }

  private String toYamlFromJson(String data) {
    try {
      return new ObjectMapper(new YAMLFactory()).writeValueAsString(new ObjectMapper(new JsonFactory()).readValue(data, Object.class));
    } catch (JsonProcessingException exception) {
      throw new IllegalStateException("Invalid YAML.");
    }
  }

}