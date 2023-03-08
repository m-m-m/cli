/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli;

import io.github.mmm.nls.NlsBundle;
import io.github.mmm.nls.NlsMessage;
import io.github.mmm.nls.argument.NlsArguments;

/**
 * {@link NlsBundle} for command-line-interface (CLI).
 *
 * @since 1.0.0
 */
@SuppressWarnings("javadoc")
public final class NlsBundleCli extends NlsBundle {

  public static final NlsBundleCli INSTANCE = new NlsBundleCli();

  public NlsBundleCli() {

    super();
  }

  public NlsMessage msgJavaSystemProperty(String name) {

    return create("msgJavaSystemProperty", "Set the JVM option '{name}'.", NlsArguments.ofName(name));
  }

  public NlsMessage errArgumentMandatory(String name) {

    return create("errArgumentMandatory", "The value for '{name}' is required.", NlsArguments.ofName(name));
  }

  public NlsMessage errDuplicateOptions(Object options) {

    return create("errDuplicateOptions", "Duplicated option(s): {value}", NlsArguments.ofValue(options));
  }

  public NlsMessage errInvalidArguments(Object arguments) {

    return create("errInvalidArguments", "Invalid argument(s): {argument}", NlsArguments.ofArgument(arguments));
  }

  public NlsMessage errNoArguments() {

    return create("errNoArguments",
        "No arguments were specified. Please call with --help to read usage and provide required arguments.");
  }

}
