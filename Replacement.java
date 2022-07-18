import java.util.function.Function;
import java.util.function.Predicate;

public class Replacement<T> {

  private Function<T, T> replacement;
  private Predicate<String> keyComparison;

  public Replacement(Predicate<String> keyComparison, Function<T, T> replacement) {
    this.keyComparison = keyComparison;
    this.replacement = replacement;
  }

  public T apply(T data) {
    return replacement.apply(data);
  }

  public boolean shouldApply(String key) {
    return keyComparison.test(key);
  }

}