package io.github.mmm.cli.container.impl;

/**
 * Simple counter for values from 0-9.
 *
 * @since 1.0.0
 */
class ValueIndex {

  private static final String[] INDEX_VALUES = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };

  private int i;

  public String get() {

    return INDEX_VALUES[this.i];
  }

  public void next() {

    if (this.i < 10) {
      this.i++;
    }
  }

  @Override
  public String toString() {

    return get();
  }

}
