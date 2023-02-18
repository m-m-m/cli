/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.nls.cli.exception;

import io.github.mmm.cli.CliValue;
import io.github.mmm.cli.exception.CliException;
import io.github.mmm.nls.cli.NlsBundleCli;

/**
 * Thrown if a mandatory {@link CliValue} is missing.
 */
public class CliTypeNotExtendingException extends CliException {

  private static final long serialVersionUID = 1L;

  /**
   * The constructor.
   *
   * @param type the illegal type.
   * @param superType the super-type that the given {@code type} does not extend or implement.
   */
  public CliTypeNotExtendingException(Object type, Object superType) {

    super(NlsBundleCli.INSTANCE.errTypeNotExtending(type, superType));
  }

}
