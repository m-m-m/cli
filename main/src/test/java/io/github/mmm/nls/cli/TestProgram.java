/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.nls.cli;

/**
 * Stupid main program for testing.
 */
public class TestProgram extends NlsMain {

  /**
   * The constructor.
   */
  public TestProgram() {

    super();
    add(CliCommandTest.class);
  }

  public static void main(String[] args) {

    new TestProgram().runAndExit(args);
  }

}