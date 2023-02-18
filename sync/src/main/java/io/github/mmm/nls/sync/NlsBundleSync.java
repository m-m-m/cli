/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.nls.sync;

import io.github.mmm.nls.NlsBundle;
import io.github.mmm.nls.NlsMessage;

/**
 * {@link NlsBundle} for {@link NlsSynchronizer}.
 *
 * @since 1.0.0
 */
@SuppressWarnings("javadoc")
public final class NlsBundleSync extends NlsBundle {

  public static final NlsBundleSync INSTANCE = new NlsBundleSync();

  private NlsBundleSync() {

    super();
  }

  public NlsMessage msgHelp() {

    return create("msgHelp",
        "Synchronize localized resource-bundle properties from NlsBundle class(es) for the given locales." //
            + "All messages defined in the bundle class missing in the resource-bundle properties will be added with a TODO-marker followed by the original untranslated message." //
            + "Existing properties that have already been translated before will be kept unchainged." //
            + "Missing resource-bundle properties files will be created with all messages. After synchronization the translators can resolve all the TODOs and your localization is guaranteed to be in sync again.\n" //
            + "E.g. for foo.bar.NlsBundleMyExample and the locale zh_TW the file foo/bar/NlsBundleMyExample_zh_TW.properties will be created or updated in the configured path.");
  }

  public NlsMessage optLocales() {

    return create("optLocales", "The locales to synchronize (e.g. 'de en en_US en_GB fr es ja zh zh_TW').");
  }

  public NlsMessage optPath() {

    return create("optPath", "Path to base-directory where resource-bundle property files are written to.");
  }

  public NlsMessage optBundles() {

    return create("optBundles", "NlsBundle(s) to synchronize.");
  }

}
