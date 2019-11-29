/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli;

/**
 * A {@link CliArgument} that is not a {@link CliOption} but a {@link #isValue() value}.
 */
public class CliValue extends CliArgument {

  private final boolean endOptions;

  /**
   * The constructor.
   *
   * @param arg the {@link #get() argument value}.
   * @param endOptions the {@link #isEndOptions() end options} flag.
   */
  CliValue(String arg, boolean endOptions) {

    super(arg);
    this.endOptions = endOptions;
  }

  @Override
  public String getOptionName() {

    return null;
  }

  @Override
  public boolean isValue() {

    return true;
  }

  @Override
  public boolean isOption() {

    return false;
  }

  @Override
  public boolean isShortOption() {

    return false;
  }

  @Override
  public boolean isLongOption() {

    return false;
  }

  @Override
  public boolean isEndOptions() {

    return this.endOptions;
  }

}
