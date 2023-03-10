/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli.container.impl;

import java.util.ArrayList;
import java.util.List;

/**
 * Group of {@link CliPropertyContainerImpl}s.
 *
 * @since 1.0.0
 */
public class CliPropertyGroup {

  private final CliPropertyType type;

  private final List<CliPropertyContainerImpl> properties;

  private int maxLength;

  /**
   * The constructor.
   *
   * @param type the {@link CliPropertyType}.
   */
  public CliPropertyGroup(CliPropertyType type) {

    super();
    this.type = type;
    this.properties = new ArrayList<>();
    this.maxLength = 0;
  }

  /**
   * @param property the {@link CliPropertyContainerImpl} to add.
   */
  public void add(CliPropertyContainerImpl property) {

    this.properties.add(property);
    int len = property.getSyntax().length();
    if (this.maxLength < len) {
      this.maxLength = len;
    }
  }

  /**
   * @return the {@link CliPropertyType}.
   */
  public CliPropertyType getType() {

    return this.type;
  }

  /**
   * @return the {@link List} of {@link CliPropertyContainerImpl}s.
   */
  public List<CliPropertyContainerImpl> getProperties() {

    return this.properties;
  }

  /**
   * @return the maximum {@link String#length() length} of the {@link CliPropertyContainerImpl#getSyntax() syntax}.
   */
  public int getMaxLength() {

    return this.maxLength;
  }

  @Override
  public String toString() {

    StringBuilder sb = new StringBuilder();
    String nl = "";
    for (CliPropertyContainerImpl property : this.properties) {
      sb.append(nl);
      sb.append(property.toString());
      nl = "\n";
    }
    return sb.toString();
  }

}
