package com.rihao.property.util;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.DocumentFamily;
import com.artofsolving.jodconverter.DocumentFormat;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * doc docx等格式转换
 */
public class DocConverter {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static int environment = 1;// 环境 1：windows 2:linux
    private File pdfFile;
    private File swfFile;
    private File sourceFile; // 需要转换的文件
    private String suffix; //文件后缀
    private String sToolsPath;
    private boolean needSwf; // 是否需要转换为swf

    static {
        String os = System.getProperty("os.name");
        if (os.startsWith("Windows")) {
            environment = 1;
        } else if (os.startsWith("Linux")) {
            environment = 2;
        }
    }

    public DocConverter(String filePath, String fileName, String suffix, boolean needSwf,String sToolsPath) {
        // 自动补全斜杠
        if(StringUtils.isBlank(sToolsPath)){
            sToolsPath = "/";
        }
        if (!sToolsPath.endsWith("/")) {
            sToolsPath = sToolsPath + "/";
        }
        ini(filePath, fileName, suffix, needSwf);
    }

    /**
     * 初始化
     */
    private void ini(String filePath, String fileName, String suffix, boolean needSwf) {
        this.suffix = suffix;
        this.needSwf = needSwf;
        sourceFile = new File(filePath + "/" + fileName);
        pdfFile = new File(filePath + "/" + fileName + ".pdf");
        swfFile = new File(filePath + "/" + fileName + ".swf");
    }

    /**
     * 通过openOffice将doc转化为pdf
     *
     * @throws Exception
     */
    private void doc2pdfWithOpenOffice() throws Exception {
        if (sourceFile.exists()) {
            String path = this.sourceFile.getPath();
            long startTime = System.currentTimeMillis();
            logger.info("OpenOffice转换开始：" + sourceFile);

            OpenOfficeConnection connection = new SocketOpenOfficeConnection(8100);
            try {

                if ("txt".equalsIgnoreCase(suffix)) {
                    // TXT文件需要转码
                    File file = FileUtils.fileEncodingToANSI(this.sourceFile, path + ".txt");
                    this.sourceFile = file;
                }
                connection.connect();
                DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
//                DocumentConverter converter = new StreamOpenOfficeDocumentConverter(connection);
                converter.convert(sourceFile, getInputDocumentFormat(), pdfFile, getOutDocumentFormat());
            } catch (java.net.ConnectException e) {
                logger.info(e.getMessage());
            } catch (Exception e) {
                logger.info(e.getMessage());
                throw e;
            } finally {
                connection.disconnect();
                if ("txt".equalsIgnoreCase(suffix)) {
                    // 转码完成后删除临时文件
                    if(this.sourceFile.exists()){
                        this.sourceFile.delete();
                        this.sourceFile = new File(path);
                    }
                }
            }
            long endTime = System.currentTimeMillis();
            logger.info("OpenOffice转换结束：" + sourceFile + "，耗时：" + (endTime - startTime) + "毫秒");
        }
    }

