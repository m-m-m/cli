/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli.io.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

import io.github.mmm.cli.io.CliIn;

/**
 * Implementation of {@link CliInReader} to read input from a {@link BufferedReader} and write output to a
 * {@link PrintStream}.
 */
public class CliInReader implements CliIn {

  private final BufferedReader in;

  private final PrintStream out;

  /**
   * The constructor.
   *
   * @param in the {@link BufferedReader} to {@link BufferedReader#readLine() read from}.
   * @param out the {@link PrintStream} to write to.
   */
  public CliInReader(BufferedReader in, PrintStream out) {

    super();
    this.in = in;
    this.out = out;
  }

  @Override
  public String readLine(String format, Object... args) {

    try {
      this.out.print(String.format(format, args));
      String line = this.in.readLine();
      this.out.println(line);
      return line;
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public char[] readPassword(String format, Object... args) {

    String password = readLine(format, args);
    if (password == null) {
      return new char[0];
    }
    return password.toCharArray();
  }

  @Override
  public void flush() {

    this.out.flush();
  }

}
