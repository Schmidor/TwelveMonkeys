/*
 * Copyright (c) 2015, Harald Kuhr
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

import com.twelvemonkeys.imageio.plugins.jpeg2000.jp2.JP2Stream;
import com.twelvemonkeys.imageio.plugins.jpeg2000.jpc.JPCStream;
import com.twelvemonkeys.imageio.spi.ImageReaderSpiBase;

import javax.imageio.stream.ImageInputStream;
import java.io.IOException;
import java.util.Locale;

/**
 * JPEG2000ImageReaderSpi.
 *
 * @author <a href="mailto:mail@schmidor.de">Oliver Schmidtmer</a>
 * @author last modified by $Author$
 * @version $Id$
 */
public final class JPEG2000ImageReaderSpi extends ImageReaderSpiBase {
    public JPEG2000ImageReaderSpi() {
        super(new JPEG2000ProviderInfo());
    }

    @Override
    public boolean canDecodeInput(final Object source) throws IOException {
        if (!(source instanceof ImageInputStream)) {
            return false;
        }

        ImageInputStream stream = (ImageInputStream) source;

        stream.mark();

        try {

            byte[] magic = new byte[8];
            stream.readFully(magic);

            boolean foundMagic = true;

            // JP2 File
            byte[] jp2Magic = JP2Stream.getMagic();
            for (int i = 0; i < jp2Magic.length; i++) {
                if (jp2Magic[i] != magic[i]) {
                    foundMagic = false;
                }
            }
            if (foundMagic) {
                return true;
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

            return foundMagic;
        }
        finally {
            stream.reset();
        }
    }

    @Override
    public JPEG2000ImageReader createReaderInstance(Object extension) {
        return new JPEG2000ImageReader(this);
    }

    @Override
    public String getDescription(final Locale locale) {
        return "JPEG2000 image reader";
    }
}
