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

public interface ResourceLoader {

    /**
     * Returns an input stream for reading the specified resource.
     *
     * @param resourceName the resource to try to fetch
     * @return An input stream for reading the resource, or <tt>null</tt>
     * if the resource could not be found
     */
    InputStream fetchResource(String resourceName);

}
