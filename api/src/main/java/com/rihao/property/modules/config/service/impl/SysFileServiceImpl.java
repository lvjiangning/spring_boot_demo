package com.rihao.property.modules.config.service.impl;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.rihao.property.modules.config.entity.SysConfig;
import com.rihao.property.modules.config.entity.SysFile;
import com.rihao.property.modules.config.mapper.SysFileMapper;
import com.rihao.property.modules.config.service.ISysFileService;
import com.rihao.property.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static java.util.stream.Collectors.joining;

@Service
@Slf4j
public class SysFileServiceImpl extends ServiceImpl<SysFileMapper, SysFile> implements ISysFileService {

    @Value("${file.upload.path}")
    private  String FILE_UPLOAD_PATH;
    @Value("${enableFileRemoteService}")
    private  Boolean enableFileRemoteService;
    @Value("${swftoolsSetupDir}")
    private  String swftoolsSetupDir;

    @Override
    public List<SysFile> upload(MultipartFile file, HttpServletRequest request, Long businessId,Boolean isOnly,Long type) throws IOException {
       //附件的业务类型
        if(type == null){
            return null;
        }
        //如果使用本地
        List<SysFile> list = new ArrayList<>();
        SysFile dbFile = new SysFile();
        //去除当前业务单据有关附件关联信息
        if (isOnly !=null && isOnly && businessId !=null){
            deleteByBusinessId(null,businessId,type);
        }
        if (org.apache.commons.lang3.StringUtils.isNotBlank(file.getOriginalFilename())) {
            dbFile.setFileSize((double) file.getSize());
            dbFile.setFileType(file.getContentType());
            dbFile.setType(type);
//                if (CollectionUtils.isNotEmpty(businessId)) {
            dbFile.setBusinessId(businessId);
//                }
            //使用本地服务
            createFile(dbFile, list, request, file);
        }


        return list;
    }

    @Transactional(readOnly = false)
    public void save(List<SysFile> sysFiles) {
        if (CollectionUtils.isNotEmpty(sysFiles)) {
            for (SysFile sysFile : sysFiles) {
                if (sysFile != null) {
                    save(sysFile);
                }
            }
        }
    }


