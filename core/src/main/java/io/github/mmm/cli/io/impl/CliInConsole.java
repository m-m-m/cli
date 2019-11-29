/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli.io.impl;

import java.io.Console;

import io.github.mmm.cli.io.CliIn;

/**
 * Implementation of {@link CliIn} that delegates to {@link Console}.
 */
public class CliInConsole implements CliIn {

  /** The singleton instance. */
  public static final CliInConsole INSTANCE = new CliInConsole(System.console());

  private final Console console;

  /**
   * The constructor.
   *
   * @param console the {@link Console} to adapt.
   */
  public CliInConsole(Console console) {

    super();
    this.console = console;
  }

  @Override
  public String readLine(String format, Object... args) {

    return this.console.readLine(format, args);
  }

  @Override
  public char[] readPassword(String format, Object... args) {

    return this.console.readPassword(format, args);
  }

  @Override
  public void flush() {

    this.console.flush();
  }

}
