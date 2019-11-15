/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli;

/**
 * {@link CliOption} in short format (e.g. "-h"). Will always be a hyphen ('-') followed by a single other character.
 */
public class CliShortOption extends CliOption {

  /**
   * The constructor.
   *
   * @param arg the {@link #get() argument short option} (e.g. "-h").
   */
  public CliShortOption(String arg) {

    super(arg);
    if ((arg.length() != 2) || (arg.charAt(0) != '-') || (arg.charAt(1) == '-')) {
      throw new IllegalArgumentException(arg);
    }
  }

  @Override
  public boolean isShortOption() {

    return true;
  }

  @Override
  public boolean isLongOption() {

    return false;
  }

  @Override
  public String getOptionName() {

    return this.arg.substring(1);
  }

}
