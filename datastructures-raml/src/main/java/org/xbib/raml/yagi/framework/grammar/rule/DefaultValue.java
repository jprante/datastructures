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
package org.xbib.raml.yagi.framework.grammar.rule;

import org.xbib.raml.yagi.framework.nodes.Node;

/**
 * Generates a default value when needed.
 */
public interface DefaultValue {

    /**
     * Generates a default value node based on the parent context.
     *
     * @param parent The parent node of the default value.
     * @return The default value node or null when it cannot be generated in a fragment file
     */

    Node getDefaultValue(Node parent);
}
