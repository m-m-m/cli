/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli.property;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Group of {@link CliPropertyInfo}s.
 *
 * @since 1.0.0
 */
public class CliPropertyGroup {

  private final List<CliPropertyInfo> propeties;

  private int maxLength;

  private boolean sorted;

  /**
   * The constructor.
   */
  public CliPropertyGroup() {

    super();
    this.propeties = new ArrayList<>();
    this.maxLength = 0;
  }

  /**
   * @param propertyInfo the {@link CliPropertyInfo} to add.
   */
  public void add(CliPropertyInfo propertyInfo) {

    assert (!this.sorted);
    this.propeties.add(propertyInfo);
    int len = propertyInfo.getSyntax().length();
    if (this.maxLength < len) {
      this.maxLength = len;
    }
  }

  /**
   * @return the {@link List} of {@link CliPropertyInfo}s.
   */
  public List<CliPropertyInfo> getPropeties() {

    if (!this.sorted) {
      Collections.sort(this.propeties);
      this.sorted = true;
    }
    return this.propeties;
  }

  /**
   * @return the maximum {@link String#length() length} of the {@link CliPropertyInfo#getSyntax() syntax}.
   */
  public int getMaxLength() {

    return this.maxLength;
  }

  void collectMandatoryProperties(Set<String> set) {

    for (CliPropertyInfo property : this.propeties) {
      if (property.isMandatory()) {
        set.add(property.getPropertyName());
      }
    }
  }

}
