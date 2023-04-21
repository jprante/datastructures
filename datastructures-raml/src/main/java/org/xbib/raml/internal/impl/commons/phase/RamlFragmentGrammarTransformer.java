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
package org.xbib.raml.internal.impl.commons.phase;

import org.xbib.raml.internal.impl.commons.RamlHeader;
import org.xbib.raml.yagi.framework.grammar.rule.Rule;
import org.xbib.raml.yagi.framework.nodes.Node;
import org.xbib.raml.yagi.framework.phase.Transformer;
import static org.xbib.raml.internal.utils.RamlNodeUtils.isErrorResult;

public class RamlFragmentGrammarTransformer implements Transformer {
    @Override
    public boolean matches(Node node) {
        return node instanceof RamlTypedFragment && node.getParent() != null && !isErrorResult(node);
    }

    @Override
    public Node transform(Node node) {
        final RamlTypedFragment ramlTypedFragmentNode = (RamlTypedFragment) node;
        final Rule rule = RamlHeader.getFragmentUsesAllowedRule(ramlTypedFragmentNode.getFragment());
        return rule.apply(node);
    }
}
