/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli.io.impl;

import java.io.PrintStream;
import java.util.Locale;

import io.github.mmm.base.exception.ApplicationException;
import io.github.mmm.base.text.Localizable;
import io.github.mmm.cli.exception.CliException;
import io.github.mmm.cli.io.CliOut;

/**
 * Implementation of {@link CliOut} that logs to a given {@link PrintStream}.
 */
public class CliOutLogger implements CliOut {

  private final PrintStream out;

  private final String prefix;

  private final Locale locale;

  private final boolean printStacktraces;

  /**
   * The constructor.
   *
   * @param out the {@link PrintStream} to write to.
   * @param prefix the loglevel prefix.
   * @param locale the {@link Locale} to use.
   * @param printStacktraces - {@code true} to print stacktraces of {@link Throwable exceptions}, {@code false}
   *        otherwise.
   */
  public CliOutLogger(PrintStream out, String prefix, Locale locale, boolean printStacktraces) {

    super();
    this.out = out;
    this.prefix = prefix;
    this.locale = locale;
    this.printStacktraces = printStacktraces;
  }

  @Override
  public void log(String message) {

    this.out.print(this.prefix);
    this.out.println(message);
  }

  @Override
  public void log(Object... messages) {

    if (messages != null) {
      for (Object message : messages) {
        if (message instanceof Localizable) {
          message = ((Localizable) message).getLocalizedMessage(this.locale);
        }
        this.out.print(message);
      }
    }
    this.out.println();
  }

  @Override
  public void logFormat(String format, Object... args) {

    for (int i = 0; i < args.length; i++) {
      if (args[i] instanceof Localizable) {
        args[i] = ((Localizable) args[i]).getLocalizedMessage(this.locale);
      }
    }
    log(String.format(format, args));
  }

  @Override
  public void log(Throwable exception) {

    log(null, exception);
  }

  @Override
  public void log(String message, Throwable exception) {

    String msg = message;
    if (msg == null) {
      if (exception instanceof ApplicationException) {
        msg = ((ApplicationException) exception).getLocalizedMessage(this.locale);
      } else {
        msg = exception.getMessage();
      }
      if (msg == null) {
        msg = exception.getClass().getSimpleName();
      }
    }
    log(msg);
    if (this.printStacktraces && (exception != null) && !(exception instanceof CliException)) {
      exception.printStackTrace(this.out);
    }
  }

  @Override
  public boolean isSuppressed() {

    return false;
  }

}
