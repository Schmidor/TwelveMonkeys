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

import com.twelvemonkeys.imageio.plugins.jpeg2000.jpc.JPCStream;

import javax.imageio.stream.ImageInputStream;
import java.io.IOException;

/**
 * Base-class for codestream boxes and markers.
 *
 * @author <a href="mailto:mail@schmidor.de">Oliver Schmidtmer</a>
 * @author last modified by $Author$
 * @version $Id$
 */
public class JPCBox {
    protected short type;
    protected int dataLength;

    public JPCBox(short type, int dataLength) {
        this.type = type;
        this.dataLength = dataLength;
    }

    /**
     * Reads a codestream box or marker from the input stream.
     *
     * @param input
     * @return
     * @throws IOException
     */
    public static JPCBox readNextBox(ImageInputStream input) throws IOException {
        short type = input.readShort();

        switch (type) {
            case JPCBoxes.SOC:
                return new SOCMarker(type, 0);
            case JPCBoxes.EOC:
                return new EOCMarker(type, 0);
            case JPCBoxes.SOD:
                return new SODMarker(type, 0);
        }

        int paramLength = input.readUnsignedShort() - 2;
        switch (type) {
            case JPCBoxes.SOT:
                return new SOTBox(type, paramLength, input);

            case JPCBoxes.SIZ:
                return new SIZBox(type, paramLength, input);
            case JPCBoxes.COD:
            case JPCBoxes.QCD:
            case JPCBoxes.COM:

            case JPCBoxes.COC:
            case JPCBoxes.QCC:

            case JPCBoxes.RGN:
            case JPCBoxes.POC:

            case JPCBoxes.PLM:
            case JPCBoxes.PLT:
            case JPCBoxes.PPM:
            case JPCBoxes.PPT:
            case JPCBoxes.TLM:
            case JPCBoxes.SOP:
            case JPCBoxes.EPH:
            case JPCBoxes.CRG:
            default:
                if (JPCStream.DEBUG) {
                    System.out.println("JPEG2000CodeStream.readNextBox: unknown type " + Integer.toHexString(type & 0xffff)
                            + " length=" + paramLength);
                }
                input.skipBytes(paramLength);
                return null;
        }
    }
}
