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
package org.xbib.raml.internal.utils.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xbib.raml.api.loader.ResourceLoader;
import org.xbib.raml.internal.utils.IOUtils;

public class XsdResourceResolver implements LSResourceResolver {

    private final String resourcePath;
    private final ResourceLoader resourceLoader;

    public XsdResourceResolver(ResourceLoader resourceLoader, String resourcePath) {
        this.resourceLoader = resourceLoader;
        this.resourcePath = resourcePath;
    }

    @Override
    public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
        if (systemId == null || systemId.startsWith("http://") || systemId.startsWith("https://") || systemId.startsWith("file:")) {
            // delegate resource resolution to xml parser
            return null;
        }

        if (Paths.get(systemId).normalize().isAbsolute()) {

            return null;
        }

        InputStream inputStream = resourceLoader.fetchResource(systemId);
        if (inputStream == null) {
            // delegate resource resolution to xml parser
            return null;
        }


        byte[] content;
        try {
            content = IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new LSInputImpl(publicId, systemId, baseURI, new ByteArrayInputStream(content), StandardCharsets.UTF_8);
    }
}
