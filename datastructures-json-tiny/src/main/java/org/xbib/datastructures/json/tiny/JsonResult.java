package org.xbib.datastructures.json.tiny;

import org.xbib.datastructures.api.Node;

public interface JsonResult extends JsonListener {

    Node<?> getResult();
}
