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


import org.xbib.raml.api.loader.ResourceLoaderFactory;

public abstract class AbstractPosition implements Position {

    private final ResourceLoaderFactory resourceLoaderFactory;

    public AbstractPosition(ResourceLoaderFactory resourceLoaderFactory) {
        this.resourceLoaderFactory = resourceLoaderFactory;
    }


    @Override
    public Position rightShift(int offset) {
        return new DefaultPosition(getIndex() + offset, getLine(), getColumn() + offset, getPath(), resourceLoaderFactory);
    }


    @Override
    public Position leftShift(int offset) {
        return new DefaultPosition(getIndex() - offset, getLine(), getColumn() - offset, getPath(), resourceLoaderFactory);
    }

    @Override
    public String toString() {
        return String.format("[line=%d, col=%d]", getLine() + 1, getColumn() + 1);
    }
}
