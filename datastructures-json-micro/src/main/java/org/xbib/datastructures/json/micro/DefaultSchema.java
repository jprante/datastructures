package org.xbib.datastructures.json.micro;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

class DefaultSchema implements Schema {
    interface Instruction extends Function<Json, Json> {
    }

    static Json maybeError(Json errors, Json E) {
        return E == null ? errors : (errors == null ? Json.array() : errors).with(E, new Json[0]);
    }

    // Anything is valid schema
    static Instruction any = new Instruction() {
        public Json apply(Json param) {
            return null;
        }
    };

    // Type validation
    class IsObject implements Instruction {
        public Json apply(Json param) {
            return param.isObject() ? null : Json.make(param.toString(maxchars));
        }
    }

    class IsArray implements Instruction {
        public Json apply(Json param) {
            return param.isArray() ? null : Json.make(param.toString(maxchars));
        }
    }

    class IsString implements Instruction {
        public Json apply(Json param) {
            return param.isString() ? null : Json.make(param.toString(maxchars));
        }
    }

    class IsBoolean implements Instruction {
        public Json apply(Json param) {
            return param.isBoolean() ? null : Json.make(param.toString(maxchars));
        }
    }

    class IsNull implements Instruction {
        public Json apply(Json param) {
            return param.isNull() ? null : Json.make(param.toString(maxchars));
        }
    }

    class IsNumber implements Instruction {
        public Json apply(Json param) {
            return param.isNumber() ? null : Json.make(param.toString(maxchars));
        }
    }

    class IsInteger implements Instruction {
        public Json apply(Json param) {
            return param.isNumber() && param.getValue() instanceof Integer ? null : Json.make(param.toString(maxchars));
        }
    }

    class CheckString implements Instruction {
        int min = 0, max = Integer.MAX_VALUE;
        Pattern pattern;

        public Json apply(Json param) {
            Json errors = null;
            if (!param.isString()) {
                return errors;
            }
            String s = param.asString();
            final int size = s.codePointCount(0, s.length());
            if (size < min || size > max) {
                errors = maybeError(errors, Json.make("String  " + param.toString(maxchars) +
                        " has length outside of the permitted range [" + min + "," + max + "]."));
            }
            if (pattern != null && !pattern.matcher(s).matches()) {
                errors = maybeError(errors, Json.make("String  " + param.toString(maxchars) +
                        " does not match regex " + pattern.toString()));
            }
            return errors;
        }
    }

    class CheckNumber implements Instruction {
        double min = Double.NaN, max = Double.NaN, multipleOf = Double.NaN;
        boolean exclusiveMin = false, exclusiveMax = false;

        public Json apply(Json param) {
            Json errors = null;
            if (!param.isNumber()) {
                return errors;
            }
            double value = param.asDouble();
            if (!Double.isNaN(min) && (value < min || exclusiveMin && value == min)) {
                errors = maybeError(errors, Json.make("Number " + param + " is below allowed minimum " + min));
            }
            if (!Double.isNaN(max) && (value > max || exclusiveMax && value == max)) {
                errors = maybeError(errors, Json.make("Number " + param + " is above allowed maximum " + max));
            }
            if (!Double.isNaN(multipleOf) && (value / multipleOf) % 1 != 0) {
                errors = maybeError(errors, Json.make("Number " + param + " is not a multiple of  " + multipleOf));
            }
            return errors;
        }
    }

    class CheckArray implements Instruction {
        int min = 0, max = Integer.MAX_VALUE;
        Boolean uniqueitems = null;
        Instruction additionalSchema = any;
        Instruction schema;
        ArrayList<Instruction> schemas;

