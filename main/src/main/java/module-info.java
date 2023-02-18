/*
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
/**
 * Provides a powerful extension for {@code mmm-cli} that integrates with {@code mmm-nls}.<br>
 * <h2>NLS CLI</h2><br>
 * With this module you can build powerful command-line-interfaces (CLI) easily. While {@code mmm-cli} is a minimal
 * module to parse command-line arguments this module adds a lot of build-in features on top of it:
 * <ul>
 * <li>help support (print usage on --help or -h)</li>
 * <li>version support (print program version on --version or -v)</li>
 * <li>native-language-support via {@code mmm-nls}</li>
 * <li>localizations for arbitrary languages via {@code mmm-n10n-all} (needs to be added as dependency)</li>
 * <li>automatic data-binding of command-line arguments to {@link CliCommand} via {@link WritableProperty
 * properties}.</li>
 * <li>automatic matching of {@link io.github.mmm.nls.cli.CliCommand}s</li>
 * </ul>
 * <h3>Example:</h3>
 *
 */
module io.github.mmm.nls.cli {

  requires transitive io.github.mmm.nls;

  requires transitive io.github.mmm.cli;

  requires transitive io.github.mmm.bean.factory;

  exports io.github.mmm.nls.cli;

  exports io.github.mmm.nls.cli.exception;

  exports io.github.mmm.nls.cli.property;

}
