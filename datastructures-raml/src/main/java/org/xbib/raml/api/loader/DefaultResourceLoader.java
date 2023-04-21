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

import java.io.InputStream;
import java.net.URI;

public class DefaultResourceLoader implements ResourceLoaderExtended {

    private final ResourceLoader resourceLoader;

    public DefaultResourceLoader() {
        resourceLoader = CompositeResourceLoader.compose(
                new UrlResourceLoader(),
                new RamlUrlResourceLoader(),
                new ClassPathResourceLoader(),
                new FileResourceLoader("."));
    }

    @Override
    public InputStream fetchResource(String resourceName, ResourceUriCallback callback) {
        if (resourceLoader instanceof ResourceLoaderExtended) {
            return ((ResourceLoaderExtended) resourceLoader).fetchResource(resourceName, callback);
        } else {
            return resourceLoader.fetchResource(resourceName);
        }
    }

    @Override
    public InputStream fetchResource(String resourceName) {
        return fetchResource(resourceName, null);
    }

    @Override
    public URI getUriCallBackParam() {
        if (resourceLoader != null && resourceLoader instanceof ResourceLoaderExtended) {
            return ((ResourceLoaderExtended) resourceLoader).getUriCallBackParam();
        }
        return null;
    }
}
