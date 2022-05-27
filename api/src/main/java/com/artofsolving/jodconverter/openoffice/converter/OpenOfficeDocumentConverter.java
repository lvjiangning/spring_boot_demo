//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.artofsolving.jodconverter.openoffice.converter;

import com.artofsolving.jodconverter.DocumentFormat;
import com.artofsolving.jodconverter.DocumentFormatRegistry;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeException;
import com.sun.star.beans.XPropertySet;
import com.sun.star.document.RedlineDisplayType;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XStorable;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.XComponent;
import com.sun.star.task.ErrorCodeIOException;
import com.sun.star.ucb.XFileIdentifierConverter;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.util.CloseVetoException;
import com.sun.star.util.XCloseable;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class OpenOfficeDocumentConverter extends AbstractOpenOfficeDocumentConverter {
    private static final Logger logger = LoggerFactory.getLogger(OpenOfficeDocumentConverter.class);

    public OpenOfficeDocumentConverter(OpenOfficeConnection connection) {
        super(connection);
    }

    public OpenOfficeDocumentConverter(OpenOfficeConnection connection, DocumentFormatRegistry formatRegistry) {
        super(connection, formatRegistry);
    }

    protected void convertInternal(InputStream inputStream, DocumentFormat inputFormat, OutputStream outputStream, DocumentFormat outputFormat) {
        File inputFile = null;
        File outputFile = null;

        try {
            inputFile = File.createTempFile("document", "." + inputFormat.getFileExtension());
            FileOutputStream inputFileStream = null;

            try {
                inputFileStream = new FileOutputStream(inputFile);
                IOUtils.copy(inputStream, inputFileStream);
            } finally {
                IOUtils.closeQuietly(inputFileStream);
            }

            outputFile = File.createTempFile("document", "." + outputFormat.getFileExtension());
            this.convert(inputFile, inputFormat, outputFile, outputFormat);
            FileInputStream outputFileStream = null;

            try {
                outputFileStream = new FileInputStream(outputFile);
                IOUtils.copy(outputFileStream, outputStream);
            } finally {
                IOUtils.closeQuietly(outputFileStream);
            }
        } catch (IOException var25) {
            logger.error(var25.getMessage(), var25);
            throw new OpenOfficeException("conversion failed", var25);
        } finally {
            if (inputFile != null) {
                inputFile.delete();
            }

            if (outputFile != null) {
                outputFile.delete();
            }

        }

    }

    protected void convertInternal(File inputFile, DocumentFormat inputFormat, File outputFile, DocumentFormat outputFormat) {
        Map loadProperties = new HashMap();
        loadProperties.putAll(this.getDefaultLoadProperties());
        loadProperties.putAll(inputFormat.getImportOptions());
        Map storeProperties = outputFormat.getExportOptions(inputFormat.getFamily());
        OpenOfficeConnection var7 = this.openOfficeConnection;
        synchronized (this.openOfficeConnection) {
            XFileIdentifierConverter fileContentProvider = this.openOfficeConnection.getFileContentProvider();
            String inputUrl = fileContentProvider.getFileURLFromSystemPath("", inputFile.getAbsolutePath());
            String outputUrl = fileContentProvider.getFileURLFromSystemPath("", outputFile.getAbsolutePath());
            this.loadAndExport(inputUrl, loadProperties, outputUrl, storeProperties);
        }
    }

    private void loadAndExport(String inputUrl, Map loadProperties, String outputUrl, Map storeProperties) throws OpenOfficeException {
        XComponent document;
        try {
            document = this.loadDocument(inputUrl, loadProperties);
            XPropertySet mxDocProps = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, document);
            mxDocProps.setPropertyValue("RedlineDisplayType", RedlineDisplayType.NONE);
        } catch (ErrorCodeIOException var9) {
            logger.error(var9.getMessage(), var9);
            throw new OpenOfficeException("conversion failed: could not load input document; OOo errorCode: " + var9.ErrCode, var9);
        } catch (Exception var10) {
            logger.error(var10.getMessage(), var10);
            throw new OpenOfficeException("conversion failed: could not load input document", var10);
        }

        if (document == null) {
            throw new OpenOfficeException("conversion failed: could not load input document");
        } else {
            this.refreshDocument(document);

            try {
                this.storeDocument(document, outputUrl, storeProperties);
            } catch (ErrorCodeIOException var7) {
                logger.error(var7.getMessage(), var7);
                throw new OpenOfficeException("conversion failed: could not save output document; OOo errorCode: " + var7.ErrCode, var7);
            } catch (Exception var8) {
                logger.error(var8.getMessage(), var8);
                throw new OpenOfficeException("conversion failed: could not save output document", var8);
            }
        }
    }

    private XComponent loadDocument(String inputUrl, Map loadProperties) throws com.sun.star.io.IOException, IllegalArgumentException, IllegalArgumentException {
        XComponentLoader desktop = this.openOfficeConnection.getDesktop();
        return desktop.loadComponentFromURL(inputUrl, "_blank", 0, toPropertyValues(loadProperties));
    }

    private void storeDocument(XComponent document, String outputUrl, Map storeProperties) throws com.sun.star.io.IOException {
        boolean var12 = false;

        try {
            var12 = true;
            XStorable storable = (XStorable) UnoRuntime.queryInterface(XStorable.class, document);
            storable.storeToURL(outputUrl, toPropertyValues(storeProperties));
            var12 = false;
        } finally {
            if (var12) {
                XCloseable closeable = (XCloseable) UnoRuntime.queryInterface(XCloseable.class, document);
                if (closeable != null) {
                    try {
                        closeable.close(true);
                    } catch (CloseVetoException var13) {
                        logger.error(var13.getMessage(), var13);
                        logger.warn("document.close() vetoed");
                    }
                } else {
                    document.dispose();
                }

            }
        }

        XCloseable closeable = (XCloseable) UnoRuntime.queryInterface(XCloseable.class, document);
        if (closeable != null) {
            try {
                closeable.close(true);
            } catch (CloseVetoException var14) {
                logger.error(var14.getMessage(), var14);
                logger.warn("document.close() vetoed");
            }
        } else {
            document.dispose();
        }

    }
}
