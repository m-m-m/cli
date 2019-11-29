/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli.io;

/**
 * Interface to log messages and {@link Throwable exceptions}.
 */
public interface CliOut {

  /**
   * @param message the message to print.
   */
  void log(String message);

  /**
   * @param messages the array of messages to print in a single line.
   */
  void log(Object... messages);

  /**
   * Like {@link #log(String)} but with dynamic arguments.
   *
   * @param format - see {@link String#format(String, Object...)}.
   * @param args - see {@link String#format(String, Object...)}.
   */
  void logFormat(String format, Object... args);

  /**
   * @param message the error message to print.
   * @param exception the {@link Throwable} that occurred or {@code null} for none.
   */
  void log(String message, Throwable exception);

  /**
   * @param exception the {@link Throwable} that occurred.
   */
  void log(Throwable exception);

  /**
   * @return {@code true} if this {@link CliOut} is suppressed and all methods will have no effect, {@code false}
   *         otherwise.
   */
  boolean isSuppressed();

}
