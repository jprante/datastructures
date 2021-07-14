package org.xbib.datastructures.json.micro;

public interface Schema {
    Json validate(Json document);

    Json toJson();

}
