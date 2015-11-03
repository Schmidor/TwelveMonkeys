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
 * Display resolution box.
 *
 * @author <a href="mailto:mail@schmidor.de">Oliver Schmidtmer</a>
 * @author last modified by $Author$
 * @version $Id$
 */
public class RESDBox extends JP2Box {

    /**
     * Vertical display grid resolution numerator
     */
    private int vrdn;

    /**
     * Vertical display grid resolution denominator
     */
    private int vrdd;

    /**
     * Horizontal display grid resolution numerator
     */
    private int hrdn;

    /**
     * Horizontal display grid resolution denominator
     */
    private int hrdd;

    /**
     * Vertical display grid resolution exponent
     */
    private byte vrde;

    /**
     * Horizontal display grid resolution exponent
     */
    private byte hrde;

    public RESDBox(int type, long dataLength, ImageInputStream stream) throws IOException {
        super(type, dataLength);

        vrdn = stream.readUnsignedShort();
        vrdd = stream.readUnsignedShort();

        hrdn = stream.readUnsignedShort();
        hrdd = stream.readUnsignedShort();

        vrde = stream.readByte();
        hrde = stream.readByte();

        if (JP2Stream.DEBUG) {
            System.out.println("RESDBox [resV=" + getVRD() + "pt/m, resH=" + getHRD() + "pt/m]");
        }
    }

    public double getVRD() {
        return (vrdn / ((double) vrdd)) * Math.pow(10, vrde);
    }

    public double getHRD() {
        return (hrdn / ((double) hrdd)) * Math.pow(10, hrde);
    }
}