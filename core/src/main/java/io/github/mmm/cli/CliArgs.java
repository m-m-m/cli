/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli;

import java.util.Iterator;
import java.util.List;

/**
 * A simple wrapper for the arguments of a {@code main} method from a command-line-interface (CLI). Allows to easily
 * iterate and parse these arguments.
 *
 * @since 1.0.0
 * @see CliArgument#parse(String...)
 */
public class CliArgs implements Iterator<CliArgument> {

  private final List<CliArgument> args;

  private int i;

  /**
   * The constructor.
   *
   * @param args the command-line arguments from {@code main} method.
   */
  public CliArgs(String... args) {

    super();
    this.args = CliArgument.parse(args);
    this.i = 0;
  }

  /**
   * @return the {@link List} with all {@link CliArgument}s.
   */
  public List<CliArgument> getAllArguments() {

    return this.args;
  }

  @Override
  public boolean hasNext() {

    return (this.i < this.args.size());
  }

  /**
   * Like {@link #next()} but without changing state and moving to the next argument.
   *
   * @return the current argument or {@code null} if no {@link #hasNext() next argument} is available.
   */
  public CliArgument getCurrent() {

    if (this.i < this.args.size()) {
      return this.args.get(this.i);
    }
    return null;
  }

  @Override
  public CliArgument next() {

    if (this.i < this.args.size()) {
      return this.args.get(this.i++);
    }
    return null;
  }

}
