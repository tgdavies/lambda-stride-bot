package org.kablambda.framework;

import java.util.List;

import org.kablambda.framework.modules.Module;

/**
 * Implement this interface to describe your app
 */
public interface App {
    String getName();
    List<Module> getModules();
}
