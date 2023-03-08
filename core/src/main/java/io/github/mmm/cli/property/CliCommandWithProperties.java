/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli.property;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.github.mmm.base.exception.DuplicateObjectException;
import io.github.mmm.cli.CliBundle;
import io.github.mmm.cli.command.CliCommand;
import io.github.mmm.cli.io.CliConsole;
import io.github.mmm.property.WritableProperty;

/**
 * Container for properties.
 *
 * @since 1.0.0
 */
public class CliCommandWithProperties {

  private final CliCommand command;

  private final CliPropertyGroup mandatoryOptions;

  private final CliPropertyGroup additionalOptions;

  private final CliPropertyGroup values;

  private final Map<String, CliPropertyInfo> propertyMap;

  private final CliBundle bundle;

  /**
   * The constructor.
   *
   * @param command the {@link CliCommand} owning the properties.
   * @param bundle the {@link CliBundle}.
   */
  public CliCommandWithProperties(CliCommand command, CliBundle bundle) {

    super();
    this.command = command;
    this.mandatoryOptions = new CliPropertyGroup();
    this.additionalOptions = new CliPropertyGroup();
    this.values = new CliPropertyGroup();
    this.propertyMap = new HashMap<>();
    this.bundle = bundle;
  }

  /**
   * @return the owning {@link CliCommand}.
   */
  public CliCommand getCommand() {

    return this.command;
  }

  /**
   * @param propertyInfo the {@link CliPropertyInfo} to add.
   */
  public void add(CliPropertyInfo propertyInfo) {

    for (String alias : propertyInfo.getAliases()) {
      put(alias, propertyInfo);
    }
    CliPropertyGroup group;
    if (propertyInfo.isOption() && (propertyInfo.getIndex() == -1)) {
      if (propertyInfo.isMandatory()) {
        group = this.mandatoryOptions;
      } else {
        group = this.additionalOptions;
      }
    } else {
      group = this.values;
    }
    group.add(propertyInfo);
  }

  private void put(String key, CliPropertyInfo propertyInfo) {

    CliPropertyInfo duplicate = this.propertyMap.putIfAbsent(key, propertyInfo);
    if (duplicate != null) {
      String prefix = this.command.getType().getSimpleName() + ".";
      throw new DuplicateObjectException(prefix + propertyInfo.getPropertyName(), key,
          prefix + duplicate.getPropertyName());
    }
  }

  /**
   * @return the {@link CliPropertyGroup} of {@link CliPropertyInfo#isMandatory() mandatory}
   *         {@link CliPropertyInfo#isOption() options}.
   */
  public CliPropertyGroup getMandatoryOptions() {

    return this.mandatoryOptions;
  }

  /**
   * @return the {@link CliPropertyGroup} of optional (not {@link CliPropertyInfo#isMandatory() mandatory})
   *         {@link CliPropertyInfo#isOption() options}.
   */
  public CliPropertyGroup getAdditionalOptions() {

    return this.additionalOptions;
  }

  /**
   * @return the {@link CliPropertyGroup} of {@link io.github.mmm.cli.arg.CliValue value} arguments (not
   *         {@link CliPropertyInfo#isOption() options}).
   */
  public CliPropertyGroup getValues() {

    return this.values;
  }

  /**
   * @return the {@link CliBundle} for this {@link #getCommand() command}.
   */
  public CliBundle getBundle() {

    return this.bundle;
  }

  /**
   * @param alias the {@link CliPropertyInfo#getAliases() alias} of the requested {@link CliPropertyInfo}.
   * @return the {@link CliPropertyInfo} for the given {@link CliPropertyInfo#getAliases() alias} or {@code null} if not
   *         found.
   */
  public CliPropertyInfo getPropertyInfo(String alias) {

    return this.propertyMap.get(alias);
  }

  /**
   * @return a {@link Set} with the {@link CliPropertyInfo#getPropertyName() property name}s of all
   *         {@link CliPropertyInfo#isMandatory() mandatory} {@link CliPropertyInfo properties}.
   */
  public Set<String> collectMandatoryProperties() {

    Set<String> set = new HashSet<>();
    this.mandatoryOptions.collectMandatoryProperties(set);
    this.values.collectMandatoryProperties(set);
    return set;
  }

  @Override
  public String toString() {

    return this.command.getType().getSimpleName();
  }

  /**
   * @param cliCommand the {@link CliCommand}.
   * @param console the {@link CliConsole} for logging.
   * @return the new {@link CliCommandWithProperties} instance corresponding to the given {@link #getCommand() command}.
   */
  public static CliCommandWithProperties of(CliCommand cliCommand, CliConsole console) {

    CliCommandWithProperties cwp = new CliCommandWithProperties(cliCommand, CliBundle.of(cliCommand, console));
    List<? extends WritableProperty<?>> properties = new ArrayList<>(cliCommand.getProperties());
    Collections.sort(properties);
    for (WritableProperty<?> property : properties) {
      CliPropertyInfo propertyInfo = new CliPropertyInfo(property, cliCommand, cwp.bundle);
      cwp.add(propertyInfo);
    }
    int i = 0;
    boolean mandatory = true;
    for (CliPropertyInfo value : cwp.values.getPropeties()) {
      if (value.getIndex() != i) {
        throw new IllegalStateException(
            "Invalid value " + cliCommand.getType().getSimpleName() + "." + value.getPropertyName() + "() with index "
                + value.getIndex() + " but expected index " + i + " - please fix @PropertyAlias annotation.");
      }
      if (mandatory) {
        mandatory = value.isMandatory();
      } else if (value.isMandatory()) {
        throw new IllegalStateException(
            "Invalid value " + cliCommand.getType().getSimpleName() + "." + value.getPropertyName() + "() with index "
                + value.getIndex() + " can not be mandatory if previous value is optional.");
      }
      i++;
    }
    return cwp;
  }

}
