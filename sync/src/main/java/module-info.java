/*
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
/**
 * Provides a command-line-interface to synchronize resource bundles for localization.<br>
 * <h2>NLS Sync</h2><br>
 */
module io.github.mmm.nls.sync {

  requires transitive io.github.mmm.nls.cli;

  // TODO JPMS bug (in Eclipse?)
  requires io.github.mmm.property;

  exports io.github.mmm.nls.sync;

}
