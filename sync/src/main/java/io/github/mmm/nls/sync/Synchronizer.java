/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.nls.sync;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import io.github.mmm.base.i18n.LocaleHelper;
import io.github.mmm.base.properties.SortedProperties;
import io.github.mmm.bean.Mandatory;
import io.github.mmm.bean.PropertyAlias;
import io.github.mmm.cli.io.CliConsole;
import io.github.mmm.nls.NlsBundle;
import io.github.mmm.nls.NlsMessage;
import io.github.mmm.nls.cli.CliCommand;
import io.github.mmm.nls.cli.NlsMain;
import io.github.mmm.nls.descriptor.NlsMessageDescriptor;
import io.github.mmm.property.container.list.ListProperty;
import io.github.mmm.property.string.StringProperty;

/**
 * Command to do the actual sync.
 */
public interface Synchronizer extends CliCommand {

  default StringProperty Path() {

    StringProperty path = new StringProperty("Path");
    path.set("src/main/resources"); // default value
    return path;
  }

  @Mandatory
  @PropertyAlias({ "--locale", "-l" })
  ListProperty<Locale> Locales();

  @Mandatory
  @PropertyAlias({ "--bundle", "-b" })
  ListProperty<String> BundleClasses();

  @SuppressWarnings("unchecked")
  @Override
  default int run(NlsMain main) {

    for (String bundleClassName : BundleClasses().get()) {
      Class<? extends NlsBundle> bundleClass;
      try {
        Class<?> clazz = Class.forName(bundleClassName);
        if (!NlsBundle.class.isAssignableFrom(clazz)) {
          throw new ClassCastException(bundleClassName);
        }
        bundleClass = (Class<? extends NlsBundle>) clazz;
      } catch (Exception e) {
        throw new IllegalArgumentException("Invalid BundleClass " + bundleClassName, e);
      }
      NlsBundle bundle;
      try {
        bundle = bundleClass.getDeclaredConstructor().newInstance();
      } catch (Exception e) {
        throw new IllegalArgumentException(bundleClass.getName(), e);
      }
      CliConsole console = main.console();
      for (Method method : bundleClass.getMethods()) {
        Class<?> returnType = method.getReturnType();
        List<NlsMessage> messages = new ArrayList<>();
        if (returnType == NlsMessage.class) {
          int parameterCount = method.getParameterCount();
          try {
            NlsMessage message = (NlsMessage) method.invoke(bundle, new Object[parameterCount]);
            messages.add(message);
          } catch (Exception e) {
            String error;
            if (e instanceof InvocationTargetException) {
              error = e.getCause().getMessage();
            } else {
              error = e.getMessage();
            }
            console.warning().log("Failed to invoke method ", method, ": ", error);
          }
        }
        syncBundles(bundle, messages, console);
      }
    }
    return 0;
  }

  private void syncBundles(NlsBundle bundle, List<NlsMessage> messages, CliConsole console) {

    String bundleName = bundle.getBundleName();
    String bundlePath = bundleName.replace('.', '/');
    for (Locale locale : Locales().get()) {
      String localeInfix = LocaleHelper.toInfix(locale);
      Path propertiesPath = Paths.get(Path().get(), bundlePath + localeInfix + ".properties");
      Properties properties = new SortedProperties();
      boolean write = false;
      if (Files.exists(propertiesPath)) {
        try (BufferedReader reader = Files.newBufferedReader(propertiesPath)) {
          properties.load(reader);
        } catch (Exception e) {
          console.warning().log("Failed to read resource bundle from ", propertiesPath, ": ", e.getMessage());
        }
      } else {
        write = true;
      }
      boolean update = updateProperties(properties, locale.toString(), messages);
      if (update) {
        write = true;
      }
      if (write) {
        try (BufferedWriter writer = Files.newBufferedWriter(propertiesPath)) {
          properties.store(writer, null);
        } catch (Exception e) {

        }
      }
    }
  }

  private boolean updateProperties(Properties properties, String locale, List<NlsMessage> messages) {

    boolean modified = false;
    StringBuilder sb = new StringBuilder();
    for (NlsMessage message : messages) {
      NlsMessageDescriptor descriptor = message.getDescriptor();
      String key = descriptor.getMessageKey();
      if (!properties.containsKey(key)) {
        sb.setLength(0);
        if (locale.length() > 0) {
          sb.append("TODO(");
          sb.append(locale);
          sb.append("):");
        }
        // escape newlines for properties-syntax
        sb.append(message.getInternationalizedMessage().replace("\r", "").replace("\n", "\\n"));
        properties.put(key, sb.toString());
      }
    }
    return modified;
  }

}