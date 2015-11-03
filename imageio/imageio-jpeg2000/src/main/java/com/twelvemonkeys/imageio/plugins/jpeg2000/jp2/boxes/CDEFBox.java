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
 * Codestream component type definition box.
 *
 * @author <a href="mailto:mail@schmidor.de">Oliver Schmidtmer</a>
 * @author last modified by $Author$
 * @version $Id$
 */
public class CDEFBox extends JP2Box {

    /**
     * Number of channel descriptions
     */
    private int n;

    /**
     * Channel index per channel description
     */
    private int[] cn;

    /**
     * Channel type per channel description: 0=color, 1=opacity, 2=premultiplied opacity, 2^16-1=unspecified
     */
    private int[] typ;

    /**
     * Channel association per channel description: 0=for all channels, 1-2^16-2: color, 2^16-1: not associated
     */
    private int[] asoc;

    public CDEFBox(int type, long dataLength, ImageInputStream stream) throws IOException {
        super(type, dataLength);

        n = stream.readUnsignedShort();

        cn = new int[n];

        if (JP2Stream.DEBUG) {
            System.out.print("CDEFBox [");
        }

        for (int i = 0; i < n; i++) {
            cn[i] = stream.readUnsignedShort();
            typ[i] = stream.readUnsignedShort();
            asoc[i] = stream.readUnsignedShort();

            if (JP2Stream.DEBUG) {
                System.out.print("cn=" + cn[i] + ", typ=" + typ[i] + ", asoc=" + asoc[i] + ";");
            }
        }

        if (JP2Stream.DEBUG) {
            System.out.println("]");
        }
    }
}