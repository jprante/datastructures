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
package org.xbib.raml.internal.impl.commons.model.type;

import java.util.List;
import org.xbib.raml.internal.impl.v10.type.StringResolvedType;
import org.xbib.raml.yagi.framework.nodes.KeyValueNode;

public class StringTypeDeclaration extends TypeDeclaration<StringResolvedType> {

    public StringTypeDeclaration(KeyValueNode node, StringResolvedType resolvedType) {
        super(node, resolvedType);
    }


    public String pattern() {
        return getResolvedType().getPattern();
    }


    public List<String> enumValues() {
        return getResolvedType().getEnums();
    }


    public Integer minLength() {
        return getResolvedType().getMinLength();
    }


    public Integer maxLength() {
        return getResolvedType().getMaxLength();
    }
}