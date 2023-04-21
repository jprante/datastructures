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
package org.xbib.raml.yagi.framework.nodes;


import org.xbib.raml.api.loader.ResourceLoader;
import org.xbib.raml.api.loader.ResourceLoaderFactories;
import org.xbib.raml.api.loader.ResourceLoaderFactory;

public class DefaultPosition extends AbstractPosition {

    public static final String ARTIFICIAL_NODE = "[artificial node]";
    private final int index;
    private final int line;
    private final int column;
    private final String resource;
    private final ResourceLoaderFactory resourceLoader;
    private String includedResourceUri;

    public DefaultPosition(int index, int line, int column, String resource, ResourceLoaderFactory resourceLoaderFactory) {
        super(resourceLoaderFactory);
        this.index = index;
        this.line = line;
        this.column = column;
        this.resource = resource;
        this.resourceLoader = resourceLoaderFactory;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public int getLine() {
        return line;
    }

    @Override
    public int getColumn() {
        return column;
    }


    @Override
    public String getPath() {
        return resource;
    }

    public String getIncludedResourceUri() {
        return includedResourceUri;
    }

    @Override
    public void setIncludedResourceUri(String includedResourceURI) {
        this.includedResourceUri = includedResourceURI;
    }


    @Override
    public ResourceLoader createResourceLoader() {
        return resourceLoader.createResourceLoader(null);
    }

    public static DefaultPosition emptyPosition() {
        return new DefaultPosition(UNKNOWN, UNKNOWN, UNKNOWN, ARTIFICIAL_NODE, ResourceLoaderFactories.nullFactory());
    }

    public static boolean isDefaultNode(Node node) {
        return node.getStartPosition().getLine() == Position.UNKNOWN
                && ARTIFICIAL_NODE.equals(node.getStartPosition().getPath())
                && node.getEndPosition().getLine() == Position.UNKNOWN
                && ARTIFICIAL_NODE.equals(node.getEndPosition().getPath());
    }


}
