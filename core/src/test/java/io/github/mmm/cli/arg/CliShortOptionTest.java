/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli.arg;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link CliShortOption}.
 */
public class CliShortOptionTest extends Assertions {

  /** Test of {@link CliShortOption}. */
  @Test
  public void testDefaults() {

    // given
    String option = "-x";
    boolean assignment = false;
    // when
    CliShortOption cliOption = new CliShortOption(option, assignment);
    // then
    assertThat(cliOption.get()).isSameAs(option);
    assertThat(cliOption.getValue()).isNull();
    assertThat(cliOption.toString()).isEqualTo(option);
    assertThat(cliOption.isValue()).isFalse();
    assertThat(cliOption.isOption()).isTrue();
    assertThat(cliOption.isShortOption()).isTrue();
    assertThat(cliOption.isLongOption()).isFalse();
    assertThat(cliOption.isEndOptions()).isFalse();
    assertThat(cliOption.isAssignment()).isEqualTo(assignment);
    assertThat(cliOption.getOptionName()).isEqualTo("x");
    assertThat(cliOption.getNext()).isNull();
  }

  /** Test of {@link CliShortOption} with invalid value. */
  @Test
  public void testInvalid() {

    for (String invalid : new String[] { "", "x", "--", "--x", "-xy" }) {
      assertThrows(IllegalArgumentException.class, () -> {
        new CliShortOption(invalid, false);
      });
    }
  }

}
