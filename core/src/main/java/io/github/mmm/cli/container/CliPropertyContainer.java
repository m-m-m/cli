/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli.container;

import java.util.List;

import io.github.mmm.cli.arg.CliValue;
import io.github.mmm.property.ReadableProperty;

/**
 * Container with the metadata of a {@link io.github.mmm.property.Property} from a
 * {@link io.github.mmm.cli.command.CliCommand}.
 *
 * @since 1.0.0
 */
public interface CliPropertyContainer {

  /**
   * @return the {@link io.github.mmm.property.Property#getName() property name}.
   */
  String getPropertyName();

  /**
   * @return the plain syntax for help (e.g. "--locale|-l").
   */
  String getSyntax();

  /**
   * @return the usage (e.g. "[--locale|-l <locale>]").
   */
  String getUsage();

  /**
   * @return the help explaining this property to the user.
   */
  String getHelp();

  /**
   * @return the {@link List} of aliases (option name).
   */
  List<String> getAliases();

  /**
   * @return the index to define the order of {@link CliValue values} or value options. Will be {@code -1} for no index.
   */
  int getIndex();

  /**
   * @return {@code true} if the {@link #getIndex() index} is greater or equal to zero, {@code false} otherwise (-1).
   */
  default boolean hasIndex() {

    return (getIndex() != -1);
  }

  /**
   * @return {@code true} if {@link ReadableProperty#isMandatory() mandatory}, {@code false} otherwise.
   */
  boolean isMandatory();

  /**
   * @return {@code true} if option (e.g. "-o" or "--option"), {@code false} otherwise.
   */
  boolean isOption();

  /**
   * @return {@code true} if a user specified value (neither an {@link #isOption() option} nor a static command
   *         keyword), {@code false} otherwise.
   */
  boolean isValue();

  /**
   * @return {@code true} in case of a flag (boolean property), {@code false} otherwise.
   */
  boolean isFlag();

  /**
   * @return {@code true} if {@link io.github.mmm.cli.command.CliCommand#ALIAS_WILDCARD wildcard}, {@code false}
   *         otherwise.
   */
  boolean isWildcard();

  /**
   * @return {@code true} if this is a vararg property (multivalue), {@code false} otherwise.
   */
  boolean isVararg();

  /**
   * @return {@code true} in case of a command keyword that is a static {@link #isMandatory() mandatory} argument that
   *         is not an {@link #isOption() option}
   */
  boolean isKeyword();

}
