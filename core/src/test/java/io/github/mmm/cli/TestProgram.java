/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli;

/**
 * Stupid main program for testing.
 */
public class TestProgram extends CliMain {

  @Override
  protected void addCommands() {

    super.addCommands();
    group().add(CliCommandTest.class);
  }

  /**
   * @param args the commandline arguments.
   */
  public static void main(String[] args) {

    new TestProgram().runAndExit(args);
  }

}