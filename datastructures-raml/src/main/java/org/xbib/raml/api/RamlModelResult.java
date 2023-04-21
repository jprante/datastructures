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
package org.xbib.raml.api;

import java.util.ArrayList;
import java.util.List;
import org.xbib.raml.api.model.common.ValidationResult;
import org.xbib.raml.api.model.v10.RamlFragment;
import org.xbib.raml.api.model.v10.api.Api;
import org.xbib.raml.api.model.v10.api.DocumentationItem;
import org.xbib.raml.api.model.v10.api.Library;
import org.xbib.raml.api.model.v10.datamodel.ExampleSpec;
import org.xbib.raml.api.model.v10.datamodel.TypeDeclaration;
import org.xbib.raml.api.model.v10.methods.Trait;
import org.xbib.raml.api.model.v10.resources.ResourceType;
import org.xbib.raml.api.model.v10.security.SecurityScheme;

/**
 * Represents the result of parsing a top level RAML descriptor or fragment.
 * <p>
 * If there are no parsing errors and the parsed RAML was a top level descriptor,
 * the <code>Api</code> model matching the RAML version is available.
 * If the parsed raml is 1.0 fragment and there are no errors, the corresponding
 * fragment instance is available.
 * <p>
 * If there are parsing errors, the list of errors is available.
 */
public class RamlModelResult {

    private List<ValidationResult> validationResults = new ArrayList<>();
    private Api apiV10;
    private Library library;
    private TypeDeclaration typeDeclaration;
    private SecurityScheme securityScheme;
    private Trait trait;
    private ResourceType resourceType;
    private ExampleSpec exampleSpec;
    private DocumentationItem documentationItem;

    RamlModelResult(List<ValidationResult> validationResults) {
        if (validationResults == null || validationResults.isEmpty()) {
            throw new IllegalArgumentException("validationResults cannot be null or emtpy");
        }
        this.validationResults = validationResults;
    }

    RamlModelResult(Api apiV10) {
        if (apiV10 == null) {
            throw new IllegalArgumentException("apiV10 cannot be null");
        }
        this.apiV10 = apiV10;
    }

    RamlModelResult(Library library) {
        if (library == null) {
            throw new IllegalArgumentException("library cannot be null");
        }
        this.library = library;
    }

    public RamlModelResult(TypeDeclaration typeDeclaration) {
        if (typeDeclaration == null) {
            throw new IllegalStateException("typeDeclaration cannot be null");
        }
        this.typeDeclaration = typeDeclaration;
    }

    public RamlModelResult(SecurityScheme securityScheme) {
        if (securityScheme == null) {
            throw new IllegalStateException("securityScheme cannot be null");
        }
        this.securityScheme = securityScheme;
    }

    public RamlModelResult(DocumentationItem documentationItem) {
        if (documentationItem == null) {
            throw new IllegalStateException("documentationItem cannot be null");
        }
        this.documentationItem = documentationItem;
    }

    public RamlModelResult(Trait trait) {
        if (trait == null) {
            throw new IllegalStateException("trait cannot be null");
        }
        this.trait = trait;
    }


    public RamlModelResult(ResourceType resourceType) {
        if (resourceType == null) {
            throw new IllegalStateException("resourceType cannot be null");
        }
        this.resourceType = resourceType;
    }


    public RamlModelResult(ExampleSpec exampleSpec) {
        if (exampleSpec == null) {
            throw new IllegalStateException("exampleSpec cannot be null");
        }
        this.exampleSpec = exampleSpec;
    }


    /**
     * @return true if any parsing error occurred
     */
    public boolean hasErrors() {
        return !validationResults.isEmpty();
    }

    /**
     * @return the list of validation results if there were parsing errors
     * or an empty list if there were no parsing errors
     */
    public List<ValidationResult> getValidationResults() {
        return validationResults;
    }


    /**
     * @return the RAML Api v1.0 parsed without errors
     * or null if there were errors or the RAML version is not 1.0 or is not a top level RAML
     */
    public Api getApiV10() {
        return apiV10;
    }

    /**
     * @return the RAML Library v1.0 parsed without errors
     * or null if there were errors or the RAML is not a Library fragment
     */
    public Library getLibrary() {
        return library;
    }


    /**
     * @return the RAML Data Type v1.0 parsed without errors
     * or null if there were errors or the RAML is not a Data Type fragment
     */
    public TypeDeclaration getTypeDeclaration() {
        return typeDeclaration;
    }


    /**
     * @return the RAML Security Scheme v1.0 parsed without errors
     * or null if there were errors or the RAML is not a Security Scheme fragment
     */
    public SecurityScheme getSecurityScheme() {
        return securityScheme;
    }


    /**
     * @return the RAML Trait v1.0 parsed without errors
     * or null if there were errors or the RAML is not a Trait fragment
     */
    public Trait getTrait() {
        return trait;
    }


    /**
     * @return the RAML ResourceType v1.0 parsed without errors
     * or null if there were errors or the RAML is not a ResourceType fragment
     */
    public ResourceType getResourceType() {
        return resourceType;
    }


    /**
     * @return the RAML NamedExample v1.0 parsed without errors
     * or null if there were errors or the RAML is not a NamedExample fragment
     */
    public ExampleSpec getExampleSpec() {
        return exampleSpec;
    }

    /**
     * @return the RAML DocumentationItem v1.0 parsed without errors
     * or null if there were errors or the RAML is not a DocumentationItem fragment
     */
    public DocumentationItem getDocumentationItem() {
        return documentationItem;
    }


    /**
     * @return the RAML 1.0 fragment identifier or <code>null</code>
     * if the RAML has errors or is version 0.8
     */
    public RamlFragment getFragment() {
        if (hasErrors()) {
            return null;
        }
        if (getApiV10() != null) {
            return RamlFragment.Default;
        }
        if (getLibrary() != null) {
            return RamlFragment.Library;
        }
        if (getTypeDeclaration() != null) {
            return RamlFragment.DataType;
        }
        if (getSecurityScheme() != null) {
            return RamlFragment.SecurityScheme;
        }
        if (getTrait() != null) {
            return RamlFragment.Trait;
        }
        if (getResourceType() != null) {
            return RamlFragment.ResourceType;
        }
        if (getExampleSpec() != null) {
            return RamlFragment.NamedExample;
        }
        if (getDocumentationItem() != null) {
            return RamlFragment.DocumentationItem;
        }
        throw new IllegalStateException("Fragment not yet supported");
    }
}
