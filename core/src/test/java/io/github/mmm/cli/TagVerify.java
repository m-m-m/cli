/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli;

import io.github.mmm.bean.Mandatory;
import io.github.mmm.bean.PropertyAlias;
import io.github.mmm.property.booleans.BooleanProperty;

/**
 * The tag verify command.
 */
public interface TagVerify extends GitAbstractTagWithName {

  /** @return {@code true} to verify. */
  @PropertyAlias({ "--verify", "-v" })
  @Mandatory
  BooleanProperty Verify();

}