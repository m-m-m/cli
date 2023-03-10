/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.mmm.base.exception.ApplicationException;
import io.github.mmm.base.exception.DuplicateObjectException;
import io.github.mmm.base.i18n.Localizable;
import io.github.mmm.cli.command.CliCommand;

/**
 * Test of {@link CliMain} with invalid {@link CliCommand}(s).
 */
public class CliMainInvalidTest extends Assertions {

  /** Test of {@link GenericTestProgram} with {@link CliCommandInvalidValueIndexStart}. */
  @Test
  public void testInvalidValueIndexStart() {

    Class<? extends CliCommand> commandInterface = CliCommandInvalidValueIndexStart.class;
    checkInvalidCommand(commandInterface,
        "Invalid value CliCommandInvalidValueIndexStart.Value() with index 1 but expected index 0 - please fix @PropertyAlias annotation.");
  }

  /** Test of {@link GenericTestProgram} with {@link CliCommandInvalidValueIndexGap}. */
  @Test
  public void testInvalidValueIndexGap() {

    Class<? extends CliCommand> commandInterface = CliCommandInvalidValueIndexGap.class;
    checkInvalidCommand(commandInterface,
        "Invalid value CliCommandInvalidValueIndexGap.Value2() with index 2 but expected index 1 - please fix @PropertyAlias annotation.");
  }

  /** Test of {@link GenericTestProgram} with {@link CliCommandInvalidValueIndexDuplicate}. */
  @Test
  public void testInvalidValueIndexDuplicate() {

    Class<? extends CliCommand> commandInterface = CliCommandInvalidValueIndexDuplicate.class;
    checkInvalidCommand(commandInterface,
        "Duplicate object for key '0': CliCommandInvalidValueIndexDuplicate.Value2 - already mapped to: CliCommandInvalidValueIndexDuplicate.Value1",
        DuplicateObjectException.class);
  }

  /** Test of {@link GenericTestProgram} with {@link CliCommandInvalidOptionDuplicate}. */
  @Test
  public void testInvalidOptionDuplicate() {

    Class<? extends CliCommand> commandInterface = CliCommandInvalidOptionDuplicate.class;
    checkInvalidCommand(commandInterface,
        "Duplicate object for key '-m': CliCommandInvalidOptionDuplicate.Mode - already mapped to: CliCommandInvalidOptionDuplicate.Keys",
        DuplicateObjectException.class);
  }

  private void checkInvalidCommand(Class<? extends CliCommand> commandInterface, String expectedErrorMessage) {

    checkInvalidCommand(commandInterface, expectedErrorMessage, IllegalStateException.class);
  }

  private void checkInvalidCommand(Class<? extends CliCommand> commandInterface, String expectedErrorMessage,
      Class<? extends Throwable> exception) {

    try {
      new GenericTestProgram(commandInterface);
      failBecauseExceptionWasNotThrown(exception);
    } catch (RuntimeException e) {
      assertThat(e).isInstanceOf(exception);
      if (e instanceof ApplicationException) {
        ApplicationException ae = (ApplicationException) e;
        Localizable nlsMessage = ae.getNlsMessage();
        assertThat(nlsMessage.getMessage()).isEqualTo(expectedErrorMessage);
      } else {
        assertThat(e).hasMessage(expectedErrorMessage);
      }
    }
  }

}
