/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli.arg;

/**
 * {@link CliArgument} for an {@link #isOption() option}. It is either a {@link CliShortOption} or a
 * {@link CliLongOption}.
 *
 * @since 1.0.0
 */
public abstract class CliOption extends CliArgument {

  private final boolean assignment;

  /**
   * The constructor.
   *
   * @param arg the {@link #get() argument option}.
   * @param assignment - the {@link #isAssignment() assignment} flag.
   */
  public CliOption(String arg, boolean assignment) {

    super(arg);
    this.assignment = assignment;
  }

  /**
   * @return {@code true} if this option was provided with a value assignment (e.g. "--file=test.txt" for
   *         {@link CliLongOption} "--file"), {@code false} otherwise (e.g. "--file test.txt").
   */
  public boolean isAssignment() {

    return this.assignment;
  }

  @Override
  public boolean isOption() {

    return true;
  }

  @Override
  public boolean isValue() {

    return false;
  }

  @Override
  public CliValueType getValueType() {

    return null;
  }

  @Override
  public boolean isEndOptions() {

    return false;
  }

}
