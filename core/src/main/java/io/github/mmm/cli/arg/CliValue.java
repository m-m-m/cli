/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli.arg;

/**
 * A {@link CliArgument} that is not a {@link CliOption} but a {@link #isValue() value}.
 *
 * @since 1.0.0
 */
public class CliValue extends CliArgument {

  private final CliValueType valueType;

  /**
   * The constructor.
   *
   * @param arg the {@link #get() argument value}.
   * @param valueType the {@link #getValueType() value type}.
   */
  CliValue(String arg, CliValueType valueType) {

    super(arg);
    this.valueType = valueType;
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
  public CliValueType getValueType() {

    return this.valueType;
  }

  @Override
  public boolean isShortOption() {

    return false;
  }

  @Override
  public boolean isLongOption() {

    return false;
  }

}
