/*********************************************************************
 *
 *      Copyright (C) 2002 Andrew Khan
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 ***************************************************************************/

package jxl.write.biff;

import jxl.biff.Type;
import jxl.biff.WritableRecordData;

/**
 * The headings options from the Page Setup dialog box
 */
class PrintHeadersRecord extends WritableRecordData {
    /**
     * The binary data
     */
    private final byte[] data;
    /**
     * Flag to print headers
     */
    private final boolean printHeaders;

    /**
     * Constructor
     *
     * @param ph print headers flag
     */
    public PrintHeadersRecord(boolean ph) {
        super(Type.PRINTHEADERS);
        printHeaders = ph;

        data = new byte[2];

        if (printHeaders) {
            data[0] = 1;
        }
    }

    /**
     * Gets the binary data for output to file
     *
     * @return the binary data
     */
    public byte[] getData() {
        return data;
    }
}