    /**
     * 通过LibreOffice将doc转化为pdf
     *
     * @throws Exception
     */
    private void doc2pdfWithLibreOffice() throws Exception {
        if (sourceFile.exists()) {
            if (!pdfFile.exists()) {
                long startTime = System.currentTimeMillis();
                logger.info("LibreOffice转换开始：" + sourceFile);

                try {
                    String osName = System.getProperty("os.name");
                    logger.info("服务器类型：" + osName);
                    String command = "";
                    if (osName.contains("Windows")) {
                        command = "soffice --convert-to pdf  -outdir " + pdfFile.getParent() + " " + sourceFile;
//                        command = "cmd /c start soffice --headless --invisible --convert-to pdf  -outdir " + pdfFile.getParent() + " " + sourceFile;
                    } else {
                        command = "libreoffice6.1 --convert-to pdf --outdir " + pdfFile.getParent() + " " + sourceFile;
                    }
                    logger.info("转换命令：" + command);
                    String result = CommandExecute.execute(30000, command);
                    if (result.equals("") || result.contains("writer_pdf_Export")) {
                        logger.info("转换成功：" + result);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.info("转换报错：" + e.getMessage());
                    throw e;
                }
                long endTime = System.currentTimeMillis();
                logger.info("LibreOffice转换结束：" + sourceFile + "，耗时：" + (endTime - startTime) + "毫秒");
            }
        }
    }


    /**
     * 设置输入文件DocumentFormat(doc/docx/xls/xlsx/ppt/pptx)
     *
     * @return
     */
    private DocumentFormat getInputDocumentFormat() throws Exception{
        DocumentFamily family = null;
        String mimeType = "";
        String exportFilter = "";
        if ("doc".equalsIgnoreCase(suffix)) {
            family = DocumentFamily.TEXT;
            mimeType = "application/msword";
            exportFilter = "MS Word 97";
        } else if ("docx".equalsIgnoreCase(suffix)) {
            family = DocumentFamily.TEXT;
            mimeType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        } else if ("xls".equalsIgnoreCase(suffix)) {
            family = DocumentFamily.SPREADSHEET;
            mimeType = "application/vnd.ms-excel";
            exportFilter = "MS Excel 97";
        } else if ("xlsx".equalsIgnoreCase(suffix)) {
            family = DocumentFamily.SPREADSHEET;
            mimeType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        } else if ("ppt".equalsIgnoreCase(suffix)) {
            family = DocumentFamily.PRESENTATION;
            mimeType = "application/vnd.ms-powerpoint";
            exportFilter = "MS PowerPoint 97";
        } else if ("pptx".equalsIgnoreCase(suffix)) {
            family = DocumentFamily.PRESENTATION;
            mimeType = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
        } else if ("txt".equalsIgnoreCase(suffix)) {
            family = DocumentFamily.TEXT;
            mimeType = "text/plain";
        } else if ("wps".equalsIgnoreCase(suffix)) {
            family = DocumentFamily.TEXT;
            mimeType = "application/kswps";
        }
        DocumentFormat docFormat = new DocumentFormat(sourceFile.getName(), family, mimeType, suffix);
        if ("doc".equalsIgnoreCase(suffix) || "xls".equalsIgnoreCase(suffix) || "ppt".equalsIgnoreCase(suffix)) {
            docFormat.setExportFilter(family, exportFilter);
        }
        return docFormat;
    }

    /**
     * 设置输出文件DocumentFormat(pdf)
     *
     * @return
     */
    private DocumentFormat getOutDocumentFormat() {
        DocumentFormat pdfFormat = new DocumentFormat(pdfFile.getName(), "application/pdf", ".pdf");
        pdfFormat.setExportFilter(DocumentFamily.DRAWING, "draw_pdf_Export");
        pdfFormat.setExportFilter(DocumentFamily.PRESENTATION, "impress_pdf_Export");
        pdfFormat.setExportFilter(DocumentFamily.SPREADSHEET, "calc_pdf_Export");
        pdfFormat.setExportFilter(DocumentFamily.TEXT, "writer_pdf_Export");
        return pdfFormat;
    }

    /**
     * doc转换成pdf后，将pdf转换成swf
     *
     * @throws Exception
     */
    private synchronized void doc2pdf2swf() throws Exception {
        doPdf2swf(pdfFile);
        // 使用pdfjs插件直接预览pdf文件，不再删除pdf文件
//        if(pdfFile.exists()){
//            pdfFile.delete();
//        }
    }

    /**
     * pdf直接转换为swf
     *
     * @throws Exception
     */
    public synchronized void pdf2swf() throws Exception {
        doPdf2swf(sourceFile);
    }

    public synchronized void doPdf2swf(File thisSourceFile) throws Exception {
        if (!swfFile.exists()) {
            if (thisSourceFile.exists()) {
                String cmd = null;
                if (environment == 1) {
                    // windows环境命令
                    cmd = sToolsPath + "pdf2swf.exe" + " " + thisSourceFile.getPath().trim() + " -o " + swfFile.getPath() + " -T 9 -s zoom=144 -f -s poly2bitmap -s languagedir=" + sToolsPath + "xpdf-chinese-simplified";
                } else {
                    if (environment == 2) {
                        // linux环境命令
                        cmd = sToolsPath + "pdf2swf" + " " + thisSourceFile.getPath().trim() + " -o " + swfFile.getPath() + " -T 9 -s zoom=144 -f -s poly2bitmap -s languagedir=" + sToolsPath + "xpdf-chinese-simplified";
                    }
                }

                Process pro;
                if (StringUtils.isNotBlank(cmd)) {
                    try {
                        pro = Runtime.getRuntime().exec(cmd);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    }
                    new DoOutput(pro.getInputStream()).start();
                    new DoOutput(pro.getErrorStream()).start();
                    try {
                        //调用waitFor方法，是为了阻塞当前进程，直到cmd执行完
                        pro.waitFor();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 多线程内部类
     * 读取转换时cmd进程的标准输出流和错误输出流，这样做是因为如果不读取流，进程将死锁
     *
     * @author iori
     */
    private static class DoOutput extends Thread {
        public InputStream is;

        //构造方法
        public DoOutput(InputStream is) {
            this.is = is;
        }

        @Override
        public void run() {
            BufferedReader br = new BufferedReader(new InputStreamReader(this.is));
            String str = null;
            try {
                //这里并没有对流的内容进行处理，只是读了一遍
                while ((str = br.readLine()) != null)
                    ;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 转换成 swf
     */
    private void jpg2swf(String suffix) throws Exception {
        Runtime r = Runtime.getRuntime();
        if (!swfFile.exists()) {
            String c = suffix;
            if ("jpg".equalsIgnoreCase(suffix)) {
                c = "jpeg";
            }
            if (environment == 1) {// windows环境处理
                try {
                    String jPath = sToolsPath + c + "2swf.exe";
                    Process p = null;
                    if (c.equalsIgnoreCase("gif")) {
                        p = r.exec(jPath + " " + sourceFile.getPath().trim() + " -o " + swfFile.getPath() + " -r 25");
                    } else if (c.equalsIgnoreCase("png")) {
                        p = r.exec(jPath + " " + sourceFile.getPath().trim() + " -o " + swfFile.getPath() + " -T 9");
                    } else {
                        p = r.exec(jPath + " " + sourceFile.getPath().trim() + " -o " + swfFile.getPath() + " -T 9");
                    }
                    loadStream(p.getInputStream());
                    loadStream(p.getErrorStream());
                    loadStream(p.getInputStream());
                } catch (IOException e) {
                    throw e;
                } catch (Exception e) {
                    throw e;
                }
            } else if (environment == 2) {// linux环境处理
                try {
                    Process p = null;
                    if (c.equalsIgnoreCase("gif")) {
                        p = r.exec(c + "2swf " + sourceFile.getPath().trim() + " -o " + swfFile.getPath() + " -r 25");
                    } else if (c.equalsIgnoreCase("png")) {
                        p = r.exec(c + "2swf " + sourceFile.getPath().trim() + " -o " + swfFile.getPath() + " -T 9");
                    } else {
                        p = r.exec(c + "2swf " + sourceFile.getPath().trim() + " -o " + swfFile.getPath() + " -T 9 -f");
                    }
                    loadStream(p.getInputStream());
                    loadStream(p.getErrorStream());
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        }
    }

    static String loadStream(InputStream in) throws IOException {

        int ptr = 0;
        in = new BufferedInputStream(in);
        StringBuffer buffer = new StringBuffer();

        while ((ptr = in.read()) != -1) {
            buffer.append((char) ptr);
        }

        return buffer.toString();
    }

    /**
     * 转换主方法
     */
    public boolean conver() throws Exception {
        if (swfFile.exists() && needSwf) {
            return true;
        }
        if (pdfFile.exists() && !needSwf) {
            return true;
        }
//        if("jpg".equalsIgnoreCase(suffix)||"jpeg".equalsIgnoreCase(suffix)||"gif".equalsIgnoreCase(suffix)||"png".equalsIgnoreCase(suffix)){
//            jpg2swf(suffix);
//        } else
        if ("pdf".equalsIgnoreCase(suffix)) {
            if (needSwf) {
                pdf2swf();
            }
        } else {
//            if (StringUtils.isBlank(GlobalConfigUtils.getGlobalConfig().getDoc2PdfType()) || "1".equalsIgnoreCase(GlobalConfigUtils.getGlobalConfig().getDoc2PdfType())) {
//                // 通过OpenOffice将doc转化为pdf
//                doc2pdfWithOpenOffice();
//            } else {
                // 通过LibreOffice将doc转化为pdf
                doc2pdfWithLibreOffice();
//            }
            if (needSwf) {
                // 将pdf转换成swf
                doc2pdf2swf();
            }
        }
        return swfFile.exists();
    }

}
