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
import java.io.InputStream;

/**
 * Created. There, you have it.
 */
public class ResourceLoaderFactories {

    // this continously returns the same resource loader.
    public static ResourceLoaderFactory identityFactory(final ResourceLoader loader) {

        return new ResourceLoaderFactory() {
            @Override
            public ResourceLoader createResourceLoader(String parent) {
                return loader;
            }
        };
    }

    // this is used nowhere but in the empty positions. these loaders shouldn't open files.
    public static ResourceLoaderFactory nullFactory() {
        return new ResourceLoaderFactory() {
            @Override
            public ResourceLoader createResourceLoader(String parent) {
                return new ResourceLoader() {
                    @Override
                    public InputStream fetchResource(String resourceName) {
                        return null;
                    }
                };
            }
        };
    }

    public static ResourceLoaderFactory defaultResourceLoaderFactory() {

        return new ResourceLoaderFactory() {
            @Override
            public ResourceLoader createResourceLoader(String parent) {
                return CompositeResourceLoader.compose(
                        new DefaultResourceLoader(),
                        new RootRamlFileResourceLoader(new File(parent)),
                        new RootRamlUrlResourceLoader(parent)
                );
            }
        };
    }
}
