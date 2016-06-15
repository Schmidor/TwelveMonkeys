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

import com.twelvemonkeys.imageio.plugins.jpeg2000.jpc.JPCInputStream;
import com.twelvemonkeys.imageio.stream.SubImageInputStream;

import javax.imageio.stream.ImageInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Start of tile-part codestream box.
 *
 * @author <a href="mailto:mail@schmidor.de">Oliver Schmidtmer</a>
 * @author last modified by $Author$
 * @version $Id$
 */
public class SOTBox extends JPCBox {
    /**
     * Tile index
     */
    private int isot;

    /**
     * Length of Tilepart (SOTMarker to end of data) or 0 for length to EOC
     */
    private long psot;

    /**
     * Tilepart index
     */
    private int tpsot;

    /**
     * Number of tileparts or 0 if not specified in this part
     */
    private int tnsot;
    /**
     * Tile index
     */
    public int getIsot() {
        return isot;
    }

    /**
     * Length of Tilepart (SOTMarker to end of data) or 0 for length to EOC
     */
    public long getPsot() {
        return psot;
    }

    /**
     * Tilepart index
     */
    public int getTPsot() {
        return tpsot;
    }

    /**
     * Number of tileparts or 0 if not specified in this part
     */
    public int getTnsot() {
        return tnsot;
    }

    public ByteArrayOutputStream getTilePartData() {
        return tilePartData;
    }

    public List<JPCBox> getBoxes(){
        return boxes;
    }


    private CODBox cod;
    private Map<Integer, COCBox> coc;


    /**
     * Tile part data
     */
    private ByteArrayOutputStream tilePartData;

    private ArrayList<JPCBox> boxes = new ArrayList<>();

    public SOTBox(short type, int dataLength, ImageInputStream stream, short Csiz) throws IOException {
        super(type, dataLength);

        isot = stream.readUnsignedShort();
        psot = stream.readUnsignedInt();
        tpsot = stream.readUnsignedByte();
        tnsot = stream.readUnsignedByte();

        ImageInputStream subStream;
        if (psot == 0) {
            subStream = new SubImageInputStream(stream, stream.length() - stream.getStreamPosition()); // incl.
            // EOC
        }
        else {
            subStream = new SubImageInputStream(stream, psot - 12);
        }

        if (JPCInputStream.DEBUG) {
            System.out.println(
                    "SOTBox [isot=" + isot + ", psot=" + psot + ", tpsot=" + tpsot + ", tnsot=" + tnsot + "]");
        }

        readFully(subStream,Csiz);
    }

    private void readFully(ImageInputStream subStream, short Csiz) throws IOException {
        while (subStream.getStreamPosition() < subStream.length()) {
            JPCBox box = readNextBox(subStream, Csiz);
            if (box instanceof SODMarker) {
                subStream.mark();
                subStream.seek(subStream.length() - 2);
                if (subStream.readShort() == JPCBoxes.EOC) {
                    subStream = new SubImageInputStream(subStream,
                            subStream.length() - subStream.getStreamPosition() - 2);
                }
                subStream.reset();

                tilePartData = new ByteArrayOutputStream((int) (subStream.length() - subStream.getStreamPosition()));
                byte[] buffer = new byte[2048];
                while (subStream.getStreamPosition() < subStream.length()) {
                    int r = subStream.read(buffer);
                    tilePartData.write(buffer, 0, r);
                }
                if (JPCInputStream.DEBUG) {
                    System.out.println("/SOTBox [BITSTREAM length="
                            + (tilePartData.size()) + "]");
                }
            } else if(box instanceof CODBox){
                cod = (CODBox) box;
            } else if(box instanceof COCBox){
                if(coc == null) coc = new HashMap<>();

                coc.put(((COCBox) box).getCcoc(), (COCBox) box);
            }


            boxes.add(box);
        }
    }

    public CODBox getCod() {
        return cod;
    }

    public void setCod(CODBox cod) {
        this.cod = cod;
    }

    public COCBox getCOC(int c) {
        return coc.get(c);
    }
}