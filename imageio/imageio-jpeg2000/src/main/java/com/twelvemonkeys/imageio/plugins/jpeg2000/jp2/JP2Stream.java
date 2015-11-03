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

package com.twelvemonkeys.imageio.plugins.jpeg2000.jp2;

import com.twelvemonkeys.imageio.plugins.jpeg2000.jp2.boxes.*;
import com.twelvemonkeys.imageio.plugins.jpeg2000.jpc.JPCStream;

import javax.imageio.stream.ImageInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * JP2-Filestream
 *
 * @author <a href="mailto:mail@schmidor.de">Oliver Schmidtmer</a>
 * @author last modified by $Author$
 * @version $Id$
 */
public class JP2Stream extends InputStream {

    public final static boolean DEBUG = "true"
            .equalsIgnoreCase(System.getProperty("com.twelvemonkeys.imageio.plugins.jpeg2000.debug"));

    private SignatureBox signatureBox = null;
    private FTYPBox ftypBox = null;
    private JP2HBox headerBox = null;
    private JP2CBox codestreamBox = null;

    private ImageInputStream inputStream;

    private JPCStream codeStream;

    public static byte[] getMagic() {
        return new byte[] {0x00, 0x00, 0x00, 0x0C, 0x6A, 0x50, 0x20, 0x20};
    }

    public JP2Stream(ImageInputStream inputStream) {
        this.inputStream = inputStream;
    }

    public int getWidth(int index) throws IOException {
        readHeader();
        return (int) headerBox.getIHDR().getWidth();
    }

    public int getHeight(int index) throws IOException {
        readHeader();
        return (int) headerBox.getIHDR().getHeight();
    }

    // TODO? getCompatibleImageType

    @Override
    public int read() throws IOException {
        if (codeStream == null) {
            readToCodestream();
            codeStream = new JPCStream(codestreamBox.getContentStream());
            // TODO: validate image size, component count, ...
        }
        return codeStream.read();
    }

    private void readHeader() throws IOException {
        while (headerBox == null) {
            JP2Box box = JP2Box.readNextBox(inputStream);
            if (signatureBox == null && !(box instanceof SignatureBox)) {
                throw new IOException("stream doesn't start with signature");
            }
            else if (box instanceof SignatureBox) {
                if (signatureBox != null) {
                    throw new IOException("more than one signature box");
                }
                signatureBox = (SignatureBox) box;
                continue;
            }

            if (ftypBox == null && !(box instanceof FTYPBox)) {
                throw new IOException("signature isn't folowed by ftyp box");
            }
            else if (box instanceof FTYPBox) {
                if (ftypBox != null) {
                    throw new IOException("more than one ftyp box");
                }
                ftypBox = (FTYPBox) box;
                continue;
            }

            if (box instanceof JP2HBox) {
                if (headerBox != null) {
                    throw new IOException("more than one header box");
                }

                headerBox = (JP2HBox) box;
                break;
            }

            if (box instanceof JP2CBox && headerBox == null) {
                throw new IOException("codestream before header box");
            }
        }
    }

    private void readToCodestream() throws IOException {
        readHeader();
        while (codestreamBox == null) {
            JP2Box box = JP2Box.readNextBox(inputStream);
            if (box instanceof SignatureBox) {
                throw new IOException("more than one signature box");
            }
            if (box instanceof FTYPBox) {
                throw new IOException("more than one ftyp box");
            }
            if (box instanceof JP2HBox) {
                throw new IOException("more than one header box");
            }

            if (box instanceof JP2CBox) {
                codestreamBox = (JP2CBox) box;
            }
        }
    }
}