        public Json apply(Json param) {
            Json errors = null;
            if (!param.isArray()) {
                return errors;
            }
            if (schema == null && schemas == null && additionalSchema == null) // no schema specified
            {
                return errors;
            }
            int size = param.asJsonList().size();
            for (int i = 0; i < size; i++) {
                Instruction S = schema != null ? schema
                        : (schemas != null && i < schemas.size()) ? schemas.get(i) : additionalSchema;
                if (S == null) {
                    errors = maybeError(errors, Json.make("Additional items are not permitted: " +
                            param.at(i) + " in " + param.toString(maxchars)));
                } else {
                    errors = maybeError(errors, S.apply(param.at(i)));
                }
                if (uniqueitems != null && uniqueitems && param.asJsonList().lastIndexOf(param.at(i)) > i) {
                    errors = maybeError(errors, Json.make("Element " + param.at(i) + " is duplicate in array."));
                }
                if (errors != null && !errors.asJsonList().isEmpty()) {
                    break;
                }
            }
            if (size < min || size > max) {
                errors = maybeError(errors, Json.make("Array  " + param.toString(maxchars) +
                        " has number of elements outside of the permitted range [" + min + "," + max + "]."));
            }
            return errors;
        }
    }

    class CheckPropertyPresent implements Instruction {
        String propname;

        public CheckPropertyPresent(String propname) {
            this.propname = propname;
        }

        public Json apply(Json param) {
            if (!param.isObject()) {
                return null;
            }
            if (param.has(propname)) {
                return null;
            } else {
                return Json.array().add(Json.make("Required property " + propname +
                        " missing from object " + param.toString(maxchars)));
            }
        }
    }

    class CheckObject implements Instruction {
        int min = 0, max = Integer.MAX_VALUE;
        Instruction additionalSchema = any;
        ArrayList<CheckObject.CheckProperty> props = new ArrayList<CheckObject.CheckProperty>();
        ArrayList<CheckObject.CheckPatternProperty> patternProps = new ArrayList<CheckObject.CheckPatternProperty>();

        // Object validation
        class CheckProperty implements Instruction {
            String name;
            Instruction schema;

            public CheckProperty(String name, Instruction schema) {
                this.name = name;
                this.schema = schema;
            }

            public Json apply(Json param) {
                Json value = param.at(name);
                if (value == null) {
                    return null;
                } else {
                    return schema.apply(param.at(name));
                }
            }
        }

        class CheckPatternProperty // implements Instruction
        {
            Pattern pattern;
            Instruction schema;

            public CheckPatternProperty(String pattern, Instruction schema) {
                this.pattern = Pattern.compile(pattern);
                this.schema = schema;
            }

            public Json apply(Json param, Set<String> found) {
                Json errors = null;
                for (Map.Entry<String, Json> e : param.asJsonMap().entrySet()) {
                    if (pattern.matcher(e.getKey()).find()) {
                        found.add(e.getKey());
                        errors = maybeError(errors, schema.apply(e.getValue()));
                    }
                }
                return errors;
            }
        }

        public Json apply(Json param) {
            Json errors = null;
            if (!param.isObject()) {
                return errors;
            }
            HashSet<String> checked = new HashSet<String>();
            for (CheckObject.CheckProperty I : props) {
                if (param.has(I.name)) {
                    checked.add(I.name);
                }
                errors = maybeError(errors, I.apply(param));
            }
            for (CheckObject.CheckPatternProperty I : patternProps) {

                errors = maybeError(errors, I.apply(param, checked));
            }
            if (additionalSchema != any) {
                for (Map.Entry<String, Json> e : param.asJsonMap().entrySet()) {
                    if (!checked.contains(e.getKey())) {
                        errors = maybeError(errors, additionalSchema == null ?
                                Json.make("Extra property '" + e.getKey() +
                                        "', schema doesn't allow any properties not explicitly defined:" +
                                        param.toString(maxchars))
                                : additionalSchema.apply(e.getValue()));
                    }
                }
            }
            if (param.asJsonMap().size() < min) {
                errors = maybeError(errors, Json.make("Object " + param.toString(maxchars) +
                        " has fewer than the permitted " + min + "  number of properties."));
            }
            if (param.asJsonMap().size() > max) {
                errors = maybeError(errors, Json.make("Object " + param.toString(maxchars) +
                        " has more than the permitted " + min + "  number of properties."));
            }
            return errors;
        }
    }

