/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli.arg;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link CliLongOption}.
 */
public class CliLongOptionTest extends Assertions {

  /** Test of {@link CliLongOption}. */
  @Test
  public void testDefaults() {

    // given
    String option = "--long-option";
    boolean assignment = false;
    // when
    CliLongOption cliOption = new CliLongOption(option, assignment);
    // then
    assertThat(cliOption.get()).isSameAs(option);
    assertThat(cliOption.getValue()).isNull();
    assertThat(cliOption.toString()).isEqualTo(option);
    assertThat(cliOption.isValue()).isFalse();
    assertThat(cliOption.isOption()).isTrue();
    assertThat(cliOption.isShortOption()).isFalse();
    assertThat(cliOption.isLongOption()).isTrue();
    assertThat(cliOption.isEndOptions()).isFalse();
    assertThat(cliOption.isAssignment()).isEqualTo(assignment);
    assertThat(cliOption.getOptionName()).isEqualTo("long-option");
    assertThat(cliOption.getNext()).isNull();
  }

  /** Test of {@link CliLongOption} with invalid value. */
  @Test
  public void testInvalid() {

    for (String invalid : new String[] { "", "x", "xy", "-x", "--" }) {
      assertThrows(IllegalArgumentException.class, () -> {
        new CliLongOption(invalid, false);
      });
    }
  }

}
