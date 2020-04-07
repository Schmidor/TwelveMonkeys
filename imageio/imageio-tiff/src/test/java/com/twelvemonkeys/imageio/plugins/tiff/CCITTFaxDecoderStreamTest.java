/*
 * Copyright (c) 2013, Harald Kuhr
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * * Neither the name of the copyright holder nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.twelvemonkeys.imageio.plugins.tiff;

import com.twelvemonkeys.imageio.metadata.tiff.TIFF;
import org.junit.Before;
import org.junit.Test;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;
import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * CCITTFaxDecoderStreamTest
 *
 * @author <a href="mailto:harald.kuhr@gmail.com">Harald Kuhr</a>
 * @author last modified by $Author: haraldk$
 * @version $Id: CCITTFaxDecoderStreamTest.java,v 1.0 09.03.13 14:44 haraldk
 *          Exp$
 */
public class CCITTFaxDecoderStreamTest {

    // group3_1d.tif: EOL|3W|1B|2W|EOL|3W|1B|2W|EOL|3W|1B|2W|EOL|2W|2B|2W|5*F
    static final byte[] DATA_G3_1D = { 0x00, 0x18, 0x4E, 0x00, 0x30, (byte) 0x9C, 0x00, 0x61, 0x38, 0x00, (byte) 0xBE,
            (byte) 0xE0 };

    // group3_1d_fill.tif
    static final byte[] DATA_G3_1D_FILL = { 0x00, 0x01, (byte) 0x84, (byte) 0xE0, 0x01, (byte) 0x84, (byte) 0xE0, 0x01,
            (byte) 0x84, (byte) 0xE0, 0x1, 0x7D, (byte) 0xC0 };

