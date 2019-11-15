/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A single argument of a {@code main} method from a command-line-interface (CLI). A {@link CliArgument} is either a
 * {@link CliOption} or a {@link CliValue}.
 *
 * @see CliArgs#next()
 */
public abstract class CliArgument {

  /**
   * The {@link #get() argument} to indicate the end of the options. If this string is given as argument, any further
   * arguments are treated as {@link CliValue values}. This allows to provide {@link CliValue}s (e.g. a filename)
   * starting with a hyphen ('-'). Please note that the first occurrence of this string given as argument will not be
   * supplied as {@link CliArgument} but only parsed internally to hide complexity for the developer using this API.
   */
  public static final String END_OPTIONS = "--";

  final String arg;

  /**
   * The constructor.
   *
   * @param arg the {@link #get() argument}.
   */
  protected CliArgument(String arg) {

    super();
    this.arg = arg;
  }

  /**
   * @return the argument text (e.g. "-h" for {@link CliShortOption}, "--help" for {@link CliLongOption}, or
   *         "archive.zip" for a {@link CliValue}.
   */
  public String get() {

    return this.arg;
  }

  /**
   * @return the name of a {@link CliOption} or {@code null} if not an {@link #isOption() option} (in case of a
   *         {@link CliValue}). For the {@link CliShortOption} "-h" this method will return "h", and for
   *         {@link CliLongOption} "--help" it will return "help".
   */
  public abstract String getOptionName();

  /**
   * @return {@code true} if this {@link CliArgument} is a {@link CliOption} (e.g. "-h" or "--help"), {@code false}
   *         otherwise (if it is a {@link CliValue}).
   */
  public abstract boolean isOption();

  /**
   * @return {@code true} if this {@link CliArgument} is a {@link CliShortOption} (e.g. "-h"), {@code false} otherwise.
   */
  public abstract boolean isShortOption();

  /**
   * @return {@code true} if this {@link CliArgument} is a {@link CliLongOption} (e.g. "--help"), {@code false}
   *         otherwise.
   */
  public abstract boolean isLongOption();

  /**
   * @return {@code true} if this {@link CliArgument} is a {@link CliValue}, {@code false} otherwise (if it is a
   *         {@link CliOption}).
   */
  public abstract boolean isValue();

  /**
   * @return {@code true} if {@link #END_OPTIONS} was detected, {@code false} otherwise. If {@link #END_OPTIONS} is
   *         parsed for the first time, it will be skipped, all following arguments will be parsed as {@link CliValue}
   *         no matter if they start with hyphen ('-') or not and will return {@code true} for this method.
   */
  public abstract boolean isEndOptions();

  @Override
  public int hashCode() {

    return this.arg.hashCode();
  }

  @Override
  public boolean equals(Object obj) {

    if (obj == this) {
      return true;
    } else if ((obj == null) || (obj.getClass() != getClass())) {
      return false;
    }
    CliArgument other = (CliArgument) obj;
    return Objects.equals(this.arg, other.arg);
  }

  @Override
  public String toString() {

    return this.arg;
  }

  /**
   * @param args the command-line arguments from {@code main} method.
   * @return the parsed {@link List} of {@link CliArgument}s.
   */
  public static List<CliArgument> parse(String... args) {

    boolean endOpts = false;
    List<CliArgument> cliArgs = new ArrayList<>(args.length + 2);
    for (int argsIndex = 0; argsIndex < args.length; argsIndex++) {
      String arg = args[argsIndex];
      if (endOpts) {
        cliArgs.add(new CliValue(arg, true));
      } else if (arg.startsWith("-")) {
        if (arg.equals(END_OPTIONS)) {
          endOpts = true;
        } else {
          int equalsIndex = arg.indexOf('=');
          CliValue value = null;
          if (equalsIndex > 0) {
            String optValue = arg.substring(equalsIndex + 1);
            value = new CliValue(optValue, false);
            arg = arg.substring(0, equalsIndex);
          }
          if (arg.startsWith("--")) {
            cliArgs.add(new CliLongOption(arg));
          } else {
            int len = arg.length();
            if (len == 1) {
              cliArgs.add(new CliValue(arg, false));
            } else if (len == 2) {
              cliArgs.add(new CliShortOption(arg));
            } else {
              for (int i = 1; i < len; i++) {
                cliArgs.add(new CliShortOption("-" + arg.charAt(i)));
              }
            }
          }
          if (value != null) {
            cliArgs.add(value);
          }
        }
      } else {
        cliArgs.add(new CliValue(arg, false));
      }
    }
    return Collections.unmodifiableList(cliArgs);
  }

}
