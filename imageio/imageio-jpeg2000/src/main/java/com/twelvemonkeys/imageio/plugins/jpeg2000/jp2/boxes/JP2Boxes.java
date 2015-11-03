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

package com.twelvemonkeys.imageio.plugins.jpeg2000.jp2.boxes;

/**
 * JP2 Box types.
 * <p>Every box is marked by a human readably four char string.</p>
 *
 * @author <a href="mailto:mail@schmidor.de">Oliver Schmidtmer</a>
 * @author last modified by $Author$
 * @version $Id$
 */
public interface JP2Boxes {

    /**
     * JPEG 2000 Signature box 'jP\040\040'
     */
    int SIGNATURE = 0x6A502020;

    int SIGNATURE_CONTENT = 0x0D0A870A;

    /**
     * File Type box
     */
    int FTYP = 0x66747970;

    /**
     * JP2 Header box
     */
    int JP2H = 0x6A703268;

    /**
     * Image Header box
     */
    int IHDR = 0x69686472;

    /**
     * Bits per Component Box
     */
    int BPCC = 0x62706363;

    /**
     * Colour Specification box
     */
    int COLR = 0x636F6C72;

    /**
     * Palette box
     */
    int PCLR = 0x70636C72;

    /**
     * Component Mapping box
     */
    int CMAP = 0x636D6170;

    /**
     * Channel Definition box
     */
    int CDEF = 0x63646566;

    /**
     * Resolution box
     */
    int RES = 0x72657320;

    /**
     * Capture Resolution box
     */
    int RESC = 0x72657363;

    /**
     * Default Display Resolution box
     */
    int RESD = 0x72657364;

    /**
     * Contiguous Codestream box
     */
    int JP2C = 0x6A703263;

    /**
     * Intellectual Property box
     */
    int JP2I = 0x6A703269;

    /**
     * XML box
     */
    int XML = 0x786D6C20;

    /**
     * UUID box
     */
    int UUID = 0x75756964;

    /**
     * UUID Info box
     */
    int UINF = 0x75696E66;

    /**
     * UUID List box
     */
    int ULST = 0x75637374;

    /**
     * URL box
     */
    int URL = 0x75726C20;
}
