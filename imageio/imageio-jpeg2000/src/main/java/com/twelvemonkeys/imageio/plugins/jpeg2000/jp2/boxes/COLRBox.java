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

import javax.imageio.stream.ImageInputStream;
import java.awt.color.ColorSpace;
import java.awt.color.ICC_Profile;
import java.io.IOException;

/**
 * Colour space definition box.
 *
 * @author <a href="mailto:mail@schmidor.de">Oliver Schmidtmer</a>
 * @author last modified by $Author$
 * @version $Id$
 */
public class COLRBox extends JP2Box {
    /**
     * Specification method. 1= Enumerated Colourspace, 2= Restriced ICC
     * profile
     */
    private int meth;

    /**
     * Precedence
     */
    private byte prec;

    /**
     * Colourspace approcimation
     */
    private int approx;

    /**
     * Enumerated colourspace
     */
    private long enumCS;

    /**
     * ICC profile
     */
    private ICC_Profile profile;

    public COLRBox(int type, long dataLength, ImageInputStream stream) throws IOException {
        super(type, dataLength);

        meth = stream.readUnsignedByte();

        approx = stream.readUnsignedByte();

        prec = stream.readByte();

        if (meth == 1) {
            enumCS = stream.readUnsignedInt();

            //TODO: change to better colorspaces, GRAY should map non linear, YCC should be sYCC
            if (enumCS == 16) {
                profile = ICC_Profile.getInstance(ColorSpace.CS_sRGB);
            }
            else if (enumCS == 17) {
                profile = ICC_Profile.getInstance(ColorSpace.CS_GRAY);
            }
            else if (enumCS == 18) {
                profile = ICC_Profile.getInstance(ColorSpace.CS_PYCC);
            }
        }

        if (meth == 2) {
            byte[] data = new byte[(int) (dataLength - 3)];
            stream.readFully(data);
            profile = ICC_Profile.getInstance(data);
        }
    }

    public int getMeth() {
        return meth;
    }

    public byte getPrec() {
        return prec;
    }

    public int getApprox() {
        return approx;
    }

    public long getEnumCS() {
        return enumCS;
    }

    public ICC_Profile getProfile() {
        return profile;
    }
}