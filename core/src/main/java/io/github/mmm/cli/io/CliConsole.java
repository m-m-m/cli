/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli.io;

import java.io.Flushable;
import java.io.PrintStream;

/**
 * Interface to give access to {@link CliOut output} and {@link CliIn}.
 */
public interface CliConsole extends Flushable {

  /**
   * @return the current {@link CliLogLevel} of this console.
   */
  CliLogLevel getLogLevel();

  /**
   * @return the {@link CliOut} for {@link #getStdOut() standard out}.
   */
  default CliOut out() {

    return out(null);
  }

  /**
   * @param level the {@link CliLogLevel}.
   * @return the {@link CliOut} for the given {@link CliLogLevel}.
   */
  CliOut out(CliLogLevel level);

  /**
   * @return the {@link CliOut} for {@link CliLogLevel#DEBUG}.
   */
  default CliOut debug() {

    return out(CliLogLevel.DEBUG);
  }

  /**
   * @return the {@link CliOut} for {@link CliLogLevel#INFO}.
   */
  default CliOut info() {

    return out(CliLogLevel.INFO);
  }

  /**
   * @return the {@link CliOut} for {@link CliLogLevel#WARNING}.
   */
  default CliOut warning() {

    return out(CliLogLevel.WARNING);
  }

  /**
   * @return the {@link CliOut} for {@link CliLogLevel#ERROR}.
   */
  default CliOut error() {

    return out(CliLogLevel.ERROR);
  }

  /**
   * @return the {@link CliIn} to read input values.
   */
  CliIn in();

  /**
   * @return the {@link PrintStream} to use as standard output. Defaults to {@link System#out}.
   */
  PrintStream getStdOut();

  /**
   * @return the {@link PrintStream} to use as standard error. Defaults to {@link System#err}.
   */
  PrintStream getStdErr();

  @Override
  void flush();

}