    class Sequence implements Instruction {
        ArrayList<Instruction> seq = new ArrayList<Instruction>();

        public Json apply(Json param) {
            Json errors = null;
            for (Instruction I : seq) {
                errors = maybeError(errors, I.apply(param));
            }
            return errors;
        }

        public Sequence add(Instruction I) {
            seq.add(I);
            return this;
        }
    }

    class CheckType implements Instruction {
        Json types;

        public CheckType(Json types) {
            this.types = types;
        }

        public Json apply(Json param) {
            String ptype = param.isString() ? "string" :
                    param.isObject() ? "object" :
                            param.isArray() ? "array" :
                                    param.isNumber() ? "number" :
                                            param.isNull() ? "null" : "boolean";
            for (Json type : types.asJsonList()) {
                if (type.asString().equals(ptype)) {
                    return null;
                } else if (type.asString().equals("integer") &&
                        param.isNumber() &&
                        param.asDouble() % 1 == 0) {
                    return null;
                }
            }
            return Json.array().add(Json.make("Type mistmatch for " + param.toString(maxchars) +
                    ", allowed types: " + types));
        }
    }

    class CheckEnum implements Instruction {
        Json theenum;

        public CheckEnum(Json theenum) {
            this.theenum = theenum;
        }

        public Json apply(Json param) {
            for (Json option : theenum.asJsonList()) {
                if (param.equals(option)) {
                    return null;
                }
            }
            return Json.array().add("Element " + param.toString(maxchars) +
                    " doesn't match any of enumerated possibilities " + theenum);
        }
    }

    class CheckAny implements Instruction {
        ArrayList<Instruction> alternates = new ArrayList<Instruction>();
        Json schema;

        public Json apply(Json param) {
            for (Instruction I : alternates) {
                if (I.apply(param) == null) {
                    return null;
                }
            }
            return Json.array().add("Element " + param.toString(maxchars) +
                    " must conform to at least one of available sub-schemas " +
                    schema.toString(maxchars));
        }
    }

    class CheckOne implements Instruction {
        ArrayList<Instruction> alternates = new ArrayList<Instruction>();
        Json schema;

        public Json apply(Json param) {
            int matches = 0;
            Json errors = Json.array();
            for (Instruction I : alternates) {
                Json result = I.apply(param);
                if (result == null) {
                    matches++;
                } else {
                    errors.add(result);
                }
            }
            if (matches != 1) {
                return Json.array().add("Element " + param.toString(maxchars) +
                        " must conform to exactly one of available sub-schemas, but not more " +
                        schema.toString(maxchars)).add(errors);
            } else {
                return null;
            }
        }
    }

    class CheckNot implements Instruction {
        Instruction I;
        Json schema;

        public CheckNot(Instruction I, Json schema) {
            this.I = I;
            this.schema = schema;
        }

        public Json apply(Json param) {
            if (I.apply(param) != null) {
                return null;
            } else {
                return Json.array().add("Element " + param.toString(maxchars) +
                        " must NOT conform to the schema " + schema.toString(maxchars));
            }
        }
    }

    class CheckSchemaDependency implements Instruction {
        Instruction schema;
        String property;

        public CheckSchemaDependency(String property, Instruction schema) {
            this.property = property;
            this.schema = schema;
        }

        public Json apply(Json param) {
            if (!param.isObject()) {
                return null;
            } else if (!param.has(property)) {
                return null;
            } else {
                return (schema.apply(param));
            }
        }
    }

    class CheckPropertyDependency implements Instruction {
        Json required;
        String property;

        public CheckPropertyDependency(String property, Json required) {
            this.property = property;
            this.required = required;
        }