    // group3_1d_premature_eol.tif
    // 0011 0101 | 0000 0010 1011 | 0110 0111 | 0010 1001 | 0100
    // 0W | 59B | 640W | 40W
    static final byte[] DATA_G3_1D_PREMATURE_EOL = { 0x35, 0x02, (byte) 0xB6, 0x72, (byte) 0x94, (byte) 0xE8, 0x74, 0x38, 0x1C, (byte) 0x81, 0x64, (byte) 0xD4, 0x0A, (byte) 0xD9, (byte) 0xD2, 0x27, 0x50, (byte) 0x90, (byte) 0xA6, (byte) 0x87, 0x43, (byte) 0xE3, (byte) 0xE3, (byte) 0x81, (byte) 0xC8, 0x1B, 0x0D, 0x40, (byte) 0xDC, 0x7C, 0x7D, (byte) 0x9C, 0x69, 0x0E, (byte) 0x87, 0x63, (byte) 0xE3, (byte) 0xA8, 0x40, 0x49, (byte) 0xC0, (byte) 0xE4, 0x06, 0x0D, 0x4C, 0x70, 0x71, (byte) 0xC8, 0x70, 0x41, (byte) 0xE1, (byte) 0xE1, (byte) 0xC4, 0x67, 0x52, 0x43, (byte) 0xA0, (byte) 0xD0, (byte) 0xE9, (byte) 0xD0, (byte) 0xE8, 0x7D, (byte) 0xD1, 0x09, (byte) 0xD0, (byte) 0xE8, 0x74, 0x38, 0x1C, (byte) 0x8C, (byte) 0xD4, 0x1C, 0x78, 0x72, 0x1C, 0x1A, (byte) 0x99, (byte) 0xC9, 0x31, (byte) 0xD6, 0x21, (byte) 0xD0, (byte) 0xE9, (byte) 0xD7, (byte) 0xA1, (byte) 0xD0, (byte) 0xE8, 0x74, 0x3A, 0x1D, 0x0E, (byte) 0x87, 0x5C, (byte) 0xA6, 0x04, (byte) 0xD0, 0x67, 0x35, 0x08, 0x38, 0x66, 0x0E, 0x0E, 0x3B, 0x67, 0x0B, 0x58, (byte) 0x87, 0x43, (byte) 0xE3, (byte) 0xA1, (byte) 0xD7, 0x27, 0x5C, (byte) 0x9D, 0x42, 0x74, 0x3E, 0x3A, 0x74, 0x3A, (byte) 0xA1, 0x42, (byte) 0xE5, 0x08, (byte) 0x84, 0x3A, 0x1C, 0x09, (byte) 0xA0, (byte) 0xDC, (byte) 0xD4, 0x16, 0x59, (byte) 0x90, (byte) 0xA8, (byte) 0xA6, (byte) 0x9D, 0x62, 0x74, (byte) 0xE8, 0x74, 0x3A, (byte) 0x9E, 0x3A, (byte) 0xA9, 0x0E, (byte) 0x87, 0x44, (byte) 0xD3, (byte) 0xE3, (byte) 0x81, 0x30, 0x19, (byte) 0xCD, 0x61, (byte) 0xC0, (byte) 0xDD, (byte) 0x98, 0x61, 0x0E, (byte) 0xA8, (byte) 0x87, 0x43, (byte) 0xA5, 0x68, (byte) 0xB1, 0x3A, 0x1D, 0x0E, 0x04, (byte) 0xC0, 0x52, 0x35, 0x05, 0x61, (byte) 0xD8, (byte) 0xF0, (byte) 0xFB, 0x30, 0x49, 0x42, (byte) 0xE4, 0x0D, 0x0B, (byte) 0xA7, 0x43, (byte) 0xA5, 0x08, 0x74, 0x3A, 0x1C, 0x09, (byte) 0x81, (byte) 0x98, 0x6A, 0x0D, 0x23, (byte) 0x85, 0x1C, 0x1D, (byte) 0x98, 0x08, (byte) 0x87, 0x48, 0x43, (byte) 0xA1, (byte) 0xD0, (byte) 0xE8, 0x15, 0x0E, (byte) 0x87, 0x43, (byte) 0xA1, (byte) 0xD2, 0x10, (byte) 0xEA, (byte) 0xB5, (byte) 0x88, 0x70, 0x26, 0x04, 0x35, 0x0D, (byte) 0x83, (byte) 0xB1, (byte) 0xC2, 0x0E, 0x1A, 0x19, (byte) 0x85, (byte) 0x8A, (byte) 0xD8, (byte) 0xE8, 0x74, 0x3A, 0x1D, 0x19, (byte) 0xA1, (byte) 0xD0, (byte) 0xE9, (byte) 0xD1, (byte) 0xD3, (byte) 0xA7, 0x50, (byte) 0xA1, 0x0E, (byte) 0x9D, 0x06, (byte) 0x87, 0x43, (byte) 0xA1, (byte) 0xD0, (byte) 0xF8, (byte) 0xE0, 0x4B, 0x05, 0x23, 0x51, 0x47, 0x21, (byte) 0xC3, 0x58, 0x79, (byte) 0x9A, 0x48, (byte) 0x9D, 0x56, (byte) 0x93, (byte) 0xA7, 0x43, (byte) 0xA1, (byte) 0xD5, (byte) 0xA1, (byte) 0xD2, 0x44, (byte) 0xE8, 0x74, 0x3A, 0x1F, 0x1C, 0x09, 0x60, (byte) 0xA0, (byte) 0xD4, 0x16, 0x19, (byte) 0xA6, 0x68, 0x62, (byte) 0x84, (byte) 0xE9, (byte) 0xD5, 0x10, (byte) 0xE8, 0x74, (byte) 0xE8, 0x74, 0x0A, (byte) 0x9D, 0x0E, (byte) 0x87, 0x02, 0x59, 0x1A, (byte) 0x98, (byte) 0xE0, (byte) 0xA6, (byte) 0xD2, (byte) 0x85, 0x0A, 0x6B, 0x62, (byte) 0xA8, 0x74, 0x38, (byte) 0x83, (byte) 0x81, 0x28, 0x1B, 0x0D, 0x43, 0x68, 0x71, 0x07, (byte) 0xC7, 0x4D, 0x21, 0x45, (byte) 0xB1, 0x54, 0x3A, 0x1C, 0x09, 0x40, (byte) 0xD2, 0x6A, 0x1B, 0x07, 0x0C, (byte) 0xB1, (byte) 0xC5, 0x6A, 0x28, 0x43, (byte) 0xA1, (byte) 0xD0, (byte) 0xF8, (byte) 0xE0, 0x4A, 0x10, (byte) 0xD4, 0x16, 0x1A, (byte) 0x81, 0x68, 0x74, 0x3A, 0x1C, 0x0D, (byte) 0xA0, 0x6E, 0x6A, 0x05, 0x0D, 0x57, 0x4E, (byte) 0x87, 0x43, (byte) 0xC3, (byte) 0xC3, (byte) 0x81, (byte) 0xB4, 0x32, (byte) 0xCD, 0x41, 0x61, (byte) 0xAA, 0x4D, 0x3A, 0x1E, 0x1C, 0x0D, (byte) 0xA2, (byte) 0x9A, (byte) 0x81, (byte) 0xC0, (byte) 0xE9, (byte) 0xAA, 0x0A, (byte) 0x9D, 0x0E, (byte) 0x87, (byte) 0xC7, 0x03, 0x60, 0x12, 0x1A, (byte) 0x86, (byte) 0xD0, (byte) 0xE0, (byte) 0xEC, (byte) 0xBE, (byte) 0x87, (byte) 0xD9, 0x56, (byte) 0x90, (byte) 0xE8, 0x70, 0x36, 0x06, (byte) 0x99, (byte) 0xA9, (byte) 0x8E, 0x0A, 0x4C, (byte) 0xA4, 0x6D, 0x14, 0x43, (byte) 0xA1, (byte) 0xD0, (byte) 0xE0, 0x6C, 0x0C, 0x1A, (byte) 0x83, 0x03, (byte) 0xC3, (byte) 0x86, 0x58, (byte) 0xF0, (byte) 0xFB, 0x2A, 0x13, (byte) 0xED, 0x04, (byte) 0xD0, (byte) 0xE8, 0x74, 0x38, 0x1A, (byte) 0x81, 0x61, (byte) 0xA8, 0x20, (byte) 0xE1, (byte) 0xB4, (byte) 0xCA, 0x1A, 0x1F, 0x68, 0x0A, 0x43, (byte) 0xE3, (byte) 0xE3, (byte) 0x81, (byte) 0xA8, 0x6A, 0x1A, (byte) 0x81, 0x43, 0x2E, (byte) 0x88, 0x79, (byte) 0xA0, (byte) 0xCD, 0x0F, (byte) 0x8E, 0x06, (byte) 0xA0, (byte) 0xDC, (byte) 0xD4, 0x1A, 0x47, 0x06, (byte) 0xE3, (byte) 0xA6, 0x5D, 0x14, 0x6C, (byte) 0xFA, 0x21, (byte) 0xD0, (byte) 0xE8, 0x70, 0x35, 0x66, (byte) 0xA0, (byte) 0xD8, 0x38, 0x61, (byte) 0xAE, 0x4A, (byte) 0x87, 0x43, (byte) 0xE3, (byte) 0x81, (byte) 0xA0, 0x2B, (byte) 0x9A, (byte) 0x83, 0x20, (byte) 0xE4, (byte) 0xD8, 0x42, 0x74, 0x38, 0x1A, 0x06, 0x69, (byte) 0xA8, 0x2A, (byte) 0x8E, 0x11, (byte) 0x94, 0x1A, 0x66, 0x7A, 0x1D, 0x0E, 0x06, (byte) 0x81, 0x0D, 0x40, (byte) 0xE0, 0x74, (byte) 0xD8, 0x0B, 0x43, (byte) 0xA1, (byte) 0xE1, (byte) 0xC0, (byte) 0xCC, 0x09, 0x0D, 0x40, (byte) 0x9D, (byte) 0xB3, 0x68, 0x74, 0x3A, 0x1D, 0x0E, 0x06, 0x60, (byte) 0xD6, 0x6A, 0x04, (byte) 0xED, (byte) 0x95, 0x68, 0x74, 0x3A, 0x1C, 0x0C, (byte) 0xC1, (byte) 0xC0 };

