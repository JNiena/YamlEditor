# YamlEditor
#### A simple copy-paste library to edit many YAML keys at once.

# Usage
```java
public class Main {
  public static void main(String[] args) {
    YamlReplacer replacer = new YamlReplacer("someFile.yml");
    replacer.replace(new Replacement<Double>(key.endsWith(".threshold"), value -> value * 5));
    replacer.writeYamlToFile();
    // This will replace all YAML keys ending with ".threshold", parse them as a double, and multiply their value by 5.
    // The changed YAML will then be written as a new file.
  }
}
```