        public Json apply(Json param) {
            if (!param.isObject()) {
                return null;
            }
            if (!param.has(property)) {
                return null;
            } else {
                Json errors = null;
                for (Json p : required.asJsonList()) {
                    if (!param.has(p.asString())) {
                        errors = maybeError(errors, Json.make("Conditionally required property " + p +
                                " missing from object " + param.toString(maxchars)));
                    }
                }
                return errors;
            }
        }
    }

    Instruction compile(Json S, Map<Json, Instruction> compiled) {
        Instruction result = compiled.get(S);
        if (result != null) {
            return result;
        }
        Sequence seq = new Sequence();
        compiled.put(S, seq);
        if (S.has("type") && !S.is("type", "any")) {
            seq.add(new CheckType(S.at("type").isString() ?
                    Json.array().add(S.at("type")) : S.at("type")));
        }
        if (S.has("enum")) {
            seq.add(new CheckEnum(S.at("enum")));
        }
        if (S.has("allOf")) {
            Sequence sub = new Sequence();
            for (Json x : S.at("allOf").asJsonList()) {
                sub.add(compile(x, compiled));
            }
            seq.add(sub);
        }
        if (S.has("anyOf")) {
            CheckAny any = new CheckAny();
            any.schema = S.at("anyOf");
            for (Json x : any.schema.asJsonList()) {
                any.alternates.add(compile(x, compiled));
            }
            seq.add(any);
        }
        if (S.has("oneOf")) {
            CheckOne any = new CheckOne();
            any.schema = S.at("oneOf");
            for (Json x : any.schema.asJsonList()) {
                any.alternates.add(compile(x, compiled));
            }
            seq.add(any);
        }
        if (S.has("not")) {
            seq.add(new CheckNot(compile(S.at("not"), compiled), S.at("not")));
        }

        if (S.has("required") && S.at("required").isArray()) {
            for (Json p : S.at("required").asJsonList()) {
                seq.add(new CheckPropertyPresent(p.asString()));
            }
        }
        CheckObject objectCheck = new CheckObject();
        if (S.has("properties")) {
            for (Map.Entry<String, Json> p : S.at("properties").asJsonMap().entrySet()) {
                objectCheck.props.add(objectCheck.new CheckProperty(
                        p.getKey(), compile(p.getValue(), compiled)));
            }
        }
        if (S.has("patternProperties")) {
            for (Map.Entry<String, Json> p : S.at("patternProperties").asJsonMap().entrySet()) {
                objectCheck.patternProps.add(objectCheck.new CheckPatternProperty(p.getKey(),
                        compile(p.getValue(), compiled)));
            }
        }
        if (S.has("additionalProperties")) {
            if (S.at("additionalProperties").isObject()) {
                objectCheck.additionalSchema = compile(S.at("additionalProperties"), compiled);
            } else if (!S.at("additionalProperties").asBoolean()) {
                objectCheck.additionalSchema = null; // means no additional properties allowed
            }
        }
        if (S.has("minProperties")) {
            objectCheck.min = S.at("minProperties").asInteger();
        }
        if (S.has("maxProperties")) {
            objectCheck.max = S.at("maxProperties").asInteger();
        }

        if (!objectCheck.props.isEmpty() || !objectCheck.patternProps.isEmpty() ||
                objectCheck.additionalSchema != any ||
                objectCheck.min > 0 || objectCheck.max < Integer.MAX_VALUE) {
            seq.add(objectCheck);
        }

        CheckArray arrayCheck = new CheckArray();
        if (S.has("items")) {
            if (S.at("items").isObject()) {
                arrayCheck.schema = compile(S.at("items"), compiled);
            } else {
                arrayCheck.schemas = new ArrayList<Instruction>();
                for (Json s : S.at("items").asJsonList()) {
                    arrayCheck.schemas.add(compile(s, compiled));
                }
            }
        }
        if (S.has("additionalItems")) {
            if (S.at("additionalItems").isObject()) {
                arrayCheck.additionalSchema = compile(S.at("additionalItems"), compiled);
            } else if (!S.at("additionalItems").asBoolean()) {
                arrayCheck.additionalSchema = null;
            }
        }
        if (S.has("uniqueItems")) {
            arrayCheck.uniqueitems = S.at("uniqueItems").asBoolean();
        }
        if (S.has("minItems")) {
            arrayCheck.min = S.at("minItems").asInteger();
        }
        if (S.has("maxItems")) {
            arrayCheck.max = S.at("maxItems").asInteger();
        }
        if (arrayCheck.schema != null || arrayCheck.schemas != null ||
                arrayCheck.additionalSchema != any ||
                arrayCheck.uniqueitems != null ||
                arrayCheck.max < Integer.MAX_VALUE || arrayCheck.min > 0) {
            seq.add(arrayCheck);
        }

        CheckNumber numberCheck = new CheckNumber();
        if (S.has("minimum")) {
            numberCheck.min = S.at("minimum").asDouble();
        }
        if (S.has("maximum")) {
            numberCheck.max = S.at("maximum").asDouble();
        }
        if (S.has("multipleOf")) {
            numberCheck.multipleOf = S.at("multipleOf").asDouble();
        }
        if (S.has("exclusiveMinimum")) {
            numberCheck.exclusiveMin = S.at("exclusiveMinimum").asBoolean();
        }
        if (S.has("exclusiveMaximum")) {
            numberCheck.exclusiveMax = S.at("exclusiveMaximum").asBoolean();
        }
        if (!Double.isNaN(numberCheck.min) || !Double.isNaN(numberCheck.max) || !Double.isNaN(numberCheck.multipleOf)) {
            seq.add(numberCheck);
        }

        CheckString stringCheck = new CheckString();
        if (S.has("minLength")) {
            stringCheck.min = S.at("minLength").asInteger();
        }
        if (S.has("maxLength")) {
            stringCheck.max = S.at("maxLength").asInteger();
        }
        if (S.has("pattern")) {
            stringCheck.pattern = Pattern.compile(S.at("pattern").asString());
        }
        if (stringCheck.min > 0 || stringCheck.max < Integer.MAX_VALUE || stringCheck.pattern != null) {
            seq.add(stringCheck);
        }

        if (S.has("dependencies")) {
            for (Map.Entry<String, Json> e : S.at("dependencies").asJsonMap().entrySet()) {
                if (e.getValue().isObject()) {
                    seq.add(new CheckSchemaDependency(e.getKey(), compile(e.getValue(), compiled)));
                } else if (e.getValue().isArray()) {
                    seq.add(new CheckPropertyDependency(e.getKey(), e.getValue()));
                } else {
                    seq.add(new CheckPropertyDependency(e.getKey(), Json.array(e.getValue())));
                }
            }
        }
        result = seq.seq.size() == 1 ? seq.seq.get(0) : seq;
        compiled.put(S, result);
        return result;
    }

    int maxchars = 50;
    URI uri;
    Json theschema;
    Instruction start;

    DefaultSchema(URI uri, Json theschema, Function<URI, Json> relativeReferenceResolver) {
        try {
            this.uri = uri == null ? new URI("") : uri;
            if (relativeReferenceResolver == null) {
                relativeReferenceResolver = new Function<URI, Json>() {
                    public Json apply(URI docuri) {
                        try {
                            return Json.read(Json.fetchContent(docuri.toURL()));
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                };
            }
            this.theschema = theschema.dup();
            this.theschema = Json.expandReferences(this.theschema,
                    this.theschema,
                    this.uri,
                    new HashMap<String, Json>(),
                    new IdentityHashMap<Json, Json>(),
                    relativeReferenceResolver);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        this.start = compile(this.theschema, new IdentityHashMap<Json, Instruction>());
    }

    public Json validate(Json document) {
        Json result = Json.object("ok", true);
        Json errors = start.apply(document);
        return errors == null ? result : result.set("errors", errors).set("ok", false);
    }

    public Json toJson() {
        return theschema;
    }

    public Json generate(Json options) {
        // TODO...
        return Json.nil();
    }
}