    // group3_2d.tif: EOL|k=1|3W|1B|2W|EOL|k=0|V|V|V|EOL|k=1|3W|1B|2W|EOL|k=0|V-1|V|V|6*F
    static final byte[] DATA_G3_2D = { 0x00, 0x1C, 0x27, 0x00, 0x17, 0x00, 0x1C, 0x27, 0x00, 0x12, (byte) 0xC0 };

    // group3_2d_fill.tif
    static final byte[] DATA_G3_2D_FILL = { 0x00, 0x01, (byte) 0xC2, 0x70, 0x01, 0x70, 0x01, (byte) 0xC2, 0x70, 0x01,
            0x2C };

    static final byte[] DATA_G3_2D_lsb2msb = { 0x00, 0x38, (byte) 0xE4, 0x00, (byte) 0xE8, 0x00, 0x38, (byte) 0xE4,
            0x00, 0x48, 0x03 };

    // group4.tif:
    // Line 1: V-3, V-2, V0
    // Line 2: V0 V0 V0
    // Line 3: V0 V0 V0
    // Line 4: V-1, V0, V0 EOL EOL
    static final byte[] DATA_G4 = { 0x04, 0x17, (byte) 0xF5, (byte) 0x80, 0x08, 0x00, (byte) 0x80 };

