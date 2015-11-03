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

/**
 * JP200 codestream markers and boxes.
 *
 * @author <a href="mailto:mail@schmidor.de">Oliver Schmidtmer</a>
 * @author last modified by $Author$
 * @version $Id$
 */
public interface JPCBoxes {

    /**
     * Start of codestream
     */
    short SOC = (short) 0xFF4F;

    /**
     * Start of tile-part
     */
    short SOT = (short) 0xFF90;

    /**
     * Start of data
     */
    short SOD = (short) 0xFF93;

    /**
     * End of codestream
     */
    short EOC = (short) 0xFFD9;

    /**
     * Image and tile size
     */
    short SIZ = (short) 0xFF51;

    /**
     * Coding style default
     */
    short COD = (short) 0xFF52;

    /**
     * Coding style component
     */
    short COC = (short) 0xFF53;

    /**
     * Region-of-interest
     */
    short RGN = (short) 0xFF5E;

    /**
     * Quantization default
     */
    short QCD = (short) 0xFF5C;

    /**
     * Quantization component
     */
    short QCC = (short) 0xFF5D;

    /**
     * Progression order change
     */
    short POC = (short) 0xFF5F;

    /**
     * Tile-part lengths
     */
    short PLM = (short) 0xFF57;

    /**
     * Packet length, main header
     */
    short PLT = (short) 0xFF58;

    /**
     * Packet length, tile-part header
     */
    short PPM = (short) 0xFF60;

    /**
     * Packed packet headers, main header
     */
    short PPT = (short) 0xFF61;

    /**
     * Packed packet headers, tile-part header
     */
    short TLM = (short) 0xFF55;

    /**
     * Start of packet
     */
    short SOP = (short) 0xFF91;

    /**
     * End of packet header
     */
    short EPH = (short) 0xFF92;

    /**
     * Component registration
     */
    short CRG = (short) 0xFF63;

    /**
     * Comment
     */
    short COM = (short) 0xFF64;
}
