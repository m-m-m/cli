/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli.arg;

/**
 * {@link Enum} with the available options of a {@link CliValue} {@link CliValue#getValueType() type}.
 *
 * @since 1.0.0
 */
public enum CliValueType {

  /** An option assignment (e.g. "value" in "--option=value"). */
  OPTION_ASSIGNMENT,

  /** An option value (e.g. "value" in "--option value"). */
  OPTION_VALUE,

  /** A regular value before the first option (e.g. "arg1" or "arg2" in "arg1 arg2 --option value1"). */
  VALUE_BEFORE_OPTION,

  /** A continued value after an {@link #OPTION_VALUE} (e.g. "value2" in "--option value1 value2"). */
  VALUE_CONTINUED,

  /**
   * A regular value after the options have explicitly ended (e.g. "arg1" or "arg2" in "--option value1 value2 -- arg1
   * arg2").
   */
  VALUE_END_OPTION;

  /**
   * @return {@code true} if {@link #OPTION_VALUE} or {@link #OPTION_ASSIGNMENT}, {@code false} otherwise.
   */
  public boolean isOption() {

    return (this == OPTION_VALUE) || (this == OPTION_ASSIGNMENT);
  }

}
