/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli.container.impl;

import io.github.mmm.cli.container.CliPropertyContainer;

/**
 * Enum the availabel types of a {@link CliPropertyContainer}.
 *
 * @since 1.0.0
 */
public enum CliPropertyType {

  /**
   * Type of {@link CliPropertyContainer} that is a {@link CliPropertyContainer#isMandatory() mandatory}
   * {@link CliPropertyContainer#isOption() option}.
   */
  MANDATORY_OPTION("options.required"),

  /**
   * Type of {@link CliPropertyContainer} that is a {@link CliPropertyContainer#isOption() option} but not
   * {@link CliPropertyContainer#isMandatory() mandatory}.
   */
  ADDITIONAL_OPTION("options.optional"),

  /** Type of {@link CliPropertyContainer} that is not an {@link CliPropertyContainer#isOption() option}. */
  VALUE("parameters"),

  /** Type of {@link CliPropertyContainer} for a JVM option. */
  JVM_OPTION("jvm");

  private final String key;

  /**
   * The constructor.
   */
  private CliPropertyType(String key) {

    this.key = key;
  }

  /**
   * @return the key for localization.
   */
  public String getKey() {

    return this.key;
  }

  /**
   * @param container the {@link CliPropertyContainer} to classify.
   * @return the {@link CliPropertyType} for the given {@link CliPropertyContainer}.
   */
  public static CliPropertyType of(CliPropertyContainer container) {

    if (container.isOption()) {
      if (container.isMandatory()) {
        return MANDATORY_OPTION;
      } else {
        return ADDITIONAL_OPTION;
      }
    } else {
      return VALUE;
    }
  }

}
