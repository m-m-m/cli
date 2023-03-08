/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli;

import io.github.mmm.bean.PropertyAlias;
import io.github.mmm.property.booleans.BooleanProperty;
import io.github.mmm.property.container.list.ListProperty;
import io.github.mmm.property.string.StringProperty;

/**
 * The tag create command.
 */
public interface TagCreate extends GitAbstractTagWithName {

  /** @return {@code true} to create annotated tag. */
  @PropertyAlias({ "--annotate", "-a" })
  BooleanProperty Annotate();

  /** @return {@code true} to create signed tag. */
  @PropertyAlias({ "--sign", "-s" })
  BooleanProperty Sign();

  /** @return {@code true} to override {@code tag.gpgSign}. */
  @PropertyAlias({ "--no-sign" })
  BooleanProperty NoSign();

  /** @return the GPG key for signing. */
  @PropertyAlias({ "--local-user", "-u" })
  StringProperty SignKey();

  /** @return {@code true} to verify. */
  @PropertyAlias({ "--force", "-f" })
  BooleanProperty Force();

  /** @return {@code true} to edit message. */
  @PropertyAlias({ "--edit", "-e" })
  BooleanProperty Edit();

  /** @return the message. */
  @PropertyAlias({ "--message", "-m" })
  ListProperty<String> Message();

  /** @return the GPG key for signing. */
  @PropertyAlias({ "--file", "-F" })
  StringProperty File();

  /** @return the name of the tag to create/delete/view. */
  @PropertyAlias("2")
  StringProperty CommitOrObject();

}