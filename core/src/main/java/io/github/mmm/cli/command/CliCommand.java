/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli.command;

import io.github.mmm.bean.WritableBean;
import io.github.mmm.cli.CliMain;

/**
 * Interface for a single command of a command-line-interface (CLI). E.g. a help command triggered via "--help" or "-h"
 * prints out the usage, while a version command triggered via "--version" or "-v" prints the program version.
 */
public interface CliCommand extends WritableBean {

  /**
   * Executes this {@link CliCommand}. Has to be implemented as default method.
   *
   * @param main the {@link CliMain} program.
   * @return the {@link System#exit(int) exit code}.
   */
  int run(CliMain main);

}