    static final byte[] DATA_G4_ALIGNED = {
            0x04, 0x14, // 00000100 000101(00)
            (byte) 0xE0,    // 111 (00000)
            (byte) 0xE0,   // 111 (00000)
            0x58 // 01011 (000)
    };

    // TODO: Better tests (full A4 width scan lines?)

    // From http://www.mikekohn.net/file_formats/tiff.php
    static final byte[] DATA_TYPE_2 = { (byte) 0x84, (byte) 0xe0, // 10000100
                                                                  // 11100000
            (byte) 0x84, (byte) 0xe0, // 10000100 11100000
            (byte) 0x84, (byte) 0xe0, // 10000100 11100000
            (byte) 0x7d, (byte) 0xc0, // 01111101 11000000
    };

    static final byte[] DATA_TYPE_3 = { 0x00, 0x01, (byte) 0xc2, 0x70, // 00000000
                                                                       // 00000001
                                                                       // 11000010
                                                                       // 01110000
            0x00, 0x01, 0x78, // 00000000 00000001 01111000
            0x00, 0x01, 0x78, // 00000000 00000001 01110000
            0x00, 0x01, 0x56, // 00000000 00000001 01010110
            // 0x01, // 00000001

    };

    // 001 00110101 10 000010 1 1 1 1 1 1 1 1 1 1 010 11 (000000 padding)
    static final byte[] DATA_TYPE_4 = { 0x26, // 001 00110
            (byte) 0xb0, // 101 10 000
            0x5f, // 010 1 1 1 1 1
            (byte) 0xfa, // 1 1 1 1 1 010
            (byte) 0xc0 // 11 (000000 padding)
    };

    // 3W|1B|2W| 3W|1B|2W| 3W|1B|2W| 2W|2B|2W
    // 1000|010|0111| 1000|010|0111| 1000|010|0111| 0111|11|0111
    static final byte[] DATA_RLE_UNALIGNED = {
            (byte)0x84, (byte)0xF0, (byte)0x9E,0x13, (byte)0xBE,(byte) 0xE0
    };

    // Image should be (6 x 4):
    // 1 1 1 0 1 1 x x
    // 1 1 1 0 1 1 x x
    // 1 1 1 0 1 1 x x
    // 1 1 0 0 1 1 x x
    final BufferedImage image = new BufferedImage(6, 4, BufferedImage.TYPE_BYTE_BINARY);

