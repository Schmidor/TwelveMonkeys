package com.twelvemonkeys.imageio.plugins.jpeg2000.jpc.boxes;

import com.twelvemonkeys.imageio.plugins.jpeg2000.jpc.JPCInputStream;

import javax.imageio.stream.ImageInputStream;
import java.io.IOException;

/**
 * COD-Box.
 *
 * @author <a href="mailto:mail@schmidor.de">Oliver Schmidtmer</a>
 * @author last modified by $Author$
 * @version $Id$
 */
public class CODBox extends JPCBox {
    /**
     * Coding style for all components
     **/
    private byte scod;

    /**
     * Progression order
     **/
    private byte sgcodProgOrder;

    /**
     * Number of layers
     **/
    private int sgcodLayers;

    /**
     * Multiple component transformation
     **/
    private byte sgcodMulCompTans;

    /**
     * Number of decomposition levels
     **/
    private int spcodDecompLayers;

    /**
     * Code-block width
     **/
    private int spcodCBWidth;

    /**
     * Code-block height
     **/
    private int spcodCBHeight;

    /**
     * Code-block style
     **/
    private byte spcodCBStyle;

    /**
     * Wavelet transformation
     */
    private byte spcodTrans;

    /**
     * Precinct size
     **/
    private byte[] spcodPrecinct;

    public CODBox(short type, int dataLength, ImageInputStream stream) throws IOException {
        super(type, dataLength);

        scod = stream.readByte();

        sgcodProgOrder = stream.readByte();
        sgcodLayers = stream.readUnsignedShort();
        sgcodMulCompTans = stream.readByte();

        spcodDecompLayers = stream.readUnsignedByte();
        spcodCBWidth = stream.readUnsignedByte();
        spcodCBHeight = stream.readUnsignedByte();
        spcodCBStyle = stream.readByte();
        spcodTrans = stream.readByte();

        spcodPrecinct = new byte[dataLength - 10];
        stream.readFully(spcodPrecinct);

        if (JPCInputStream.DEBUG) {
            boolean ent = (scod & 0x01) == 0x01;
            boolean sop = (scod & 0x02) == 0x02;
            boolean eph = (scod & 0x04) == 0x04;
            System.out.println("CODBox [scod=" + (ent ? "E" : "_") + (sop ? "S" : "_") + (eph ? "H" : "_") +
                    ", sgcod=(" + sgcodProgOrder + "," + sgcodLayers + "," + sgcodMulCompTans +
                    "), spcod=(" + spcodDecompLayers + "," + spcodCBWidth + "," + spcodCBHeight + "," + spcodCBStyle + "," + spcodTrans + ", precinct.length=" + spcodPrecinct.length +")]");

        }
    }
}
