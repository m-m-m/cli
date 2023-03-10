/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli.container.impl;

import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import io.github.mmm.base.text.CaseSyntax;
import io.github.mmm.cli.CliBundle;
import io.github.mmm.cli.arg.CliValue;
import io.github.mmm.cli.command.CliCommand;
import io.github.mmm.cli.container.CliPropertyContainer;
import io.github.mmm.property.ReadableProperty;
import io.github.mmm.property.WritableProperty;
import io.github.mmm.property.booleans.BooleanProperty;
import io.github.mmm.property.container.ReadableContainerProperty;
import io.github.mmm.property.container.WritableContainerProperty;

/**
 * Container for the property-metadata together with {@link #getSyntax() syntax} and {@link #getUsage() usage}.
 *
 * @since 1.0.0
 */
public final class CliPropertyContainerImpl implements CliPropertyContainer, Comparable<CliPropertyContainerImpl> {

  private final String propertyName;

  private final String syntax;

  private final String usage;

  private final String help;

  private final List<String> aliases;

  private final int index;

  private final boolean mandatory;

  private final boolean option;

  private final boolean value;

  private final boolean flag;

  private final boolean vararg;

  private final boolean wildcard;

  /**
   * The constructor.
   *
   * @param propertyName the #getPropertyName()
   * @param syntax the {@link #getSyntax() syntax}.
   * @param usage the {@link #getUsage() usage}.
   * @param help the {@link #getHelp() help}.
   * @param mandatory the {@link #isMandatory() mandatory flag}.
   * @param option the {@link #isOption() option flag}.
   * @param flag the {@link #isFlag() flag attribute}.
   */
  public CliPropertyContainerImpl(String propertyName, String syntax, String usage, String help, boolean mandatory,
      boolean option, boolean flag) {

    this(propertyName, syntax, usage, help, mandatory, option, flag, !option, -1);
  }

  /**
   * The constructor.
   *
   * @param propertyName the #getPropertyName()
   * @param syntax the {@link #getSyntax() syntax}.
   * @param usage the {@link #getUsage() usage}.
   * @param help the {@link #getHelp() help}.
   * @param mandatory the {@link #isMandatory() mandatory flag}.
   * @param option the {@link #isOption() option flag}.
   * @param flag the {@link #isFlag() flag attribute}.
   * @param value the {@link #isValue() value flag}.
   * @param index the {@link #getIndex() index}.
   */
  public CliPropertyContainerImpl(String propertyName, String syntax, String usage, String help, boolean mandatory,
      boolean option, boolean flag, boolean value, int index) {

    super();
    this.propertyName = propertyName;
    this.syntax = syntax;
    this.usage = usage;
    this.help = help;
    this.index = index;
    this.mandatory = mandatory;
    this.option = option;
    this.value = value;
    this.flag = flag;
    this.aliases = Collections.emptyList();
    this.vararg = false;
    this.wildcard = false;
  }

  /**
   * The constructor.
   *
   * @param property the {@link WritableProperty}.
   * @param command the {@link CliCommand}.
   * @param bundle the {@link ResourceBundle}.
   */
  public CliPropertyContainerImpl(WritableProperty<?> property, CliCommand command, CliBundle bundle) {

    super();
    this.propertyName = property.getName();
    this.flag = (property instanceof BooleanProperty);
    this.aliases = command.getAliases().getAliases(this.propertyName);
    String defaultName = null;
    StringBuilder sb = new StringBuilder();
    char separator = 0;
    int i = -1;
    boolean isOption = false;
    boolean isVararg = (property instanceof WritableContainerProperty);
    boolean isWildcard = false;
    boolean isValue = true;
    for (String alias : this.aliases) {
      if (isIndex(alias)) {
        i = Integer.parseInt(alias);
        continue;
      } else {
        if (CliCommand.ALIAS_WILDCARD.equals(alias)) {
          isWildcard = true;
        } else {
          isValue = false;
          if (alias.startsWith("-")) {
            isOption = true;
          }
        }
      }
      if ((defaultName == null) || !defaultName.startsWith("--")) {
        defaultName = alias;
      }
      if (separator == 0) {
        separator = '|';
      } else {
        sb.append(separator);
      }
      sb.append(alias);
    }
    if (sb.isEmpty()) {
      sb.append(CaseSyntax.UNCAPITALIZED.convert(this.propertyName));
    }
    this.index = i;
    if (isOption && isWildcard) {
      throw new IllegalStateException(
          "Invalid property " + property.getQualifiedName() + " - option may not be a wildcard (use * as alias).");
    }
    this.option = isOption;
    this.value = isValue;
    this.vararg = isVararg;
    this.wildcard = isWildcard;
    this.syntax = sb.toString();
    sb.setLength(0);
    this.mandatory = property.isMandatory();
    if (!this.mandatory) {
      sb.append('[');
    }
    if (this.value) {
      sb.append('<');
    }
    sb.append(this.syntax);
    if (this.value) {
      sb.append('>');
    }
    if (this.option && !this.flag) {
      sb.append(" <");
      String valueName;
      if ((defaultName == null) || !defaultName.startsWith("--")) {
        valueName = "value";
      } else {
        valueName = defaultName.substring(2);
      }
      sb.append(valueName);
      sb.append('>');
      if (property instanceof ReadableContainerProperty) {
        sb.append("...");
      }
    }
    if (!this.mandatory) {
      sb.append(']');
    }
    this.usage = sb.toString();
    String fallback = null;
    if (this.flag && this.mandatory) {
      fallback = "";
    }
    this.help = bundle.get(this.propertyName, fallback);
  }

