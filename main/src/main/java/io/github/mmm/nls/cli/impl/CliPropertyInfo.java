/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.nls.cli.impl;

import java.util.Collection;
import java.util.ResourceBundle;

import io.github.mmm.base.text.CaseSyntax;
import io.github.mmm.cli.io.CliConsole;
import io.github.mmm.nls.cli.CliBundle;
import io.github.mmm.nls.cli.CliCommand;
import io.github.mmm.nls.cli.property.CliCollectionProperty;
import io.github.mmm.nls.cli.property.CliProperty;
import io.github.mmm.property.ReadableProperty;
import io.github.mmm.property.WritableProperty;
import io.github.mmm.property.booleans.BooleanProperty;

/**
 * Container for a {@link CliProperty} together with {@link #getSyntax() syntax} and {@link #getUsage() usage}.
 *
 * @since 1.0.0
 */
public class CliPropertyInfo {

  private final String syntax;

  private final String usage;

  private final String help;

  private final boolean mandatory;

  private final boolean option;

  private final boolean flag;

  /**
   * The constructor.
   *
   * @param syntax the {@link #getSyntax() syntax}.
   * @param usage the {@link #getUsage() usage}.
   * @param help the {@link #getHelp() help}.
   * @param mandatory the {@link #isMandatory() mandatory flag}.
   * @param option the {@link #isOption() option flag}.
   * @param flag the {@link #isFlag() flag attribute}.
   */
  public CliPropertyInfo(String syntax, String usage, String help, boolean mandatory, boolean option, boolean flag) {

    super();
    this.syntax = syntax;
    this.usage = usage;
    this.help = help;
    this.mandatory = mandatory;
    this.option = option;
    this.flag = flag;
  }

  /**
   * The constructor.
   *
   * @param property the {@link WritableProperty}.
   * @param command the {@link CliCommand}.
   * @param console the {@link CliConsole} for logging.
   * @param bundle the {@link ResourceBundle}.
   */
  public CliPropertyInfo(WritableProperty<?> property, CliCommand command, CliConsole console, CliBundle bundle) {

    super();
    StringBuilder sb = new StringBuilder();
    this.flag = (property instanceof BooleanProperty);
    char separator = 0;
    String name = property.getName();
    String defaultName = null;
    Collection<String> aliases = command.getAliases().getAliases(name);
    if (aliases.isEmpty()) {
      this.option = false;
      sb.append(CaseSyntax.CAML_CASE.convert(name));
    } else {
      this.option = true;
      for (String alias : aliases) {
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
    }
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
      if (property instanceof CliCollectionProperty) {
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
    this.help = bundle.get(name, fallback);
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
  public String toString() {

    return this.usage;
  }

}
