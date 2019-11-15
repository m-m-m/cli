/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli;

import java.io.PrintStream;

/**
 * This is the abstract base class for a main-program. <br>
 * You simply need to extend this class.
 */
public abstract class AbstractMain {

  private PrintStream out;

  private PrintStream err;

  /**
   * The constructor.
   */
  public AbstractMain() {

    this.out = System.out;
    this.err = System.err;
  }

  /**
   * @return the {@link PrintStream} to use as standard output. Defaults to {@link System#out}.
   */
  public PrintStream getOut() {

    return this.out;
  }

  /**
   * @param out the new value of {@link #getOut()}.
   */
  public void setOut(PrintStream out) {

    this.out = out;
  }

  /**
   * @return the {@link PrintStream} to use as standard error. Defaults to {@link System#err}.
   */
  public PrintStream getErr() {

    return this.err;
  }

  /**
   * Has to be implemented to handle that given {@link CliArgs} and do the program logic.
   *
   * @param args the {@link CliArgs}.
   * @return the {@link System#exit(int) exit code}.
   */
  public abstract int run(CliArgs args);

  /**
   * This method delegates to {@link #run(CliArgs)} and then calls {@link System#exit(int)} with the returned exit code.
   * You can easily call this additional statement from your regular {@code main} method, but this allows us to keep the
   * "evil" {@link System#exit(int)} statement in a single place.
   *
   * @param args are the commandline arguments.
   */
  protected void runAndExit(String... args) {

    int exitCode = run(new CliArgs(args));
    // CHECKSTYLE:OFF (main method)
    System.exit(exitCode); // NOSONAR
    // CHECKSTYLE:ON
  }

}
