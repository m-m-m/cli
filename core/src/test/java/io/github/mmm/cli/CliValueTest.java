/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link CliValue}.
 */
public class CliValueTest extends Assertions {

  /** Test of {@link CliValue}. */
  @Test
  public void testDefaults() {

    // given
    String value = "value";
    boolean endOptions = false;
    // when
    CliValue cliValue = new CliValue(value, endOptions);
    // then
    assertThat(cliValue.get()).isSameAs(value);
    assertThat(cliValue.getValue()).isSameAs(value);
    assertThat(cliValue.toString()).isEqualTo(value);
    assertThat(cliValue.isValue()).isTrue();
    assertThat(cliValue.isOption()).isFalse();
    assertThat(cliValue.isShortOption()).isFalse();
    assertThat(cliValue.isLongOption()).isFalse();
    assertThat(cliValue.isEndOptions()).isEqualTo(endOptions);
    assertThat(cliValue.getNext()).isNull();
    assertThat(cliValue.getOptionName()).isNull();
  }

  /** Test of {@link CliValue}. */
  @Test
  public void testEndOptions() {

    // given
    String value = "its magic";
    boolean endOptions = true;
    // when
    CliValue cliValue = new CliValue(value, endOptions);
    // then
    assertThat(cliValue.get()).isSameAs(value);
    assertThat(cliValue.getValue()).isSameAs(value);
    assertThat(cliValue.toString()).isEqualTo(value);
    assertThat(cliValue.isValue()).isTrue();
    assertThat(cliValue.isOption()).isFalse();
    assertThat(cliValue.isShortOption()).isFalse();
    assertThat(cliValue.isLongOption()).isFalse();
    assertThat(cliValue.isEndOptions()).isEqualTo(endOptions);
  }

}
