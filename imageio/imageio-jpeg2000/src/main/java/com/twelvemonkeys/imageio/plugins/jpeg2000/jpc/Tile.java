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

package com.twelvemonkeys.imageio.plugins.jpeg2000.jpc;

import com.twelvemonkeys.imageio.plugins.jpeg2000.jpc.boxes.JPCBox;
import com.twelvemonkeys.imageio.plugins.jpeg2000.jpc.boxes.SOTBox;

import java.io.IOException;

/**
 * Tile of a JPEG2000 Codestream
 *
 * @author <a href="mailto:mail@schmidor.de">Oliver Schmidtmer</a>
 * @author last modified by $Author$
 * @version $Id$
 */
public class Tile {
    SOTBox[] parts;

    public Tile(SOTBox firstPart) throws IOException {
        if (firstPart.getTPsot() != 0) {
            throw new IOException("tile does not start with part 0");
        }

        parts = new SOTBox[firstPart.getTnsot()];
        parts[0] = firstPart;
    }

    public SOTBox[] getParts() {
        return parts;
    }

    /**
     * Coding-Style for tile.
     * <code>first Tile-part COC > first Tile-part COD > Main COC > Main COD</code>
     *
     * @param component
     * @return COC or COD box, if tile has one.
     */
    public JPCBox getTileCodingStyle(int component) {
        JPCBox box = parts[0].getCOC(component);
        if (box != null) {
            return box;
        }
        return parts[0].getCod();
    }

    public void getQuantization(/* per component */) {
        // TODO: first Tile-part QCC > first Tile-part QCD > Main QCC > Main QCD
    }

    public void getROI(/* per component */) {
        // TODO: first Tile-part ROI > Main ROI > none
    }

    public void getPOC() {
        // TODO: first Tile-part POC > Main POC > first Tile-part COD > Main COD
    }
}