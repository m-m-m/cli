/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli.io.impl;

import io.github.mmm.cli.io.CliOut;

/**
 * Implementation of {@link CliOut} that does nothing.
 */
public class CliOutNone implements CliOut {

  /** The singleton instance. */
  public static final CliOutNone INSTANCE = new CliOutNone();

  /**
   * The constructor.
   */
  public CliOutNone() {

    super();
  }

  @Override
  public void log(String message) {

  }

  @Override
  public void log(Object... messages) {

  }

  @Override
  public void logFormat(String format, Object... args) {

  }

  @Override
  public void log(Throwable exception) {

  }

  @Override
  public void log(String message, Throwable exception) {

  }

  @Override
  public boolean isSuppressed() {

    return true;
  }

}
