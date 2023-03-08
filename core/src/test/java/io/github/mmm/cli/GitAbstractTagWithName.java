/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli;

import io.github.mmm.bean.AbstractInterface;
import io.github.mmm.bean.Mandatory;
import io.github.mmm.bean.PropertyAlias;
import io.github.mmm.property.string.StringProperty;

/**
 * The test command.
 */
@AbstractInterface
public interface GitAbstractTagWithName extends GitAbstractTag {

  /** @return the name of the tag to create/delete/view. */
  @Mandatory
  @PropertyAlias("1")
  StringProperty TagName();

}