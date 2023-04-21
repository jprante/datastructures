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
package org.xbib.raml.yagi.framework.nodes.snakeyaml;


import org.xbib.raml.api.loader.ResourceLoader;
import org.xbib.raml.api.loader.ResourceLoaderFactory;
import org.xbib.raml.yagi.framework.nodes.AbstractPosition;
import org.yaml.snakeyaml.error.Mark;

public class SYPosition extends AbstractPosition {

    private final Mark mark;
    private final ResourceLoaderFactory resourceLoaderFactory;
    private final String resourcePath;
    private String includedResourceURI;

    public SYPosition(Mark mark, ResourceLoaderFactory resourceLoaderFactory, String resourcePath) {
        super(resourceLoaderFactory);
        this.mark = mark;
        this.resourceLoaderFactory = resourceLoaderFactory;
        this.resourcePath = resourcePath;
    }

    @Override
    public int getIndex() {
        return mark.getIndex();
    }

    @Override
    public int getLine() {
        return mark.getLine();
    }

    @Override
    public int getColumn() {
        return mark.getColumn();
    }


    @Override
    public String getPath() {
        return resourcePath;
    }

    @Override
    public String getIncludedResourceUri() {
        return includedResourceURI;
    }

    @Override
    public void setIncludedResourceUri(String includedResourceURI) {
        this.includedResourceURI = includedResourceURI;
    }


    public ResourceLoader createResourceLoader() {
        return resourceLoaderFactory.createResourceLoader(null);
    }
}
