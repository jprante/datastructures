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

import jxl.biff.IntegerHelper;
import jxl.biff.Type;
import jxl.biff.WritableRecordData;

/**
 * Writes out the workbook option flag (should it save the external
 * link options)
 */
class BookboolRecord extends WritableRecordData {
    /**
     * The external link option flag
     */
    private final boolean externalLink;
    /**
     * The binary data to write out
     */
    private final byte[] data;

    /**
     * Constructor
     *
     * @param extlink the external link options flag
     */
    public BookboolRecord(boolean extlink) {
        super(Type.BOOKBOOL);

        externalLink = extlink;
        data = new byte[2];

        if (!externalLink) {
            IntegerHelper.getTwoBytes(1, data, 0);
        }
    }

    /**
     * Gets the binary data to write to the output file
     *
     * @return the binary data
     */
    public byte[] getData() {
        return data;
    }
}
