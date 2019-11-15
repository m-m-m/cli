/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli;

/**
 * {@link CliOption} in long format (e.g. "--help").
 */
public class CliLongOption extends CliOption {

  /**
   * The constructor.
   *
   * @param arg the {@link #get() argument long option} (e.g. "--help").
   * @param assignment - the {@link #isAssignment() assignment} flag.
   */
  public CliLongOption(String arg, boolean assignment) {

    super(arg, assignment);
    if ((arg.length() < 3) || !arg.startsWith(CliArgument.END_OPTIONS)) {
      throw new IllegalArgumentException(arg);
    }
  }

  @Override
  public boolean isLongOption() {

    return true;
  }

  @Override
  public boolean isShortOption() {

    return false;
  }

  @Override
  public String getOptionName() {

    return this.arg.substring(2);
  }

}
