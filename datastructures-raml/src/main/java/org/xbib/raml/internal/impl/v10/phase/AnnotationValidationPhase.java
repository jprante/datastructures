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
package org.xbib.raml.internal.impl.v10.phase;

import java.util.List;
import org.xbib.raml.api.loader.ResourceLoader;
import org.xbib.raml.api.model.v10.declarations.AnnotationTarget;
import org.xbib.raml.internal.impl.commons.nodes.AnnotationNode;
import org.xbib.raml.internal.impl.commons.nodes.AnnotationTypeNode;
import org.xbib.raml.internal.impl.commons.nodes.TypeDeclarationNode;
import org.xbib.raml.internal.impl.commons.rule.RamlErrorNodeFactory;
import org.xbib.raml.internal.impl.v10.type.TypeToRuleVisitor;
import org.xbib.raml.yagi.framework.grammar.rule.ErrorNodeFactory;
import org.xbib.raml.yagi.framework.grammar.rule.Rule;
import org.xbib.raml.yagi.framework.nodes.Node;
import org.xbib.raml.yagi.framework.phase.Phase;

public class AnnotationValidationPhase implements Phase {

    private final ResourceLoader resourceLoader;

    public AnnotationValidationPhase(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public Node apply(Node tree) {
        final List<AnnotationNode> annotations = tree.findDescendantsWith(AnnotationNode.class);
        for (AnnotationNode annotation : annotations) {
            final String annotationName = annotation.getName();
            final AnnotationTypeNode annotationTypeNode = annotation.getAnnotationTypeNode();
            if (annotationTypeNode == null) {
                annotation.replaceWith(ErrorNodeFactory.createMissingAnnotationType(annotationName));
            } else {
                final Node annotationValue = annotation.getValue();

                // check annotation target
                List<AnnotationTarget> allowedTargets = annotationTypeNode.getAllowedTargets();
                addTargetAliases(allowedTargets);
                AnnotationTarget target = annotation.getTarget();
                if (target != null && !allowedTargets.isEmpty() && !allowedTargets.contains(target)) {
                    annotationValue.replaceWith(RamlErrorNodeFactory.createInvalidAnnotationTarget(allowedTargets, target));
                }

                // check annotation type
                final TypeDeclarationNode typeNode = annotationTypeNode.getDeclaredType();
                final Rule annotationRule = typeNode.getResolvedType().visit(new TypeToRuleVisitor(resourceLoader));
                annotationValue.replaceWith(annotationRule.apply(annotationValue));
            }
        }
        return tree;
    }

    private void addTargetAliases(List<AnnotationTarget> allowedTargets) {
        if (allowedTargets.contains(AnnotationTarget.TypeDeclaration)) {
            if (!allowedTargets.contains(AnnotationTarget.RequestBody)) {
                allowedTargets.add(AnnotationTarget.RequestBody);
            }
            if (!allowedTargets.contains(AnnotationTarget.ResponseBody)) {
                allowedTargets.add(AnnotationTarget.ResponseBody);
            }
        }
    }
}
