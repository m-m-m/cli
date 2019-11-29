/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli.io.impl;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import io.github.mmm.cli.io.CliConsole;
import io.github.mmm.cli.io.CliIn;
import io.github.mmm.cli.io.CliLogLevel;
import io.github.mmm.cli.io.CliOut;

/**
 * Interface for a console as standard in, out, and err. Allows to read user-input and output messages or errors. It is
 * similar to a logger but specific for the needs of command-line-interfaces.
 */
public class CliConsoleImpl implements CliConsole {

  private final Map<CliLogLevel, CliOut> outMap;

  private CliIn in;

  private PrintStream stdOut;

  private PrintStream stdErr;

  private CliLogLevel level;

  private Locale locale;

  /**
   * The constructor.
   */
  public CliConsoleImpl() {

    super();
    this.outMap = new HashMap<>();
    this.stdOut = System.out;
    this.stdErr = System.err;
    this.in = CliInConsole.INSTANCE;
    this.level = CliLogLevel.INFO;
    this.locale = Locale.getDefault();
  }

  @Override
  public PrintStream getStdOut() {

    return this.stdOut;
  }

  /**
   * @param stdOut the new value of {@link #getStdOut()}.
   */
  public void setStdOut(PrintStream stdOut) {

    Objects.requireNonNull(stdOut, "stdOut");
    if (this.stdOut == stdOut) {
      return;
    }
    this.stdOut = stdOut;
    reset();
  }

  @Override
  public PrintStream getStdErr() {

    return this.stdErr;
  }

  /**
   * @param stdErr new value of {@link #getStdErr()}.
   */
  public void setStdErr(PrintStream stdErr) {

    Objects.requireNonNull(stdErr, "stdErr");
    if (this.stdErr == stdErr) {
      return;
    }
    this.stdErr = stdErr;
    reset();
  }

  @Override
  public CliLogLevel getLogLevel() {

    return this.level;
  }

  /**
   * @param level new value of {@link #getLogLevel()}.
   */
  public void setLogLevel(CliLogLevel level) {

    Objects.requireNonNull(level, "level");
    if (this.level == level) {
      return;
    }
    this.level = level;
    reset();
  }

  /**
   * @return the {@link Locale} to use.
   */
  public Locale getLocale() {

    return this.locale;
  }

  /**
   * @param locale new value of {@link #getLocale()}.
   */
  public void setLocale(Locale locale) {

    if (locale == null) {
      locale = Locale.getDefault();
    }
    if (Objects.equals(this.locale, locale)) {
      return;
    }
    this.locale = locale;
    reset();
  }

  @Override
  public CliIn in() {

    return this.in;
  }

  /**
   * @param in the new value of {@link #in()}.
   */
  public void setIn(CliIn in) {

    Objects.requireNonNull(in, "in");
    this.in = in;
  }

  /**
   * @param stdIn the standard input used to read user input from.
   */
  public void setIn(BufferedReader stdIn) {

    this.in = new CliInReader(stdIn, getStdOut());
  }

  private void reset() {

    this.outMap.clear();
  }

  @Override
  public CliOut out(CliLogLevel logLevel) {

    return this.outMap.computeIfAbsent(logLevel, this::createOutInternal);
  }

  private CliOut createOutInternal(CliLogLevel logLevel) {

    if ((logLevel == null) || this.level.includes(logLevel)) {
      return createOut(logLevel);
    } else {
      return CliOutNone.INSTANCE;
    }
  }

  /**
   * @param logLevel the {@link CliLogLevel} to log to.
   * @return the {@link CliOut}.
   */
  protected CliOut createOut(CliLogLevel logLevel) {

    return new CliOutLogger(getOut(logLevel), getOutPrefix(logLevel), this.locale, isPrintStacktrace(logLevel));
  }

  /**
   * May be overridden to change defaults (e.g. do log warnings on standard out instead of standard error).
   *
   * @param logLevel the {@link CliLogLevel} to log to. May be {@code null}.
   * @return the {@link PrintStream} to use for the given {@link CliLogLevel}.
   */
  protected PrintStream getOut(CliLogLevel logLevel) {

    if ((logLevel != null) && (logLevel.ordinal() >= CliLogLevel.WARNING.ordinal())) {
      return this.stdErr;
    }
    return this.stdOut;
  }

  /**
   * May be overridden to change style (e.g. "[DEBUG] " instead of "DEBUG: ") or to do localization.
   *
   * @param logLevel the {@link CliLogLevel} to log to.
   * @return the prefix to log for the given {@link CliLogLevel}.
   */
  protected String getOutPrefix(CliLogLevel logLevel) {

    if (logLevel == null) {
      return "";
    }
    return logLevel.name() + ": ";
  }

  /**
   * @param logLevel the {@link CliLogLevel} to log to.
   * @return {@code true} if stacktraces should be printed.
   */
  public boolean isPrintStacktrace(CliLogLevel logLevel) {

    if (this.level == CliLogLevel.DEBUG) {
      return true;
    }
    return logLevel == CliLogLevel.ERROR;
  }

  @Override
  public void flush() {

    this.stdOut.flush();
    this.stdErr.flush();
    this.in.flush();
  }

}
