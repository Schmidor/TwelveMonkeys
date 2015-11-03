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
 * Capture grid resolution box.
 *
 * @author <a href="mailto:mail@schmidor.de">Oliver Schmidtmer</a>
 * @author last modified by $Author$
 * @version $Id$
 */
public class RESCBox extends JP2Box {

    /**
     * Vertical capture grid resolution numerator
     */
    private int vrcn;

    /**
     * Vertical capture grid resolution denominator
     */
    private int vrcd;

    /**
     * Horizontal capture grid resolution numerator
     */
    private int hrcn;

    /**
     * Horizontal capture grid resolution denominator
     */
    private int hrcd;

    /**
     * Vertical capture grid resolution exponent
     */
    private byte vrce;

    /**
     * Horizontal capture grid resolution exponent
     */
    private byte hrce;

    public RESCBox(int type, long dataLength, ImageInputStream stream) throws IOException {
        super(type, dataLength);

        vrcn = stream.readUnsignedShort();
        vrcd = stream.readUnsignedShort();

        hrcn = stream.readUnsignedShort();
        hrcd = stream.readUnsignedShort();

        vrce = stream.readByte();
        hrce = stream.readByte();

        if (JP2Stream.DEBUG) {
            System.out.println("RESCBox [resV=" + getVRC() + "pt/m, resH=" + getHRC() + "pt/m]");
        }
    }

    public double getVRC() {
        return (vrcn / ((double) vrcd)) * Math.pow(10, vrce);
    }

    public double getHRC() {
        return (hrcn / ((double) hrcd)) * Math.pow(10, hrce);
    }
}