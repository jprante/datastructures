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

public class FileResourceLoader implements ResourceLoaderExtended {

    private final File parentPath;
    private URI callbackParam;

    public FileResourceLoader(String path) {
        this(new File(path));
    }

    public FileResourceLoader(File path) {
        this.parentPath = path;
    }

    @Override
    public InputStream fetchResource(String resourceName, ResourceUriCallback callback) {
        File includedFile = new File(resourceName);
        if (!includedFile.isAbsolute()) {
            includedFile = new File(parentPath, resourceName);
        }
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
