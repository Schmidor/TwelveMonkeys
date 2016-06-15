package com.twelvemonkeys.imageio.plugins.jpeg2000.jpc.boxes;

import com.twelvemonkeys.imageio.plugins.jpeg2000.jpc.JPCInputStream;

import javax.imageio.stream.ImageInputStream;
import java.io.IOException;

/**
 * COC-Box.
 *
 * @author <a href="mailto:mail@schmidor.de">Oliver Schmidtmer</a>
 * @author last modified by $Author$
 * @version $Id$
 */
public class COCBox extends JPCBox {

    /**
     * Component index
     **/
    private int ccoc;

    /** Coding style for this component **/
    private byte scoc;

    /**
     * Number of decomposition levels
     **/
    private int spcocDecompLayers;

    /**
     * Code-block width
     **/
    private int spcocCBWidth;

    /**
     * Code-block height
     **/
    private int spcocCBHeight;

    /**
     * Code-block style
     **/
    private byte spcocCBStyle;

    /**
     * Wavelet transformation
     */
    private byte spcocTrans;

    /**
     * Precinct size
     **/
    private byte[] spcocPrecinct;

    public COCBox(short type, int dataLength, ImageInputStream stream, int Csiz) throws IOException {
        super(type, dataLength);


        ccoc = (Csiz < 257)? stream.readUnsignedByte() : stream.readUnsignedShort();
        scoc = stream.readByte();

        spcocDecompLayers = stream.readUnsignedByte();
        spcocCBWidth = stream.readUnsignedByte();
        spcocCBHeight = stream.readUnsignedByte();
        spcocCBStyle = stream.readByte();
        spcocTrans = stream.readByte();

        spcocPrecinct = new byte[dataLength - ((Csiz < 257)? 7 : 8)];
        stream.readFully(spcocPrecinct);

        if (JPCInputStream.DEBUG) {
            System.out.println("COCBox [ccoc=" + ccoc +
                    ", scoc=" + scoc +
                    ", spcod=(" + spcocDecompLayers + "," + spcocCBWidth + "," + spcocCBHeight + "," + spcocCBStyle + "," + spcocTrans + ", precinct.length=" + spcocPrecinct.length +")]");

        }
    }

    public int getCcoc() {
        return ccoc;
    }

    public void setCcoc(int ccoc) {
        this.ccoc = ccoc;
    }
}
