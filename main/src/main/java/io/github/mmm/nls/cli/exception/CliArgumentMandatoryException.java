/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.nls.cli.exception;

import io.github.mmm.cli.CliValue;
import io.github.mmm.cli.exception.CliException;
import io.github.mmm.nls.cli.NlsBundleCli;

/**
 * Thrown if a mandatory {@link CliValue} is missing.
 */
public class CliArgumentMandatoryException extends CliException {

  private static final long serialVersionUID = 1L;

  /**
   * The constructor.
   *
   * @param name the name of the required argument (option or parameter).
   */
  public CliArgumentMandatoryException(String name) {

    super(NlsBundleCli.INSTANCE.errArgumentMandatory(name));
  }

}
