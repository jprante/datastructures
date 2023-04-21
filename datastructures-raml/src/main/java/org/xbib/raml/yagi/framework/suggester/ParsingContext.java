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
package org.xbib.raml.yagi.framework.suggester;

public class ParsingContext {

    private final ParsingContextType contextType;
    private final String content;
    private final int location;

    public ParsingContext(ParsingContextType contextType, String content, int location) {
        this.contextType = contextType;
        this.content = content;
        this.location = location;
    }

    public String getContent() {
        return content;
    }

    public ParsingContextType getContextType() {
        return contextType;
    }

    public int getLocation() {
        return location;
    }
}
