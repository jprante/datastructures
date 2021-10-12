import org.xbib.datastructures.api.DataStructure;
import org.xbib.datastructures.yaml.tiny.Yaml;

module org.xbib.datastructures.yaml.tiny {
    exports org.xbib.datastructures.yaml.tiny;
    requires org.xbib.datastructures.api;
    requires org.xbib.datastructures.tiny;
    provides DataStructure with Yaml;
}
