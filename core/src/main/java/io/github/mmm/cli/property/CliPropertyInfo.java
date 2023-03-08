/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli.property;

import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import io.github.mmm.base.text.CaseSyntax;
import io.github.mmm.cli.CliBundle;
import io.github.mmm.cli.arg.CliValue;
import io.github.mmm.cli.command.CliCommand;
import io.github.mmm.property.ReadableProperty;
import io.github.mmm.property.WritableProperty;
import io.github.mmm.property.booleans.BooleanProperty;
import io.github.mmm.property.container.ReadableContainerProperty;

/**
 * Container for the property-metadata together with {@link #getSyntax() syntax} and {@link #getUsage() usage}.
 *
 * @since 1.0.0
 */
public final class CliPropertyInfo implements Comparable<CliPropertyInfo> {

  private final String propertyName;

  private final String syntax;

  private final String usage;

  private final String help;

  private final List<String> aliases;

  private final int index;

  private final boolean mandatory;

  private final boolean option;

  private final boolean flag;

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
  public CliPropertyInfo(String propertyName, String syntax, String usage, String help, boolean mandatory,
      boolean option, boolean flag) {

    super();
    this.propertyName = propertyName;
    this.syntax = syntax;
    this.usage = usage;
    this.help = help;
    this.index = -1;
    this.mandatory = mandatory;
    this.option = option;
    this.flag = flag;
    this.aliases = Collections.emptyList();
  }

  /**
   * The constructor.
   *
   * @param property the {@link WritableProperty}.
   * @param command the {@link CliCommand}.
   * @param bundle the {@link ResourceBundle}.
   */
  public CliPropertyInfo(WritableProperty<?> property, CliCommand command, CliBundle bundle) {

    super();
    this.propertyName = property.getName();
    this.flag = (property instanceof BooleanProperty);
    this.aliases = command.getAliases().getAliases(this.propertyName);
    boolean isOption = false;
    String defaultName = null;
    StringBuilder sb = new StringBuilder();
    char separator = 0;
    int i = -1;
    for (String alias : this.aliases) {
      if ((alias.length() == 1) && isDigit(alias.charAt(0))) {
        i = Integer.parseInt(alias);
        continue;
      } else {
        isOption = true;
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
    this.option = isOption;
    this.syntax = sb.toString();
    sb.setLength(0);
    this.mandatory = property.isMandatory();
    if (!this.mandatory) {
      sb.append('[');
    }
    if (!this.option) {
      sb.append('<');
    }
    sb.append(this.syntax);
    if (!this.option) {
      sb.append('>');
    } else if (!this.flag) {
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
    if (this.flag && this.option) {
      fallback = "";
    }
    this.help = bundle.get(this.propertyName, fallback);
  }

  private static boolean isDigit(char c) {

    return (c >= '0') && (c <= '9');
  }

  /**
   * @return the {@link WritableProperty#getName() property name}.
   */
  public String getPropertyName() {

    return this.propertyName;
  }

  /**
   * @return the plain syntax for help (e.g. "--locale|-l").
   */
  public String getSyntax() {

    return this.syntax;
  }

  /**
   * @return the usage (e.g. "[--locale|-l <locale>]").
   */
  public String getUsage() {

    return this.usage;
  }

  /**
   * @return the help explaining this option to the user.
   */
  public String getHelp() {

    return this.help;
  }

  /**
   * @return the {@link List} of aliases (option name).
   */
  public List<String> getAliases() {

    return this.aliases;
  }

  /**
   * @return the index to define the order of {@link CliValue values} or value options. Will be {@code -1} for no index.
   */
  public int getIndex() {

    return this.index;
  }

  /**
   * @return {@code true} if {@link ReadableProperty#isMandatory() mandatory}, {@code false} otherwise.
   */
  public boolean isMandatory() {

    return this.mandatory;
  }

  /**
   * @return {@code true} if option, {@code false} otherwise.
   */
  public boolean isOption() {

    return this.option;
  }

  /**
   * @return {@code true} in case of a flag (boolean property), {@code false} otherwise.
   */
  public boolean isFlag() {

    return this.flag;
  }

  @Override
  public int compareTo(CliPropertyInfo o) {

    if (o == null) {
      return -1;
    }
    if (this.index >= 0) {
      if (this.index >= o.index) {
        return 1;
      } else {
        return -1;
      }
    }
    return this.syntax.compareTo(o.syntax);
  }

  @Override
  public String toString() {

    return this.usage;
  }

}
