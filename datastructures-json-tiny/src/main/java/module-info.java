import org.xbib.datastructures.api.DataStructure;
import org.xbib.datastructures.json.tiny.Json;

module org.xbib.datastructures.json.tiny {
    exports org.xbib.datastructures.json.tiny;
    requires org.xbib.datastructures.api;
    requires org.xbib.datastructures.tiny;
    provides DataStructure with Json;
}