    @Before
    public void init() {

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 6; x++) {
                image.setRGB(x, y, x != 3 ? 0xff000000 : 0xffffffff);
            }
        }

        image.setRGB(2, 3, 0xffffffff);
    }

    @Test
    public void testDecodeType2() throws IOException {
        InputStream stream = new CCITTFaxDecoderStream(new ByteArrayInputStream(DATA_TYPE_2), 6,
                TIFFBaseline.COMPRESSION_CCITT_MODIFIED_HUFFMAN_RLE, 1, 0L);

        byte[] imageData = ((DataBufferByte) image.getData().getDataBuffer()).getData();
        byte[] bytes = new byte[imageData.length];
        new DataInputStream(stream).readFully(bytes);
        assertArrayEquals(imageData, bytes);
    }

    @Test
    public void testDecodeType3_1D() throws IOException {
        InputStream stream = new CCITTFaxDecoderStream(new ByteArrayInputStream(DATA_G3_1D), 6,
                TIFFExtension.COMPRESSION_CCITT_T4, 1, 0L);

        byte[] imageData = ((DataBufferByte) image.getData().getDataBuffer()).getData();
        byte[] bytes = new byte[imageData.length];
        new DataInputStream(stream).readFully(bytes);
        assertArrayEquals(imageData, bytes);
    }

    @Test
    public void testDecodeType3_1D_FILL() throws IOException {
        InputStream stream = new CCITTFaxDecoderStream(new ByteArrayInputStream(DATA_G3_1D_FILL), 6,
                TIFFExtension.COMPRESSION_CCITT_T4, 1, TIFFExtension.GROUP3OPT_FILLBITS);

        byte[] imageData = ((DataBufferByte) image.getData().getDataBuffer()).getData();
        byte[] bytes = new byte[imageData.length];
        new DataInputStream(stream).readFully(bytes);
        assertArrayEquals(imageData, bytes);
    }

    @Test
    public void testDecodeType3_1D_premature_EOL() throws IOException {
        InputStream stream = new CCITTFaxDecoderStream(new ByteArrayInputStream(DATA_G3_1D_PREMATURE_EOL), 1700,
                TIFFExtension.COMPRESSION_CCITT_T4, 1, TIFFExtension.GROUP3OPT_FILLBITS);

        int sizeOfArray = 1700*32;
        byte[] bytes = new byte[sizeOfArray];
        new DataInputStream(stream).readFully(bytes);
        boolean foundOne = false;
        int idx = 0;
        while (!foundOne && idx < sizeOfArray)
            if (bytes[idx++] == 1 ){
                foundOne = true;
            }
        assertNotSame(foundOne, false);
    }

    @Test
    public void testDecodeType3_2D() throws IOException {
        InputStream stream = new CCITTFaxDecoderStream(new ByteArrayInputStream(DATA_G3_2D), 6,
                TIFFExtension.COMPRESSION_CCITT_T4, 1, TIFFExtension.GROUP3OPT_2DENCODING);

        byte[] imageData = ((DataBufferByte) image.getData().getDataBuffer()).getData();
        byte[] bytes = new byte[imageData.length];
        new DataInputStream(stream).readFully(bytes);
        assertArrayEquals(imageData, bytes);
    }

    @Test
    public void testDecodeType3_2D_FILL() throws IOException {
        InputStream stream = new CCITTFaxDecoderStream(new ByteArrayInputStream(DATA_G3_2D_FILL), 6,
                TIFFExtension.COMPRESSION_CCITT_T4, 1,
                TIFFExtension.GROUP3OPT_2DENCODING | TIFFExtension.GROUP3OPT_FILLBITS);

        byte[] imageData = ((DataBufferByte) image.getData().getDataBuffer()).getData();
        byte[] bytes = new byte[imageData.length];
        new DataInputStream(stream).readFully(bytes);
        assertArrayEquals(imageData, bytes);
    }

    @Test
    public void testDecodeType3_2D_REVERSED() throws IOException {
        InputStream stream = new CCITTFaxDecoderStream(new ByteArrayInputStream(DATA_G3_2D_lsb2msb), 6,
                TIFFExtension.COMPRESSION_CCITT_T4, 2, TIFFExtension.GROUP3OPT_2DENCODING);

        byte[] imageData = ((DataBufferByte) image.getData().getDataBuffer()).getData();
        byte[] bytes = new byte[imageData.length];
        new DataInputStream(stream).readFully(bytes);
        assertArrayEquals(imageData, bytes);
    }

    @Test
    public void testDecodeType4() throws IOException {
        InputStream stream = new CCITTFaxDecoderStream(new ByteArrayInputStream(DATA_G4), 6,
                TIFFExtension.COMPRESSION_CCITT_T6, 1, 0L);

        byte[] imageData = ((DataBufferByte) image.getData().getDataBuffer()).getData();
        byte[] bytes = new byte[imageData.length];
        new DataInputStream(stream).readFully(bytes);
        assertArrayEquals(imageData, bytes);
    }

    @Test
    public void testDecodeMissingRows() throws IOException {
        // See https://github.com/haraldk/TwelveMonkeys/pull/225 and https://github.com/haraldk/TwelveMonkeys/issues/232
        InputStream inputStream = getClass().getResourceAsStream("/tiff/ccitt_tolessrows.tif");

        // Skip until StripOffsets: 8
        for (int i = 0; i < 8; i++) {
            inputStream.read();
        }

        // Read until StripByteCounts: 7
        byte[] data = new byte[7];
        new DataInputStream(inputStream).readFully(data);

        InputStream stream = new CCITTFaxDecoderStream(new ByteArrayInputStream(data),
                6, TIFFExtension.COMPRESSION_CCITT_T6, 1, 0L);

        byte[] bytes = new byte[6]; // 6 x 6 pixel, 1 bpp => 6 bytes
        new DataInputStream(stream).readFully(bytes);

        // Pad image data with 0s
        byte[] imageData = Arrays.copyOf(((DataBufferByte) image.getData().getDataBuffer()).getData(), 6);
        assertArrayEquals(imageData, bytes);

        // Ideally, we should have no more data now, but the stream don't know that...
        // assertEquals("Should contain no more data", -1, stream.read());
    }

    @Test
    public void testMoreChangesThanColumns() throws IOException {
        // Produces an CCITT Stream with 9 changes on 8 columns.
        byte[] data = new byte[] {(byte) 0b10101010};
        ByteArrayOutputStream imageOutput = new ByteArrayOutputStream();
        OutputStream outputSteam = new CCITTFaxEncoderStream(imageOutput,
                8, 1, TIFFExtension.COMPRESSION_CCITT_T6, 1, 0L);
        outputSteam.write(data);
        outputSteam.close();

        byte[] encoded = imageOutput.toByteArray();
        InputStream inputStream = new CCITTFaxDecoderStream(new ByteArrayInputStream(encoded), 8,
                TIFFExtension.COMPRESSION_CCITT_T6, 1, 0L);
        byte decoded = (byte) inputStream.read();
        assertEquals(data[0], decoded);
    }

    @Test
    public void testMoreChangesThanColumnsFile() throws IOException {
        // See https://github.com/haraldk/TwelveMonkeys/issues/328
        // 26 changes on 24 columns: H0w1b, H1w1b, ..., H1w0b
        InputStream stream = getClass().getResourceAsStream("/tiff/ccitt-too-many-changes.tif");

        // Skip bytes before StripOffsets: 86
        for (int i = 0; i < 86; i++) {
            stream.read();
        }

        InputStream inputStream = new CCITTFaxDecoderStream(stream,
                24, TIFFExtension.COMPRESSION_CCITT_T6, 1, 0L);
        byte decoded = (byte) inputStream.read();
        assertEquals((byte) 0b10101010, decoded);
    }

    @Test
    public void testDecodeType4ByteAligned() throws IOException {
        CCITTFaxDecoderStream stream = new CCITTFaxDecoderStream(new ByteArrayInputStream(DATA_G4_ALIGNED), 6,
                TIFFExtension.COMPRESSION_CCITT_T6, 1, 0L, true);

        byte[] imageData = ((DataBufferByte) image.getData().getDataBuffer()).getData();
        byte[] bytes = new byte[imageData.length];
        new DataInputStream(stream).readFully(bytes);
        assertArrayEquals(imageData, bytes);
    }

    @Test
    public void testDecodeType2NotByteAligned() throws IOException {
        CCITTFaxDecoderStream stream = new CCITTFaxDecoderStream(new ByteArrayInputStream(DATA_RLE_UNALIGNED), 6,
                TIFFBaseline.COMPRESSION_CCITT_MODIFIED_HUFFMAN_RLE, 1, 0L, false);

        byte[] imageData = ((DataBufferByte) image.getData().getDataBuffer()).getData();
        byte[] bytes = new byte[imageData.length];
        new DataInputStream(stream).readFully(bytes);
        assertArrayEquals(imageData, bytes);
    }

    @Test
    public void testG3AOE() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/tiff/ccitt/g3aoe.tif");

        // Skip until StripOffsets: 8
        for (int i = 0; i < 8; i++) {
            inputStream.read();
        }

        // Read until StripByteCounts: 20050
        byte[] data = new byte[20050];
        new DataInputStream(inputStream).readFully(data);

        InputStream stream = new CCITTFaxDecoderStream(new ByteArrayInputStream(data),
                1728, TIFFExtension.COMPRESSION_CCITT_T4, 1, TIFFExtension.GROUP3OPT_FILLBITS);

        byte[] bytes = new byte[216 * 1168]; // 1728 x 1168 pixel, 1 bpp => 216 bytes * 1168
        new DataInputStream(stream).readFully(bytes);
    }
}
