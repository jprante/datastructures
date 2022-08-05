package com.incesoft.tools.excel.xlsx;

import javax.xml.stream.XMLStreamReader;

public interface ParsableEntry {
	void parse(XMLStreamReader reader);
}
