/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.nls.cli.impl;

import java.util.HashMap;
import java.util.Map;

import io.github.mmm.nls.cli.property.CliProperty;

/**
 * Container for {@link CliProperty}-objects.
 *
 * @since 1.0.0
 */
public class CliProperties {

  private final CliPropertyGroup mandatoryOptions;

  private final CliPropertyGroup additionalOptions;

  private final CliPropertyGroup parameters;

  private final Map<String, CliPropertyInfo> propertyMap;

  /**
   * The constructor.
   */
  public CliProperties() {

    super();
    this.mandatoryOptions = new CliPropertyGroup();
    this.additionalOptions = new CliPropertyGroup();
    this.parameters = new CliPropertyGroup();
    this.propertyMap = new HashMap<>();
  }

  /**
   * @param propertyInfo the {@link CliProperty} to add.
   */
  public void add(CliPropertyInfo propertyInfo) {

    String name = propertyInfo.getSyntax();
    CliPropertyInfo duplicate = this.propertyMap.putIfAbsent(name, propertyInfo);
    if (duplicate == null) {
      if (propertyInfo.isOption()) {
        if (propertyInfo.isMandatory()) {
          if (!propertyInfo.isFlag()) {
            this.mandatoryOptions.add(propertyInfo);
          }
        } else {
          this.additionalOptions.add(propertyInfo);
        }
      } else {
        this.parameters.add(propertyInfo);
      }

    }
  }

  /**
   * @return the {@link CliPropertyGroup} of {@link CliProperty#isMandatory() mandatory} {@link CliProperty#isOption()
   *         options}.
   */
  public CliPropertyGroup getMandatoryOptions() {

    return this.mandatoryOptions;
  }

  /**
   * @return the {@link CliPropertyGroup} of optional (not {@link CliProperty#isMandatory() mandatory})
   *         {@link CliProperty#isOption() options}.
   */
  public CliPropertyGroup getAdditionalOptions() {

    return this.additionalOptions;
  }

  /**
   * @return the {@link CliPropertyGroup} of parameters (not {@link CliProperty#isOption() options}).
   */
  public CliPropertyGroup getParameters() {

    return this.parameters;
  }

  /**
   * Clears this {@link CliProperties}.
   */
  public void clear() {

    this.mandatoryOptions.clear();
    this.additionalOptions.clear();
    this.parameters.clear();
    this.propertyMap.clear();
  }

}
