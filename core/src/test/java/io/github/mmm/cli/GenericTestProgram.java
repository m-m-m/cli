/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli;

import io.github.mmm.cli.command.CliCommand;

/**
 * Generic main program for testing.
 */
public class GenericTestProgram extends CliMain {

  /**
   * The constructor.
   *
   * @param commandInterface the {@link CliCommand} to test.
   */
  public GenericTestProgram(Class<? extends CliCommand> commandInterface) {

    super();
    group().add(commandInterface);
  }

}