    public List<SysFile> findFileList(List<String> businessIdIds) {
        Map<String, Object> map = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(businessIdIds)) {
            map.put("businessIdIds", businessIdIds);
        }
        map.put("DEL_FLAG_NORMAL", "0");
        return null;
    }


    //附件预览
    @Transactional(readOnly = false)
    public String viewFile(SysFile sysFile, String suffix, boolean needSwf, HttpServletRequest request, HttpServletResponse response) {
        if (sysFile.getFileSize() > 1024 * 1024 * 100 && !"pdf".equals(suffix)) {
            String classpath = this.getClass().getResource("/").getPath().replaceFirst("/", "");
            String path = classpath.replace("WEB-INF/classes/", "upload/");
            String filePath = path + "pdf/model2";
            sysFile.setFilePath(filePath);
            return "【预览文件失败】：文件大于100M，暂不支持在线预览";
        } else {
            if (!enableFileRemoteService) {
                String newPath = Encodes.urlDecode(sysFile.getFilePath());
                File file = new File(newPath);
                if (!file.exists() || suffix.isEmpty()) {
                    return null;
                }
                return localTransForm(file, suffix, needSwf, request, response);
            } else {
                return null;
//                return remoteFileService.fileView(sysFile, needSwf);
            }
        }
    }

    //本地附件预览
    @Transactional(readOnly = false)
    public String localTransForm(File file, String suffix, boolean needSwf, HttpServletRequest request, HttpServletResponse response) {
        String filePath = file.getParent();
        String fileName = file.getName();
        String result = "";
        //图片预览不需要预览插件
        if ("jpg".equalsIgnoreCase(suffix) || "jpeg".equalsIgnoreCase(suffix) || "gif".equalsIgnoreCase(suffix) || "png".equalsIgnoreCase(suffix) || "bmp".equalsIgnoreCase(suffix)) {
        } else {
            DocConverter d = new DocConverter(filePath, fileName, suffix, needSwf, swftoolsSetupDir);
            try {
                d.conver();
            } catch (com.artofsolving.jodconverter.openoffice.connection.OpenOfficeException e) {
                e.printStackTrace();
                result = "【预览文件失败】：当前文件不支持在线预览，请尝试将文件“另存为”后，重新上传!";
            } catch (NullPointerException e) {
                e.printStackTrace();
                result = "【预览文件失败】：未启动预览插件服务，请联系管理员!";
            } catch (Exception e) {
                e.printStackTrace();
                String message = "未知错误，请联系管理员！";
                if (StringUtils.isNotBlank(e.getMessage())) {
                    message = e.getMessage();
                } else if (e.getCause() != null && StringUtils.isNotBlank(e.getCause().getMessage())) {
                    message = e.getCause().getMessage();
                }
                // 转中文双引号
                message = message.replaceAll("\"", "”");
                result = "【预览文件失败】：" + message;
            }
        }
        return result;
    }

    public void showImage(SysFile sysFile, HttpServletRequest request, HttpServletResponse response) throws IOException {
        InputStream fis = null;
        //没有启用附件服务器
        if (!enableFileRemoteService) {
            String newPath = Encodes.urlDecode(sysFile.getFilePath());
            File file = new File(newPath);
            if (!file.exists()) {
                return;
            }
            fis = new FileInputStream(file);
        } else {
//            fis = remoteFileService.downloadFileInputStrem(sysFile, true, false);
//            if (fis == null) {
//                return;
//            }
        }
        response.setContentType("image/jpeg");
        OutputStream os = response.getOutputStream();
        try {
            int count;
            byte[] buffer = new byte[1024 * 1024];
            while ((count = fis.read(buffer)) != -1)
                os.write(buffer, 0, count);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null)
                os.close();
            if (fis != null)
                fis.close();
        }
    }

    /**
     * 获取swf文件流
     *
     * @param response
     * @return
     * @throws Exception
     */
    public void getSwfFile(SysFile sysFile, HttpServletResponse response) throws Exception {
        getFile(sysFile, response, ".swf");
    }

    /**
     * 获取文件流
     *
     * @param type 文件后缀
     * @return
     * @throws Exception
     */
    public void getFile(SysFile sysFile, HttpServletResponse response, String type) throws Exception {
        OutputStream os = null;
        InputStream in = null;
        if (sysFile.getFileSize() > 1024 * 1024 * 100 && StringUtils.isNotBlank(type)) {
            // 文件大于100M，暂不支持在线预览 （type为空表示取原文件(pdf)，不涉及转码）
            String classpath = this.getClass().getResource("/").getPath().replaceFirst("/", "");
            String path = classpath.replace("WEB-INF/classes/", "upload/");
            String filePath = path + type.replace(".", "") + "/model2" + type;
            File file = new File(filePath);
            if (file.exists()) {
                in = new FileInputStream(file);
            }
        } else {
            if (!enableFileRemoteService) {
                String filePath = sysFile.getFilePath();
                if (StringUtils.isNotBlank(type)) {
                    filePath += type;
                }
                File file = new File(filePath);
                if (file.exists()) {
                    in = new FileInputStream(file);
                }
            } else {
//                in = remoteFileService.downloadFileInputStrem(sysFile, true, ".swf".equals(type));
            }
        }
        response.setContentType("application/x-shockwave-flash");     //设置返回内容格式
        //用该文件创建一个输入流
        os = response.getOutputStream();  //创建输出流
        IOUtils.copy(in, os);
        IOUtils.closeQuietly(in);
        IOUtils.closeQuietly(os);
        response.flushBuffer();

    }


    // 获取文件上传路径 本地服务器调用
    public String getUploadPath(HttpServletRequest request) throws IOException {
        log.info("FILE_UPLOAD_PATH; :" + FILE_UPLOAD_PATH);
        if (StringUtils.isBlank(FILE_UPLOAD_PATH)) {
            FILE_UPLOAD_PATH = request.getSession().getServletContext().getRealPath("/file/");
        }
        String fullPath = this.getRemoteUploadPath(FILE_UPLOAD_PATH);
        log.info("[getUploadPath] this.getRemoteUploadPath(fileUploadPath) :" + fullPath);
        File savePath = new File(fullPath);
        if (!savePath.isDirectory()) {
            FileUtils.forceMkdir(savePath);
        }
        return fullPath;
    }

    /**
     * 得到上传的文件夹，远程服务器专用
     *
     * @return
     */
    public String getRemoteUploadPath(String path) {
        String fileUploadPath = StringUtils.isBlank(path) ? "" : path;
        ;
        if (!fileUploadPath.endsWith("/")) {
            fileUploadPath = fileUploadPath + "/";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String fullPath = fileUploadPath + sdf.format(new Date()) + "/";

        fullPath = fullPath.replaceAll("\\\\", "/");
        return fullPath;
    }


//    /**
//     * 批量删除
//     * fileIds:主键list
//     */
//    @Transactional(readOnly = false)
//    public void batchDelete(List<String> fileIds) {
//        if (CollectionUtils.isNotEmpty(fileIds)) {
//            Map<String, Object> map = Maps.newHashMap();
//            map.put("list", fileIds);
//            map.put("updateDate", new Date());
//            map.put("DEL_FLAG_DELETE", "1");
//            dao.batchDelete(map);
//        }
//    }


    @Transactional(readOnly = false)
    public void updateBusinessIds(List<String> businessIds, List<String> fileIds) {
//        if (CollectionUtils.isNotEmpty(fileIds) && CollectionUtils.isNotEmpty(businessIds)) {
//            SysFile param = new SysFile();
//            param.getSqlMap().put("fileIds", fileIds.parallelStream().collect(joining("','", "'", "'")));
//            List<SysFile> sysFiles = this.findList(param);
//            List<SysFile> insertList = Lists.newArrayList();
//            String[] ignoreProperties = {"remarks", "createBy", "createDate", "updateBy", "updateDate", "delFlag", "accountSuitId", "id", "currentUser", "page", "sqlMap", "isNewRecord"};
//            businessIds.forEach(businessId -> {
//                sysFiles.forEach(dbSysFile -> {
//                    SysFile sysFile = new SysFile();
//                    BeanUtils.copyProperties(dbSysFile, sysFile, ignoreProperties);
//                    sysFile.preInsert();
//                    sysFile.setBusinessId(businessId);
//                    insertList.add(sysFile);
//                });
//            });
//            this.batchInsert(insertList);
//            this.batchDelete(fileIds);
//        }
    }

    @Transactional(readOnly = false)
    public void batchUpdate(List<String> list, SysFile sysFile, String businessId) {
//        sysFile.setAccountSuitId(AccountSuitUtils.getAccountSuit().getId());
//        sysFile.setUpdateDate(new Date());
//        dao.batchUpdate(list, sysFile, businessId);
    }

//    @Transactional(readOnly = false)
//    public String getBusinessName(String businessName) {
//        return dao.getBusinessName(businessName);
//    }

    @Transactional(readOnly = false)
    public List<SysFile> findImagePath(SysFile sysFile) {
//        Map<String, Object> map = Maps.newHashMap();
//        if (StringUtils.isNotBlank(sysFile.getId())) {
//            map.put("id", sysFile.getId());
//        }
//        map.put("DEL_FLAG_NORMAL", BaseEntity.DEL_FLAG_NORMAL);
//        map.put("accountSuitId", UserUtils.getUser().getAccountSuitId());
//        return dao.findImagePath(map);
        return null;
    }

    /**
     * PDF文件转换为JPG后上传
     *
     * @param pdfFile
     * @param dbFile
     * @param fullName
     * @param businessId
     * @param list
     */
    @Transactional(readOnly = false)
    public void PDFProcess(File pdfFile, SysFile dbFile, String fullName, List<Long> businessId, List<SysFile> list) {
        String transportResult = "false";
        if (pdfFile.exists()) {
            //先在本地进行PDF转JPG处理
            transportResult = pdfToImage(pdfFile.getPath(), pdfFile.getParent(), 160);
        }
        String originName = fullName.substring(0, fullName.lastIndexOf("."));
        if (!"false".equals(transportResult)) {
            File jpgFile = new File(transportResult);
            long fileSize = jpgFile.length();
            dbFile.setFileName(originName + ".jpg");
            if (fileSize > 0) {
                dbFile.setFileSize(NumberUtils.sub(jpgFile.length(), 1024));
                if (new MimetypesFileTypeMap().getContentType(jpgFile) != null) {
                    dbFile.setFileType(new MimetypesFileTypeMap().getContentType(jpgFile));
                } else {
                    dbFile.setFileType("image/jpeg");
                }
            }
            dbFile.setFilePath(transportResult);
            if (businessId != null) {
                if (businessId.get(0) != null) {
                    dbFile.setBusinessId(businessId.get(0));
                }
            }
            if (enableFileRemoteService) {
                //本地服务
                save(dbFile);
                dbFile.setFileName(originName + ".pdf");
                list.add(dbFile);
            } else {//使用远程服务
//                String remoteUploadPath = getRemoteUploadPath("");
//                dbFile.setFilePath(remoteUploadPath);
//                try {
//                    //转换后的JPG文件再上传远程服务器
//                    FileInputStream in_file = new FileInputStream(jpgFile);
//                    MultipartFile tmpFile = new MockMultipartFile(originName + ".jpg", in_file);
//                    SysFile sysFile = remoteFileService.fileUpload(tmpFile, dbFile);
//                    if (sysFile != null) {
//                        //删除本地缓存的JPG文件和存放JPG文件的文件夹
//                        deleteCache(jpgFile, pdfFile);
//                        in_file.close();
//                        //页面展示名称时仍显示为是PDF
//                        sysFile.setFileName(originName + ".pdf");
//                        list.add(sysFile);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        }
    }

    /**
     * 删除本地缓存的PDF转换文件
     *
     * @param jpgFile
     * @param pdfFile
     * @throws IOException
     */
    public void deleteCache(File jpgFile, File pdfFile) throws IOException {
        Path jpgDir = jpgFile.toPath().getParent();
        File jpgDirFile = jpgFile.getParentFile();
        if (jpgDirFile.exists() && jpgDirFile.isDirectory()) {
            File[] files = jpgDirFile.listFiles();
            //delete()方法不能删除非空文件夹
            for (File f : files) {
                if (f.exists()) {
                    f.delete();
                    log.info("删除本地缓存文件" + f.getName() + "成功!");
                } else {
                    log.info("本地缓存文件" + f.getName() + "未删除");
                }
            }
            Files.delete(jpgDir);
            if (!jpgDirFile.exists()) {
                log.info("删除本地jpg缓存文件夹" + jpgDirFile.getName() + "成功!");
            } else {
                log.info("本地jpg缓存文件夹" + jpgDirFile.getName() + "未删除");
            }
            if (!jpgFile.exists() && !pdfFile.exists() && !!jpgDirFile.exists()) {
                log.info("全缓存文件删除完毕");
            }
        }
    }

    /***
     * PDF文件转jpg图片，全部页数
     *
     * @param PdfFilePath pdf完整路径
     * @param dstImgFolder 图片存放的文件夹
     * @param dpi dpi越大转换后越清晰，相对转换速度越慢
     * @return
     */
    public String pdfToImage(String PdfFilePath, String dstImgFolder, int dpi) {
        File PdfFile = new File(PdfFilePath);

        try {
            // 获取图片文件名
            String imagePdfName = PdfFile.getName().substring(0, PdfFile.getName().lastIndexOf('.'));
            String imgFolderPath = null;
            if (dstImgFolder.equals("")) {
                // 获取图片存放的文件夹路径
                imgFolderPath = PdfFile.getParent() + File.separator + imagePdfName;
            } else {
                imgFolderPath = dstImgFolder + File.separator + imagePdfName;
            }

            if (createDirectory(imgFolderPath)) {
                PDDocument pdDocument = PDDocument.load(PdfFile);
                PDFRenderer renderer = new PDFRenderer(pdDocument);
                /* dpi越大转换后越清晰，相对转换速度越慢 */
                String imgFilePath = imgFolderPath + File.separator + imagePdfName + "_" + String.valueOf(1) + ".jpg";
                File dstFile = new File(imgFilePath);
                //仅抓取PDF中的第一页转换为图片
                ImageIO.write(renderer.renderImageWithDPI(0, dpi), "jpg", dstFile);
                pdDocument.close();
                Files.delete(PdfFile.toPath());
                if (!PdfFile.exists()) {
                    log.info("删除缓存文件" + PdfFile.getName() + "成功!");
                }
                return dstFile.getPath();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "false";
    }

    private static boolean createDirectory(String folder) {
        File dir = new File(folder);
        if (dir.exists()) {
            return true;
        } else {
            return dir.mkdirs();
        }
    }

//    public String getJson(List<SysFile> list, String tagIndex) {
//        List<String> jsonList = Lists.newArrayList();
//        if (CollectionUtils.isNotEmpty(list)) {
//            for (SysFile fileInfo : list) {
//                jsonList.add(fileInfo.getId());
//                jsonList.add(fileInfo.getFileType());
//                jsonList.add(fileInfo.getShowFileSize());
//                jsonList.add(fileInfo.getFileName());
//                jsonList.add(fileInfo.getFilePath());
//                jsonList.add(fileInfo.getBusinessCategory());
//                //tagIndex 附件上传标签的序号
//                if(StringUtils.isNotBlank(tagIndex)){
//                    jsonList.add(tagIndex);
//                }
//            }
//        }
//        String json = JsonMapper.getInstance().toJson(jsonList);
//        return json;
//    }

    /**
     * 創建文件
     *
     * @param dbFile
     * @param list
     * @param request
     * @param fileDetail
     * @throws IOException
     */
    public void createFile(SysFile dbFile, List<SysFile> list, HttpServletRequest request, MultipartFile fileDetail) throws IOException {
        //使用本地服务
        if (!enableFileRemoteService) {
            String name = FileUtil.getName(fileDetail.getOriginalFilename());
            String fullPath2 = getUploadPath(request) + name;
            dbFile.setFilePath(fullPath2);
            dbFile.setFileName(name);
            File saveFile2 = new File(fullPath2);
            fileDetail.transferTo(saveFile2);
            save(dbFile);
            list.add(dbFile);
        } else {//使用远程服务
//            String remoteUploadPath = getRemoteUploadPath("");
//            dbFile.setFilePath(remoteUploadPath);
//            SysFile sysFile = remoteFileService.fileUpload(fileDetail, dbFile);
//            if (sysFile != null) {
//                list.add(sysFile);
//            }
        }
    }

//    public SysFile findSysFile(SysFile sysFile) {
//        return get(sysFile);
//    }

    public List<SysFile> findListByFileIds(List<String> fileIds) {
//        List<SysFile> fileList = Lists.newArrayList();
//        if (CollectionUtils.isNotEmpty(fileIds)) {
//            fileList = dao.findListByFileIds(fileIds);
//        }
//        return fileList;
        return null;
    }

    public void resetFile(String oldBusinessId, Long newBusinessId) {
        List<SysFile> sysFileList = findFileList(Arrays.asList(oldBusinessId));
        if (CollectionUtils.isNotEmpty(sysFileList)) {
            for (SysFile sysFile : sysFileList) {
                sysFile.setId(null);
                sysFile.setBusinessId(newBusinessId);
                save(sysFile);
            }
        }
    }


    /**
     * 附件打包导出
     *
     * @param sysFileList 要导出的附件集合
     * @param packageName 压缩包名称
     * @param request
     * @param response
     */
    public void exportFilePackage(List<SysFile> sysFileList, String packageName, HttpServletRequest request, HttpServletResponse response) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(sysFileList)) {
            for (SysFile sysFile : sysFileList) {
                if (sysFile != null && sysFile.getId() != null) {
                    if (StringUtils.isNotBlank(sysFile.getFileName())) {
                        int count = 0;
                        for (SysFile sf : sysFileList) {
                            String sfName = sf.getFileName();
                            if (StringUtils.isNotBlank(sfName) && sysFile.getFileName().equals(sfName)) {
                                count++;
                                if (count > 1) {
                                    int pointIndex = sfName.lastIndexOf(".");
                                    String fileType = sfName.substring(pointIndex, sfName.length());
                                    String originName = sfName.substring(0, sfName.lastIndexOf("."));
                                    String newName = originName + "-" + String.valueOf(count) + fileType;
                                    sf.setFileName(newName);
                                }
                            }
                        }
                    }

                    if (!enableFileRemoteService) {
                        String newPath = Encodes.urlDecode(sysFile.getFilePath());
                        File file = new File(newPath);
                        String fileName = StringUtils.isNotBlank(sysFile.getFileName()) ? sysFile.getFileName() : file.getName();
                        String fileCheck = FileUtils.fileCheck(file, request, response, fileName);
                        if (StringUtils.isBlank(fileCheck)) {
                            Map<String, Object> fileMap = new HashMap<>();
                            fileMap.put("type", "file");
                            fileMap.put("file", file);
                            fileMap.put("fileName", fileName);
                            fileMap.put("id", sysFile.getId());
                            mapList.add(fileMap);
                        }
                    } else {
//                        String fileName = StringUtils.isNotBlank(sysFile.getFileName()) ? sysFile.getFileName() : IdGen.uuid();
//                        Map<String, Object> fileMap = new HashMap<>();
//
//                        InputStream in = remoteFileService.downloadFileInputStrem(sysFile, false, false);
//                        if (in != null) {
//                            try {
//                                fileMap.put("type", "inputStream");
//                                fileMap.put("inputStream", in);
//                                fileMap.put("fileName", fileName);
//                                fileMap.put("id", sysFile.getId());
//                                mapList.add(fileMap);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
                    }
                }

            }
        }

        if (CollectionUtils.isNotEmpty(mapList)) {
            try {
                DateFormat format = new SimpleDateFormat("yyyyMMdd-HHmmss");
                String downloadFileName = format.format(new Date()) + ".zip";//文件的名称
                if (StringUtils.isNotBlank(packageName)) {
                    downloadFileName = packageName + ".zip";
                }
                downloadFileName = URLEncoder.encode(downloadFileName, "UTF-8");//转换中文否则可能会产生乱码
                response.setContentType("application/octet-stream");// 指明response的返回对象是文件流
                response.setHeader("Content-Disposition", "attachment;filename=" + downloadFileName);// 设置在下载框默认显示的文件名
                ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
                InputStream in = null;

                for (Map<String, Object> map : mapList) {
                    String type = (String) map.get("type");
                    if ("file".equals(type)) {
                        File file = (File) map.get("file");
                        in = new FileInputStream(file);
                    } else {
                        in = (InputStream) map.get("inputStream");
                    }
                    String fileName = (String) map.get("fileName");
                    zos.putNextEntry(new ZipEntry(fileName));
                    byte[] buffer = new byte[1024];
                    int r = 0;
                    while ((r = in.read(buffer)) != -1) {
                        zos.write(buffer, 0, r);
                    }
                    in.close();
                }
                zos.flush();
                zos.close();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public List<SysFile> getFilesByBusIdAndType(Long busId,Long type) {
        Map<String,Object> query=new HashMap<>();
        query.put("business_id",busId);
        query.put("type",type);
        return this.listByMap(query);
    }

    @Override
    public Boolean updateFileForBusId(List<Long> fileIds, Long busId,Long type) {
        if (type == null){
            return false;
        }
        this.getBaseMapper().updateFileForBusId(fileIds,busId,type);
        return true;
    }

    @Override
    public Boolean deleteByBusinessId(List<Long> fileIds, Long busId,Long type) {
        if (type == null){
            return false;
        }

        if (type == 7){ //合同文件先删除再新增
            SysFile sysFile = this.getBaseMapper().selectById(busId);
            if (sysFile != null){
                new File(sysFile.getFilePath()).delete();
            }

        }
        this.getBaseMapper().deleteByBusinessId(fileIds,busId,type);
        return true;
    }
}
