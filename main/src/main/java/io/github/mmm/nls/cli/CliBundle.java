package io.github.mmm.nls.cli;

import java.util.ResourceBundle;

import io.github.mmm.base.i18n.Localizable;
import io.github.mmm.cli.io.CliConsole;

/**
 * Simple wrapper for {@link ResourceBundle} to circumvent flaws and bugs in JDK.
 */
public class CliBundle {

  private final String name;

  private final ResourceBundle bundle;

  private final CliConsole console;

  /**
   * The constructor.
   *
   * @param name the {@link ResourceBundle#getBundle(String) base name} of the {@link ResourceBundle}.
   * @param console the {@link CliConsole}.
   */
  public CliBundle(String name, CliConsole console) {

    super();
    this.name = Localizable.BUNDLE_PREFIX + name;
    this.bundle = ResourceBundle.getBundle(this.name, console.getLocale());
    this.console = console;
  }

  /**
   * @param key the {@link ResourceBundle#getString(String) property key} of the value to localize.
   * @return the localized value.
   */
  public String get(String key) {

    return get(key, null);
  }

  /**
   * @param key the {@link ResourceBundle#getString(String) property key} of the value to localize.
   * @param fallback the fallback to use if undefined.
   * @return the localized value.
   */
  public String get(String key, String fallback) {

    if (this.bundle.containsKey(key)) {
      return this.bundle.getString(key);
    } else {
      if (fallback == null) {
        // JDK Bug:
        // https://stackoverflow.com/questions/63852026/java-resourcebundle-loaded-from-properties-file-is-missing-base-name
        // String name = this.bundle.getBaseBundleName();
        this.console.warning().logFormat("Missing resource-bundle value %s.%s", this.name, key);
        return key;
      }
      return fallback;
    }
  }

  /**
   * @param command the {@link CliCommand} to get {@link ResourceBundle} for.
   * @param console the {@link CliConsole}.
   * @return the new {@link CliBundle}.
   */
  public static CliBundle of(CliCommand command, CliConsole console) {

    return new CliBundle(command.getType().getQualifiedName(), console);
  }

  /**
   * @param commandClass the {@link Class} reflecting the {@link CliCommand} interface to get {@link ResourceBundle}
   *        for.
   * @param console the {@link CliConsole}.
   * @return the new {@link CliBundle}.
   */
  public static CliBundle of(Class<? extends CliCommand> commandClass, CliConsole console) {

    return new CliBundle(commandClass.getName(), console);
  }
}
