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

import com.twelvemonkeys.imageio.plugins.jpeg2000.jpc.boxes.*;

import javax.imageio.stream.ImageInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * JPEG2000 Codestream
 *
 * @author <a href="mailto:mail@schmidor.de">Oliver Schmidtmer</a>
 * @author last modified by $Author$
 * @version $Id$
 */
public class JPCInputStream extends InputStream {

    public final static boolean DEBUG = "true"
            .equalsIgnoreCase(System.getProperty("com.twelvemonkeys.imageio.plugins.jpeg2000.debug"));

    private ImageInputStream inputStream;

    private HashMap<Integer, Tile> tiles = new HashMap<Integer, Tile>();

    private SIZBox sizBox;

    public static byte[] getMagic() {
        return new byte[] {(byte) 0xFF, 0x4F};
    }

    public JPCInputStream(ImageInputStream input) {
        this.inputStream = input;
    }

    public int getWidth(int index) throws IOException {
        readBoxes(false);
        return (int) (sizBox.getXsiz() - sizBox.getXOsiz());
    }

    public int getHeight(int index) throws IOException {
        readBoxes(false);
        return (int) (sizBox.getYsiz() - sizBox.getYOsiz());
    }

    private void readBoxes(boolean fully) throws IOException {
        while (inputStream.length() > inputStream.getStreamPosition()) {
            if (!fully && sizBox != null) {
                break;
            }

            JPCBox box = JPCBox.readNextBox(inputStream, sizBox != null? sizBox.getCsiz() : -1);

            if (box instanceof SOTBox) {
                SOTBox tilePart = (SOTBox) box;
                if (tiles.containsKey(tilePart.getIsot())) {
                    tiles.get(tilePart.getIsot()).getParts()[tilePart.getTnsot()] = tilePart;
                }
            }
            else if (box instanceof SOCMarker) {
                // START
            }
            else if (box instanceof EOCMarker) {
                // EOF
                break;
            }
            else if (box instanceof SODMarker) {
                throw new IOException("found " + box.getClass().getSimpleName() + " outside of tile");
            }
            else if (box instanceof SIZBox) {
                this.sizBox = (SIZBox) box;
            }
            else {
                if(DEBUG){
                    System.out.println("box not used: " + box);
                }
            }
            // TODO: Globale COD und COC Boxen speichern
        }
    }

    @Override
    public int read() throws IOException {
        readBoxes(true);

        return -1;
    }
}
