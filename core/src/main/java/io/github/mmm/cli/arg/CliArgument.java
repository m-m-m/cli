/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli.arg;

import java.util.ArrayList;
import java.util.List;

/**
 * A single argument of a {@code main} method from a command-line-interface (CLI). A {@link CliArgument} is either a
 * {@link CliOption} or a {@link CliValue}.
 *
 * @since 1.0.0
 * @see CliArgs#getFirst()
 * @see #getNext()
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

  CliArgument next;

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
   * @return the {@link CliValueType} or {@code null} in case of an {@link #isOption() option}.
   */
  public abstract CliValueType getValueType();

  /**
   * @return {@code true} if {@link #END_OPTIONS} was detected, {@code false} otherwise. If {@link #END_OPTIONS} is
   *         parsed for the first time, it will be skipped, all following arguments will be parsed as {@link CliValue}
   *         no matter if they start with hyphen ('-') or not and will return {@code true} for this method.
   */
  public boolean isEndOptions() {

    return getValueType() == CliValueType.VALUE_END_OPTION;
  }

  /**
   * @return the next {@link CliArgument} or {@code null} if this is the last argument.
   */
  public CliArgument getNext() {

    return this.next;
  }

  /**
   * @return the {@link #getNext() next} {@link CliOption} (skipping all {@link #getNext() next} {@link CliValue}s) or
   *         {@code null} if no more {@link CliOption} follows.
   */
  public CliOption getNextOption() {

    CliArgument current = this.next;
    while ((current != null) && !current.isOption()) {
      current = current.next;
    }
    return (CliOption) current;
  }

  /**
   * @return the {@link #getNext() next} {@link CliValue} (skipping all {@link #getNext() next} {@link CliOption}s) or
   *         {@code null} if no more {@link CliValue} follows.
   */
  public CliValue getNextValue() {

    CliArgument current = this.next;
    while ((current != null) && !current.isValue()) {
      current = current.next;
    }
    return (CliValue) current;
  }

  /**
   * If this is a {@link CliOption} followed by a {@link CliValue}, this method will return that value (e.g. for "--key
   * foo" it will return "foo"). If however such {@link CliOption} is followed by another {@link CliOption}, this method
   * will return {@code null} (e.g. for "-x -z"). Finally, if this is a {@link CliValue}, this method will return its
   * {@link #get() value}.
   *
   * @return the same as {@link #get()} if this is a {@link CliValue}, otherwise if this a {@link CliOption} the value
   *         of the {@link #getNext() next} argument if a {@link CliValue} or otherwise {@code null}.
   */
  public String getValue() {

    if (isValue()) {
      return this.arg;
    } else if ((this.next != null) && (this.next.isValue())) {
      return this.next.get();
    }
    return null;
  }

  /**
   * Like {@link #getValue()} but returns all the next values.
   *
   * @return a {@link List} with the {@link #getValue() value} of this {@link CliArgument} followed by all
   *         {@link #getNext() next} {@link CliValue}s available. If {@link #getValue()} return {@code null} this method
   *         will return an {@link List#isEmpty() empty} {@link List}.
   */
  public List<String> getValues() {

    List<String> values = new ArrayList<>();
    CliArgument argument;
    if (isValue()) {
      argument = this;
    } else {
      argument = this.next;
    }
    while ((argument != null) && argument.isValue() && !argument.isEndOptions()) {
      values.add(argument.get());
      argument = argument.next;
    }
    return values;
  }

  @Override
  public String toString() {

    return this.arg;
  }

}
