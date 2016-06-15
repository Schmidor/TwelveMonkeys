/*
 * Copyright (c) 2013, Harald Kuhr
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name "TwelveMonkeys" nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.twelvemonkeys.imageio.plugins.jpeg2000.jpc.boxes;

import com.twelvemonkeys.imageio.plugins.jpeg2000.jp2.boxes.JP2Box;
import com.twelvemonkeys.imageio.plugins.jpeg2000.jpc.JPCInputStream;

import javax.imageio.stream.ImageInputStream;
import java.io.IOException;

/**
 * SIZ-Box.
 *
 * @author <a href="mailto:mail@schmidor.de">Oliver Schmidtmer</a>
 * @author last modified by $Author$
 * @version $Id$
 */
public class SIZBox extends JPCBox {

    /**
     * Profile
     */
    private short rsiz;

    /**
     * Width of reference grid
     */
    private long xsiz;

    public short getRsiz() {
        return rsiz;
    }

    public long getXsiz() {
        return xsiz;
    }

    public long getYsiz() {
        return ysiz;
    }

    public long getXOsiz() {
        return xosiz;
    }

    public long getYOsiz() {
        return yosiz;
    }

    public long getXTsiz() {
        return xtsiz;
    }

    public long getYTsiz() {
        return ytsiz;
    }

    public long getXTOsiz() {
        return xtosiz;
    }

    public long getYTOsiz() {
        return ytosiz;
    }

    public short getCsiz() {
        return csiz;
    }

    public byte[] getSsiz() {
        return ssiz;
    }

    public int[] getXRsiz() {
        return xrsiz;
    }

    public int[] getYRsiz() {
        return yrsiz;
    }

    /**
     * Height of reference grid
     */
    private long ysiz;

    /**
     * Left offset to reference grid
     */
    private long xosiz;

    /**
     * Top offset to reference grid
     */
    private long yosiz;

    /**
     * Width of reference tile with respect to reference grid
     */
    private long xtsiz;

    /**
     * Height of reference tile with respect to reference grid
     */
    private long ytsiz;

    /**
     * Horizontal offset from the origin of the reference grid to the left
     * side of the first tile
     */
    private long xtosiz;

    /**
     * Vertical offset from the origin of the reference grid to the top side
     * of the first tile
     */
    private long ytosiz;

    /**
     * Number of components
     */
    private short csiz;

    /**
     * Component precision
     */
    private byte[] ssiz;

    /**
     * Horizontal separation of a sample of component with respect to the
     * reference grid
     */
    private int[] xrsiz;

    /**
     * Vertical separation of a sample of component with respect to the
     * reference grid
     */
    private int[] yrsiz;

    public SIZBox(short type, int dataLength, ImageInputStream stream) throws IOException {
        super(type, dataLength);

        rsiz = stream.readShort();
        xsiz = stream.readUnsignedInt();
        ysiz = stream.readUnsignedInt();
        xosiz = stream.readUnsignedInt();
        yosiz = stream.readUnsignedInt();
        xtsiz = stream.readUnsignedInt();
        ytsiz = stream.readUnsignedInt();
        xtosiz = stream.readUnsignedInt();
        ytosiz = stream.readUnsignedInt();

        csiz = stream.readShort();
        ssiz = new byte[csiz];
        xrsiz = new int[csiz];
        yrsiz = new int[csiz];

        if (JPCInputStream.DEBUG) {
            System.out.print("SIZBox [rsiz=" + rsiz + ", xsiz=" + xsiz + ", ysiz=" + ysiz + ", xosiz=" + xosiz
                    + ", yosiz=" + yosiz + ", xtsiz=" + xtsiz + ", ytsiz=" + ytsiz + ", xtosiz=" + xtosiz
                    + ", ytosiz=" + ytosiz + ", csiz=" + csiz + "] (");

        }

        for (int i = 0; i < csiz; i++) {
            ssiz[i] = stream.readByte();
            xrsiz[i] = stream.readUnsignedByte();
            yrsiz[i] = stream.readUnsignedByte();

            if (JPCInputStream.DEBUG) {
                System.out.print(JP2Box.bpcAsString(ssiz[i]) + " xrsiz=" + xrsiz[i] + " yrsiz=" + yrsiz[i] + ", ");
            }
        }
        if (JPCInputStream.DEBUG) {
            System.out.println(")");
        }
    }
}