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
 * Color palette box.
 *
 * @author <a href="mailto:mail@schmidor.de">Oliver Schmidtmer</a>
 * @author last modified by $Author$
 * @version $Id$
 */
public class PCLRBox extends JP2Box {

    /**
     * Number of entries in table
     */
    private int n;

    /**
     * Number of palette columns
     */
    private int npc;

    /**
     * Bit depth per palette column
     */
    private byte[] b;

    /**
     * Pallete table
     */
    private long[][] c;

    public PCLRBox(int type, long dataLength, ImageInputStream stream) throws IOException {
        super(type, dataLength);

        n = stream.readUnsignedShort();
        npc = stream.readUnsignedByte();

        b = new byte[npc];
        boolean[] isSigned = new boolean[npc];
        int[] bitLengths = new int[npc];

        if (JP2Stream.DEBUG) {
            System.out.print("PCLRBox [n=" + n + ", npc=" + npc + ", b=");
        }

        for (int ci = 0; ci < npc; ci++) {
            b[ci] = stream.readByte();

            isSigned[ci] = b[ci] < 0;
            bitLengths[ci] = getBPCBitlength(b[ci]);

            if (JP2Stream.DEBUG) {
                System.out.print(bpcAsString(b[ci]) + ";");
            }
        }
        if (JP2Stream.DEBUG) {
            System.out.println("]");
        }

        c = new long[n][npc];
        for (int ei = 0; ei < n; ei++) {
            for (int ci = 0; ci < npc; ci++) {
                switch (bitLengths[ci]) {
                    case 8:
                        c[ei][ci] = isSigned[ci] ? stream.readByte() : stream.readUnsignedByte();
                        break;
                    case 16:
                        c[ei][ci] = isSigned[ci] ? stream.readShort() : stream.readUnsignedShort();
                        break;
                    case 32:
                        c[ei][ci] = isSigned[ci] ? stream.readInt() : stream.readUnsignedInt();
                        break;
                    default:
                        if (isSigned[ci]) {
                            throw new IOException(
                                    "reader only supports unsigned components if they are exactly 8, 16 or 32 bit long. Found "
                                            + bitLengths[ci] + " component.");
                        }
                        c[ei][ci] = stream.readBits(bitLengths[ci]);
                        int padBits = 8 - (bitLengths[ci] % 8);
                        if (padBits > 0) {
                            stream.readBits(padBits);
                        }
                }
            }
        }
    }
}