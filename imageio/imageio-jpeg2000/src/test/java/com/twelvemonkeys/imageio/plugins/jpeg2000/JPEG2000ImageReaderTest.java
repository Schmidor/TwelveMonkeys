/*
 * Copyright (c) 2011, Harald Kuhr
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *  Neither the name "TwelveMonkeys" nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
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

import com.twelvemonkeys.imageio.util.ImageReaderAbstractTest;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.internal.matchers.GreaterThan;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.imageio.*;
import javax.imageio.event.IIOReadWarningListener;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataFormatImpl;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.plugins.jpeg.JPEGHuffmanTable;
import javax.imageio.plugins.jpeg.JPEGQTable;
import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.color.ICC_Profile;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.EOFException;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeNoException;
import static org.junit.Assume.assumeNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * JPEGImageReaderTest
 *
 * @author <a href="mailto:harald.kuhr@gmail.com">Harald Kuhr</a>
 * @author last modified by $Author: haraldk$
 * @version $Id: JPEGImageReaderTest.java,v 1.0 24.01.11 22.04 haraldk Exp$
 */
public class JPEG2000ImageReaderTest extends ImageReaderAbstractTest<JPEG2000ImageReader> {

    protected static final JPEG2000ImageReaderSpi SPI = new JPEG2000ImageReaderSpi();

    @Override
    protected List<TestData> getTestData() {
        return Arrays.asList(
                new TestData(getClassLoaderResource("/jp2/Rome.jp2"), new Dimension(640, 480))
        );
    }

    @Override
    protected ImageReaderSpi createProvider() {
        return SPI;
    }

    @Override
    protected JPEG2000ImageReader createReader() {
        try {
            return (JPEG2000ImageReader) SPI.createReaderInstance();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Class<JPEG2000ImageReader> getReaderClass() {
        return JPEG2000ImageReader.class;
    }

    @Override
    protected boolean allowsNullRawImageType() {
        return true;
    }

    @Override
    protected List<String> getFormatNames() {
        return Arrays.asList("JPEG2000", "jpeg2000");
    }

    @Override
    protected List<String> getSuffixes() {
        return Arrays.asList("jp2", "j2k", "jpf", "jpg2", "jpx", "jpc", "j2c");
    }

    @Override
    protected List<String> getMIMETypes() {
        return Arrays.asList("image/jp2", "image/jpx", "image/jpm");
    }

    // TODO: Test that subsampling is actually reading something

    // Special cases found in the wild below

}
