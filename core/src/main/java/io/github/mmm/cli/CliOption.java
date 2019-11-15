/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli;

/**
 * {@link CliArgument} for an {@link #isOption() option}. It is either a {@link CliShortOption} or a
 * {@link CliLongOption}.
 */
public abstract class CliOption extends CliArgument {

  /**
   * The constructor.
   *
   * @param arg the {@link #get() argument option}.
   */
  public CliOption(String arg) {

    super(arg);
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
  public boolean isEndOptions() {

    return false;
  }

}
