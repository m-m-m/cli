/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli;

import io.github.mmm.bean.Mandatory;
import io.github.mmm.bean.PropertyAlias;
import io.github.mmm.property.booleans.BooleanProperty;
import io.github.mmm.property.container.list.ListProperty;
import io.github.mmm.property.enumeration.EnumProperty;
import io.github.mmm.property.string.StringProperty;

/**
 * The tag list command.
 */
public interface TagList extends GitAbstractTag {

  /** @return the pattern for filter tags to list. */
  @PropertyAlias({ "--list", "-l" })
  @Mandatory
  BooleanProperty List();

  /** @return the key to sort by. */
  @PropertyAlias({ "--sort" })
  ListProperty<String> Sort();

  /** @return {@code true} to sort and filter tags case insensitive. */
  @PropertyAlias({ "--ignore-case", "-i" })
  BooleanProperty IgnoreCase();

  /** @return the format pattern. */
  @PropertyAlias({ "--format" })
  ListProperty<String> Format();

  /** @return the columns to display. */
  @PropertyAlias({ "--column" })
  StringProperty Column();

  /** @return the color. */
  @PropertyAlias({ "--color" })
  EnumProperty<ColorWhen> Color();

}