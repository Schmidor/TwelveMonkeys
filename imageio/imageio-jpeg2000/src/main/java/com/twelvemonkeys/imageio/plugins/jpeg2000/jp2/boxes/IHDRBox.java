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

package com.twelvemonkeys.imageio.plugins.jpeg2000.jp2.boxes;

import com.twelvemonkeys.imageio.plugins.jpeg2000.jp2.JP2Stream;

import javax.imageio.stream.ImageInputStream;
import java.io.IOException;

/**
 * Image header box.
 *
 * @author <a href="mailto:mail@schmidor.de">Oliver Schmidtmer</a>
 * @author last modified by $Author$
 * @version $Id$
 */
public class IHDRBox extends JP2Box {

    /**
     * Height, should be equal to Ysiz-YOsiz in SIZ
     */
    private final long height;

    /**
     * Width, should be equal to Xsiz-XOsiz in SIZ
     */
    private final long width;

    /**
     * Number of components, should be equal to Csiz in SIZ
     */
    private final int nc;

    /**
     * bit depth of components -1, shall be equal to Ssiz^i in SIZ if all
     * components are equal or 0xFF if not (then must have BPC box per
     * component)<br>
     * high bit 1 signed, 0 unsigned
     */
    private final byte bpc;

    /**
     * compression type
     */
    private final int c;

    /**
     * Colorspace unknown, 0=known, 1=unknown
     */
    private final int unkC;

    /**
     * Intellectual property, 0= does not contain IPR information, 1 = does
     * contain
     */
    private final int ipr;

    public IHDRBox(int type, long dataLength, ImageInputStream stream) throws IOException {
        super(type, dataLength);

        height = stream.readUnsignedInt();
        width = stream.readUnsignedInt();
        nc = stream.readUnsignedShort();
        bpc = stream.readByte();
        c = stream.readUnsignedByte();
        unkC = stream.readUnsignedByte();
        ipr = stream.readUnsignedByte();

        if (JP2Stream.DEBUG) {
            System.out.println("IHDRBox [height=" + height + ", width=" + width + ", nc=" + nc + ", bpc=" + bpcAsString(bpc) + ", c="
                    + c + ", unkC=" + unkC + ", ipr=" + ipr + "]");
        }

        if (c != 7) {
            throw new IOException("unknown compression type");
        }
    }

    public long getHeight() {
        return height;
    }

    public long getWidth() {
        return width;
    }

    public int getNc() {
        return nc;
    }

    public byte getBpc() {
        return bpc;
    }

    public int getC() {
        return c;
    }

    public int getUnkC() {
        return unkC;
    }

    public int getIpr() {
        return ipr;
    }

}