  static boolean isIndex(String alias) {

    return (alias.length() == 1) && isDigit(alias.charAt(0));
  }

  static boolean isDigit(char c) {

    return (c >= '0') && (c <= '9');
  }

  /**
   * @return the {@link WritableProperty#getName() property name}.
   */
  @Override
  public String getPropertyName() {

    return this.propertyName;
  }

  /**
   * @return the plain syntax for help (e.g. "--locale|-l").
   */
  @Override
  public String getSyntax() {

    return this.syntax;
  }

  /**
   * @return the usage (e.g. "[--locale|-l <locale>]").
   */
  @Override
  public String getUsage() {

    return this.usage;
  }

  /**
   * @return the help explaining this option to the user.
   */
  @Override
  public String getHelp() {

    return this.help;
  }

  /**
   * @return the {@link List} of aliases (option name).
   */
  @Override
  public List<String> getAliases() {

    return this.aliases;
  }

  /**
   * @return the index to define the order of {@link CliValue values} or value options. Will be {@code -1} for no index.
   */
  @Override
  public int getIndex() {

    return this.index;
  }

  /**
   * @return {@code true} if the {@link #getIndex() index} is greater or equal to zero, {@code false} otherwise (-1).
   */
  @Override
  public boolean hasIndex() {

    return (this.index != -1);
  }

  /**
   * @return {@code true} if {@link ReadableProperty#isMandatory() mandatory}, {@code false} otherwise.
   */
  @Override
  public boolean isMandatory() {

    return this.mandatory;
  }

  /**
   * @return {@code true} if option (e.g. "-o" or "--option"), {@code false} otherwise.
   */
  @Override
  public boolean isOption() {

    return this.option;
  }

  /**
   * @return {@code true} if a user specified value (neither an {@link #isOption() option} nor a static command
   *         keyword), {@code false} otherwise.
   */
  @Override
  public boolean isValue() {

    return this.value;
  }

  /**
   * @return {@code true} in case of a flag (boolean property), {@code false} otherwise.
   */
  @Override
  public boolean isFlag() {

    return this.flag;
  }

  /**
   * @return {@code true} if {@link CliCommand#ALIAS_WILDCARD wildcard}, {@code false} otherwise.
   */
  @Override
  public boolean isWildcard() {

    return this.wildcard;
  }

  /**
   * @return {@code true} if this is a vararg property (multivalue), {@code false} otherwise.
   */
  @Override
  public boolean isVararg() {

    return this.vararg;
  }

  /**
   * @return {@code true} in case of a command keyword that is a static {@link #isMandatory() mandatory} argument that
   *         is not an {@link #isOption() option}
   */
  @Override
  public boolean isKeyword() {

    return !this.option && this.mandatory && !this.value && this.flag;
  }

  @Override
  public int compareTo(CliPropertyContainerImpl o) {

    if (o == null) {
      return -1;
    }
    // 1. mandatory keywords ordered by index
    // 2. mandatory options ordered by syntax
    // 3. additional options ordered by syntax
    // 4. values ordered by index
    int result = 0;
    int cmpMandatory = this.mandatory ? -1 : 1;
    if (this.mandatory != o.mandatory) {
      result = cmpMandatory;
    }
    if (this.index != o.index) {
      if ((this.index == -1) || (o.index == -1)) {
        if (isKeyword()) {
          return -1;
        } else if (o.isKeyword()) {
          return 1;
        } else if (this.value && o.option) {
          return 1;
        } else if (this.option && o.value) {
          return -1;
        } else if (result == 0) {
          return 1;
        } else {
          return cmpMandatory;
        }
      } else {
        return (this.index > o.index) ? 1 : -1;
      }
    }
    if (result == 0)

    {
      result = this.syntax.compareTo(o.syntax);
    }
    return result;
  }

  @Override
  public String toString() {

    if (this.index >= 0) {
      return this.usage + " #" + this.index;
    }
    return this.usage;
  }

}
