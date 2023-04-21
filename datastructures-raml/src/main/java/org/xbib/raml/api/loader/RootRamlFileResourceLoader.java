/*
 * Copyright 2013 (c) MuleSoft, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
package org.xbib.raml.api.loader;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RootRamlFileResourceLoader implements ResourceLoaderExtended {

    private final Logger logger = Logger.getLogger(RootRamlFileResourceLoader.class.getName());
    private final File parentPath;
    private URI callbackParam;

    public RootRamlFileResourceLoader(File path) {
        this.parentPath = path;
    }

    @Override
    public InputStream fetchResource(String resourceName, ResourceUriCallback callback) {
        final File includedFile = new File(parentPath, resourceName.startsWith("/") ? resourceName.substring(1) : resourceName);
        logger.log(Level.FINE, "Looking for resource " + resourceName + " on directory " + parentPath);
        try {
            if (callback != null) {
                callbackParam = includedFile.toURI();
                callback.onResourceFound(callbackParam);
            }

            return new FileInputStream(includedFile);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public InputStream fetchResource(String resourceName) {
        return fetchResource(resourceName, null);
    }

    @Override
    public URI getUriCallBackParam() {
        return callbackParam;
    }
}
