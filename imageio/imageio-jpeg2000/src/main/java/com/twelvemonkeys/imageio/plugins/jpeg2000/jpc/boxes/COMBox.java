package com.twelvemonkeys.imageio.plugins.jpeg2000.jpc.boxes;

import com.twelvemonkeys.imageio.plugins.jpeg2000.jpc.JPCInputStream;

import javax.imageio.stream.ImageInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * COM-Box.
 *
 * @author <a href="mailto:mail@schmidor.de">Oliver Schmidtmer</a>
 * @author last modified by $Author$
 * @version $Id$
 */
public class COMBox extends JPCBox {
    private final int rcom;

    public COMBox(short type, int dataLength, ImageInputStream stream) throws IOException {
        super(type, dataLength);

        rcom = stream.readUnsignedShort();

        byte[] ccom = new byte[dataLength-2];
        stream.readFully(ccom);

        if (JPCInputStream.DEBUG) {
            System.out.print("COMBox [rcom=" + rcom);

            if(rcom == 1){
                // "Latin charset"
                System.out.print(",\"" + new String(ccom, Charset.forName("ISO8859_15")) + "\"");
            } else {
                // 0 == "binary values"
                System.out.print(",\"" + new String(ccom, Charset.forName("ASCII")) + "\"");
            }
            System.out.println(", length=" + ccom.length + "]");

        }
    }
}
