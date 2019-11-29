/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli.io;

import java.io.Flushable;

/**
 * Interface to read input.
 *
 * @see java.io.Console
 * @see CliConsole#in()
 */
public interface CliIn extends Flushable {

  /**
   * @return - see {@link java.io.Console#readLine()}
   */
  default String readLine() {

    return readLine("");
  }

  /**
   * @param format - see {@link java.io.Console#readLine(String, Object...)}
   * @param args - see {@link java.io.Console#readLine(String, Object...)}
   * @return - see {@link java.io.Console#readLine(String, Object...)}
   */
  String readLine(String format, Object... args);

  /**
   * @return - see {@link java.io.Console#readPassword()}
   */
  default char[] readPassword() {

    return readPassword("");
  }

  /**
   * @param format - see {@link java.io.Console#readPassword(String, Object...)}
   * @param args - see {@link java.io.Console#readPassword(String, Object...)}
   * @return - see {@link java.io.Console#readPassword(String, Object...)}
   */
  char[] readPassword(String format, Object... args);

  @Override
  void flush();

}
