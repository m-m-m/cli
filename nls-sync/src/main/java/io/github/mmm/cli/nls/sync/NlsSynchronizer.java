/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli.nls.sync;

import io.github.mmm.cli.CliMain;
import io.github.mmm.nls.NlsBundle;

/**
 * {@link CliMain Main program} (CLI) to synchronize resource bundles for localization from {@link NlsBundle} classes.
 */
public class NlsSynchronizer extends CliMain {

  @Override
  protected void addCommands() {

    super.addCommands();
    add(Synchronizer.class);
  }

  /**
   * @param args the CLI arguments.
   */
  public static void main(String[] args) {

    new NlsSynchronizer().runAndExit(args);
  }

}
