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
import com.twelvemonkeys.imageio.stream.SubImageInputStream;

import javax.imageio.stream.ImageInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Base-class for JP2-Boxes
 *
 * @author <a href="mailto:mail@schmidor.de">Oliver Schmidtmer</a>
 * @author last modified by $Author$
 * @version $Id$
 */
public class JP2Box {
    protected int type;
    protected long dataLength;

    public JP2Box(int type, long dataLength) {
        this.type = type;
        this.dataLength = dataLength;
    }

    /**
     * Base-class for JP2-Super-Boxes which contain other JP2-Boxes
     */
    public static class JP2Superbox extends JP2Box {
        public ImageInputStream dataStream;

        public JP2Superbox(int type, long dataLength, ImageInputStream stream) throws IOException {
            super(type, dataLength);
            dataStream = new SubImageInputStream(stream, dataLength);
        }

        public ImageInputStream getContentStream() {
            return dataStream;
        }
    }

    /**
     * Interprets an integer of four bytes as ASCII chars
     */
    public static String asCharString(int i) {
        byte[] bytes = new byte[4];

        bytes[0] = (byte) (i >> 24);
        bytes[1] = (byte) (i >> 16);
        bytes[2] = (byte) (i >> 8);
        bytes[3] = (byte) (i);

        return new String(bytes, StandardCharsets.US_ASCII);
    }

    /**
     * Converts a "bits per component" definition to readable output. The most
     * significant bit determines whether the component is signed. Ther other
     * bits determine the component size with a range of 0 to 38 bits
     */
    public static String bpcAsString(byte bpc) {

        if (bpc == (byte) 0xFF) {
            return "255";
        }

        boolean signed = bpc < 0;
        return new String((!signed ? "u_" : "") + "" + getBPCBitlength(bpc));
    }

    /**
     * Reads the bit length of a component
     */
    public static int getBPCBitlength(byte bpc) {
        byte withoutHighBit = (byte) (bpc << 1);
        withoutHighBit = (byte) (withoutHighBit >> 1);
        return withoutHighBit + 1;
    }

    /**
     * Reads a JP2-Box from the input stream.
     *
     * @param input
     * @return
     * @throws IOException
     */
    public static JP2Box readNextBox(ImageInputStream input) throws IOException {
        long length = input.readUnsignedInt();
        long dataLength;
        int type = input.readInt();

        if (length == 0) {
            dataLength = input.length() - input.getStreamPosition();
        }
        else if (length == 1) {
            length = input.readLong();
            if (length < 0) {
                throw new IOException("XLBox to long. Can't handle longer boxes than signed long.");
            }
            else {
                dataLength = length - 16;
            }
        }
        else {
            dataLength = length - 8;
        }

        switch (type) {
            case JP2Boxes.SIGNATURE:
                return new SignatureBox(type, dataLength, input);
            case JP2Boxes.FTYP:
                return new FTYPBox(type, dataLength, input);
            case JP2Boxes.JP2H:
                return new JP2HBox(type, dataLength, input);
            case JP2Boxes.JP2C:
                return new JP2CBox(type, dataLength, input);
            case JP2Boxes.IHDR:
                return new IHDRBox(type, dataLength, input);
            case JP2Boxes.BPCC:
                return new BPCCBox(type, dataLength, input);
            case JP2Boxes.COLR:
                return new COLRBox(type, dataLength, input);
            case JP2Boxes.PCLR:
                return new PCLRBox(type, dataLength, input);
            case JP2Boxes.CMAP:
                return new CMAPBox(type, dataLength, input);
            case JP2Boxes.CDEF:
                return new CDEFBox(type, dataLength, input);
            case JP2Boxes.RES:
                return new RESBox(type, dataLength, input);
            case JP2Boxes.RESC:
                return new RESCBox(type, dataLength, input);
            case JP2Boxes.RESD:
                return new RESDBox(type, dataLength, input);
            case JP2Boxes.JP2I:
            case JP2Boxes.XML:
            case JP2Boxes.UUID:
            case JP2Boxes.UINF:
            case JP2Boxes.ULST:
            case JP2Boxes.URL:
                if (JP2Stream.DEBUG) {
                    System.out.println("JP2Stream.readNextBox: unsupported type \"" + asCharString(type) + "\"");
                }
                input.skipBytes(dataLength);
                return null;
            default:
                if (JP2Stream.DEBUG) {
                    System.out.println("JP2Stream.readNextBox: unknown type \"" + asCharString(type) + "\"");
                }
                input.skipBytes(dataLength);
                return null;
        }
    }
}
