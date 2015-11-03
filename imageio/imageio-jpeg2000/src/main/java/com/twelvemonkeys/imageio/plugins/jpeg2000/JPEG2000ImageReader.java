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

package com.twelvemonkeys.imageio.plugins.jpeg2000;

import com.twelvemonkeys.imageio.ImageReaderBase;
import com.twelvemonkeys.imageio.plugins.jpeg2000.jp2.JP2Stream;
import com.twelvemonkeys.imageio.plugins.jpeg2000.jpc.JPCStream;

import javax.imageio.ImageReadParam;
import javax.imageio.ImageTypeSpecifier;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * ImageReader implementation for JPEG2000.
 * <p>See ISO/IEC 15444-1 or ITU-T T.800 (08/2002)</p>
 * @author <a href="mailto:mail@schmidor.de">Oliver Schmidtmer</a>
 * @author last modified by $Author$
 * @version $Id$
 */
public class JPEG2000ImageReader extends ImageReaderBase {

    InputStream readerStream = null;

    public JPEG2000ImageReader(JPEG2000ImageReaderSpi jpeg2000ImageReaderSpi) {
        super(jpeg2000ImageReaderSpi);
    }

    @Override
    protected void resetMembers() {
        readerStream = null;
    }

    @Override
    public int getHeight(int imageIndex) throws IOException {
        initReader();
        if (readerStream instanceof JP2Stream) {
            return ((JP2Stream) readerStream).getHeight(imageIndex);
        }
        else if (readerStream instanceof JPCStream) {
            return ((JPCStream) readerStream).getHeight(imageIndex);
        }
        else {
            return 0;
        }
    }

    @Override
    public Iterator<ImageTypeSpecifier> getImageTypes(int imageIndex) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getWidth(int imageIndex) throws IOException {
        initReader();
        if (readerStream instanceof JP2Stream) {
            return ((JP2Stream) readerStream).getWidth(imageIndex);
        }
        else if (readerStream instanceof JPCStream) {
            return ((JPCStream) readerStream).getWidth(imageIndex);
        }
        else {
            return 0;
        }
    }

    @Override
    public BufferedImage read(int imageIndex, ImageReadParam param) throws IOException {
        initReader();
        // TODO

        while (readerStream.read() != -1) {
            ;
        }

        return null;
    }

    private void initReader() throws IOException {
        if (readerStream == null) {

            imageInput.mark();

            try {

                byte[] magic = new byte[8];
                imageInput.readFully(magic);

                boolean foundMagic = true;

                // JP2 File
                byte[] jp2Magic = JP2Stream.getMagic();
                for (int i = 0; i < jp2Magic.length; i++) {
                    if (jp2Magic[i] != magic[i]) {
                        foundMagic = false;
                    }
                }
                if (foundMagic) {
                    readerStream = new JP2Stream(imageInput);
                    return;
                }
                else {
                    foundMagic = true;
                }

                // JPEG 2000 codestream
                byte[] jpcMagic = JPCStream.getMagic();
                for (int i = 0; i < jpcMagic.length; i++) {
                    if (jpcMagic[i] != magic[i]) {
                        foundMagic = false;
                    }
                }

                if (foundMagic) {
                    readerStream = new JPCStream(imageInput);
                    return;
                }
                else {
                    throw new IOException("reader not compatible to input");
                }
            }
            finally {
                imageInput.reset();
            }
        }
    }
}
