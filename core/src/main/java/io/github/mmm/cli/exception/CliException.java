/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli.exception;

import io.github.mmm.base.exception.ApplicationException;
import io.github.mmm.base.i18n.Localizable;
import io.github.mmm.cli.CliArgument;

/**
 * Abstract base class for a {@link RuntimeException} thrown for invalid {@link CliArgument}s.
 */
public abstract class CliException extends ApplicationException {

  private static final long serialVersionUID = 1L;

  /**
   * The constructor.
   *
   * @param message the {@link #getMessage() message}.
   * @param cause the {@link #getCause() cause}.
   */
  public CliException(String message, Throwable cause) {

    super(message, cause);
  }

  /**
   * The constructor.
   *
   * @param message the {@link #getMessage() message}.
   */
  public CliException(String message) {

    super(message);
  }

  /**
   * The constructor.
   *
   * @param message the {@link #getNlsMessage() message}.
   */
  public CliException(Localizable message) {

    super(message);
  }

  /**
   * The constructor.
   *
   * @param message the {@link #getNlsMessage() message}.
   * @param cause the {@link #getCause() cause}.
   */
  public CliException(Localizable message, Throwable cause) {

    super(message, cause);
  }

}
