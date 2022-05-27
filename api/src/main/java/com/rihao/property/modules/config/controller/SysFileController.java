package com.rihao.property.modules.config.controller;

import com.alibaba.fastjson.JSON;
import com.anteng.boot.web.BaseController;
import com.anteng.boot.web.bind.response.ResBody;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.rihao.property.common.annotation.Log;
import com.rihao.property.modules.common.vo.UploadResultVo;
import com.rihao.property.modules.config.entity.SysFile;
import com.rihao.property.modules.config.service.ISysFileService;
import com.rihao.property.modules.config.vo.FileQueryParam;
import com.rihao.property.util.Encodes;
import com.rihao.property.util.FileUtils;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.h2.store.fs.FilePath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 本地文件上传
 */
@RestController
@RequestMapping("/api/file")
@Slf4j
public class SysFileController extends BaseController {

    private ISysFileService sysFileService;
    @Value("${enableFileRemoteService}")
    private  Boolean enableFileRemoteService;
    @Value("${file.upload.path}")
    private  String FILE_UPLOAD_PATH;
    @Autowired
    private void setSysFileService(ISysFileService sysFileService) {
        this.sysFileService = sysFileService;
    }


    /**
     * 文件下载
     *
     * @param request
     * @param response
     * @param fileQueryParam
     * @return
     */
    @RequestMapping(value = "downloadFile")
    public void downloadFile(HttpServletRequest request, HttpServletResponse response, FileQueryParam fileQueryParam) {
            String filePath=null;
            String fileName=null;
            if (fileQueryParam != null && StringUtils.isNotEmpty(fileQueryParam.getId())) {
                SysFile sysFile = sysFileService.getById(fileQueryParam.getId());
                filePath=sysFile.getFilePath();
                fileName=sysFile.getFileName();
                String newPath = Encodes.urlDecode(filePath);
                java.io.File file = new java.io.File(newPath);
                FileUtils.downFile(file, request, response, fileName);
            }else  if(fileQueryParam != null && fileQueryParam.getParkXlsTemplate() !=null && fileQueryParam.getParkXlsTemplate()) {
                filePath =  "execlTemplate" + File.separator + "园区导入模版.xls";
                 fileName ="园区导入模版.xls";
                try {
//                    File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + filePath);
                    //目标目录
                    String tempPath = FILE_UPLOAD_PATH+File.separator+"tomcat_temp"+System.currentTimeMillis();
                    String tempFile = tempPath+File.separator+fileName;
                    Resource resource = new DefaultResourceLoader().getResource("classpath:"+filePath);
                    if (resource == null) {
                        log.info("资源文件为空："+filePath);
                        return ;
                    }

                    File file = new File(tempPath);
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    //复制文件
                    FileUtils.copyInputStreamToFile(resource.getInputStream(),new File(tempFile));
                    //下载复制后的临时文件
                    FileUtils.downFile(new File(tempFile), request, response, fileName);
                    //删除临时文件
                    FileUtils.deleteDirectory(tempPath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
//            if (!enableFileRemoteService) {
//                String newPath = Encodes.urlDecode(filePath);
//                java.io.File file = new java.io.File(newPath);
//                FileUtils.downFile(file, request, response, fileName);
//            } else {
//                // 普通下载，app安装包下载，不涉及转码
////            remoteFileService.remoteFileDownload(response, sysFile, false, false);
//            }

    }

//    /**
//     * 文件打包下载
//     *
//     * @param request
//     * @param response
//     * @param businessId
//     * @param code
//     */
//    @RequiresPermissions("sys:filePreview:edit")
//    @RequestMapping(value = "downloadFilePackage")
//    public void downloadFilePackage(HttpServletRequest request, HttpServletResponse response,
//                                    String businessId, String code) {
//        if (StringUtils.isNotBlank(businessId)) {
//            sysFileService.downloadFilePackage(request, response, businessId, code);
//        }
//    }




    /**
     * app文件下载
     *
     * @param request
     * @param response
     * @param isView false表示下载 其他情况表示预览
     * @return
     */
//    @RequestMapping(value = "appDownloadFile")
//    public void appDownloadFile(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = true) String id, @RequestParam(required = false) Boolean isView) {
//        // 下载
//        if (isView != null && !isView) {
//            downloadFile(request, response, id);
//            return;
//        }
//        // 预览
//        SysFile sysFile = sysFileService.get(id);
//        String fileName = sysFile.getFileName();
//        String ex = FileUtils.getFileExtension(fileName);
//        List<String> downFileTypeList = Arrays.asList(new String[]{"pdf","jpg","jpeg","gif","png","bmp","apk"});
//        // 需要转码的情况下，文件大小不能超过100M
//        if (!downFileTypeList.contains(ex) && sysFile.getFileSize() > 1024 * 1024 *100) {
//            String classpath = this.getClass().getResource("/").getPath().replaceFirst("/", "");
//            String path = classpath.replace("WEB-INF/classes/", "upload/");
//            String filePath = path + "pdf/model2.pdf";
//            File file2 = new File(filePath);
//            FileUtils.downFile(file2, request, response, fileName);
//            return;
//        }
//        if (enableFileRemoteService) {
//            String newPath = Encodes.urlDecode(sysFile.getFilePath());
//            java.io.File file = new java.io.File(newPath);
//            if (!file.exists() || ex.isEmpty()) {
//                return;
//            }
//            if (downFileTypeList.contains(ex)) {
//                //直接下载
//                FileUtils.downFile(file, request, response, fileName);
//                return;
//            }
//            //转成pdf再下载，不需要转swf
//            sysFileService.viewFile(sysFile, ex, false, request, response);
//            File file2 = new File(sysFile.getFilePath() + ".pdf");
//            FileUtils.downFile(file2, request, response, fileName.substring(0, fileName.lastIndexOf(".")) + ".pdf");
//        } else {
//            //app对于图片以外的文件，需要先转换成pdf再下载，不需要转成swf
//            remoteFileService.remoteFileDownload(response, sysFile, true, false);
//        }
//    }

    /**
     * 文件上传
     *
     * @param file
     * @param isOnly  是否只允许存在一个附件，如果是，则上传文件时会先去除当前单据相关的附件关联信息
     * @param type  附件对应的业务类型
     * @return
     * @throws Exception
     */
    @PostMapping(value = "upload",consumes = "multipart/*",headers = "content-type=multipart/form-data" )
    @ResponseBody
    public String upload( HttpServletRequest request,Long busid,Boolean isOnly,Long type ) throws Exception {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        if  (files != null && type != null) {
            List<SysFile> sysFiles=new ArrayList<>();
            for (int i = 0; i < files.size(); i++) {
                MultipartFile file1 = files.get(i);
                sysFiles.addAll(sysFileService.upload(file1,request,busid,isOnly,type));
            }
            return JSON.toJSONString(sysFiles);
        }
        return null;
    }

//    @ResponseBody
//    @RequiresPermissions("sys:userFile:edit")
//    @RequestMapping(value = "scanUpload", method = RequestMethod.POST)
//    public Object scanUpload(String businessId, @RequestParam MultipartFile[] file, String uploadNamePri, String mergeToPDF,
//                             HttpServletRequest request, RedirectAttributes redirectAttributes) {
//        sysFileService.scanUpload(businessId, file, request, "", "", uploadNamePri, "", mergeToPDF);
//        return AjaxResult.ok("扫描添加成功！");
////        return "扫描添加成功！";
//    }

//    @ResponseBody
//    @RequestMapping(value = "scanUploadNow", method = RequestMethod.POST)
//    public Object scanUploadNow(@RequestParam(value = "file", required = false) MultipartFile[] file, String businessCategory, String fileConfigCode,
//                                String uploadNamePri, String index, String mergeToPDF, HttpServletRequest request, RedirectAttributes redirectAttributes) {
//        List<SysFile> fileList = sysFileService.scanUpload("", file, request, businessCategory, fileConfigCode, uploadNamePri, index, mergeToPDF);
//        return JsonMapper.toJsonString(fileList);
//    }

//    @RequestMapping(value = "userFileUpload")
//    @ResponseBody
//    public String userFileUpload(@RequestParam("addFile") MultipartFile[] file,
//                                 @RequestParam("categoryId") String categoryId,
////                         @RequestParam(value = "fileConfigCode", required = false) String fileConfigCode,
//                                 HttpServletRequest request) throws Exception {
//        List<SysFile> list = Lists.newArrayList();
//        int i = file.length;
//        if (StringUtils.isBlank(categoryId)) {
//            return "上传失败，分类Id为空或未选择分类";
//        }
//        if (i > 0) {
//            FileCategory fileCategory = fileCategoryService.get(categoryId);
//            if (fileCategory == null) {
//                return "上传失败：所选分类不存在，请刷新重试";
//            }
//            String fileType = "";
//            if (NumberUtils.isNumber(fileCategory.getFileType())) {
//                int typeNum = Integer.parseInt(fileCategory.getFileType());
//                fileType = FileConfig.FILE_TYPES.get(typeNum);
//            }
//            for (MultipartFile fileDetail : file) {
//                SysFile dbFile = new SysFile();
//                if (StringUtils.isNotBlank(fileDetail.getOriginalFilename())) {
//                    String fileLastType = getFileLastType(fileDetail);
//                    // 判断附件类型是否有效
//                    if ((StringUtils.isNotBlank(fileType) && fileType.indexOf(fileLastType) == -1)) {
//                        continue;
//                    }
//                    dbFile.setFileName(fileDetail.getOriginalFilename());
//                    dbFile.setFileSize((double) fileDetail.getSize());
//                    dbFile.setFileType(fileDetail.getContentType());
//                    dbFile.setBusinessId(categoryId);
//                    sysFileService.createFile(dbFile, list, request, fileDetail);
//                }
//            }
//        } else {
//            return "上传失败：未检索到文件";
//        }
//        if (list.size() == 0) {
//            return "上传失败：请上传正确的附件格式";
//        }
//        if (i != list.size() && list.size() != 0) {
//            return "部分上传成功：存在不符的文件类型";
//        }
//        return sysFileService.getJson(list, "");
//    }
//
//    private String getFileLastType(MultipartFile fileDetail) {
//        String fileLastType = "";
//        int num = fileDetail.getOriginalFilename().lastIndexOf(".");
//        if (num > 0) {
//            fileLastType = fileDetail.getOriginalFilename().substring(num);
//        }
//        return fileLastType;
//    }


//    @ResponseBody
//    @RequestMapping(value = "delete")
//    public String delete(@RequestParam(value = "id", required = true) String id,
//                         @RequestParam(value = "businessId", required = false) String businessId) {
//        String result;
//        if (StringUtils.isNotBlank(businessId)) {
//            if ("1".equals(CacheUtils.get("UploadQrCodeState" + AccountSuitUtils.getAccountSuit().getId() + businessId))) {
//                result = deleteFileById(id);
//            } else {
//                return "false";
//            }
//        } else {
//            result = deleteFileById(id);
//        }
//        return result;
//    }

//    public String deleteFileById(String id) {
//        try {
//            sysFileService.delete(new SysFile(id));
//            return "true";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "false";
//        }
//    }

//    @RequestMapping(value = "batchDelete")
//    @ResponseBody
//    public Boolean batchDelete(@RequestParam(value = "fileId", required = false) List<String> fileId) {
//        sysFileService.batchDelete(fileId);
//        return true;
//    }








    //根据businessId获取图片到页面显示
    @ResponseBody
    @RequestMapping(value = "getFile")
    public String getFile(@RequestParam(value = "businessId", required = true) Long businessId,
                          HttpServletRequest request,
                          HttpServletResponse response) {

//        if (CollectionUtils.isNotEmpty(fileList)) {
//            SysFile sysFile = sysFileService.getById(fileList.get(0));
//            String ex = FileUtils.getFileExtension(sysFile.getFileName());
//            if ("jpg".equalsIgnoreCase(ex) || "jpeg".equalsIgnoreCase(ex) || "gif".equalsIgnoreCase(ex) || "png".equalsIgnoreCase(ex)) {
//                try {
//                    sysFileService.showImage(sysFile, request, response);
//                    return null;
//                } catch (IOException e) {
//                }
//            }
//        }
        return null;
    }

    //根据businessId获取文件信息
    @ResponseBody
    @RequestMapping(value = "getFileList")
    public String getFileList(@RequestParam(value = "businessId", required = false) String businessId,
                              @RequestParam(value = "businessCategory", required = false) String businessCategory) {
//        if ("0".equals(CacheUtils.get("UploadWindowState" + AccountSuitUtils.getAccountSuit().getId() + businessId))) {
//            Map map = new HashMap();
//            map.put("UploadWindowState", "0");
//            return JsonMapper.toJsonString(map);
//        }
//        List<SysFile> fileList = Lists.newArrayList();
//        if (StringUtils.isNotBlank(businessId)) {
//            SysFile fileParam = new SysFile();
//            fileParam.setBusinessId(businessId);
//            fileParam.setBusinessCategory(businessCategory);
//            fileList = sysFileService.findList(fileParam);
//        }
//        if (CollectionUtils.isNotEmpty(fileList)) {
//            return JsonMapper.toJsonString(fileList);
//        }
        return null;
    }

    /**
     * 附件预览
     *
     * @param model
     * @param id           文件ID
     * @param type         预览类型  'photo'/'wps'/'pdf'
     * @param pdfJsEdition pdfjs版本，''/'pdfjs-1.10.100'/'pdfjs-2.2.228'，浏览器决定
     * @return
     */
    @RequestMapping(value = "viewFile")
    public String viewFile(Model model, @RequestParam(required = true) String id, @RequestParam(required = false) String type,
                           @RequestParam(required = false) String pdfJsEdition, HttpServletRequest request,
                           HttpServletResponse response, RedirectAttributes redirectAttributes) {
        SysFile sysFile = sysFileService.getById(id);
        String ex = FileUtils.getFileExtension(sysFile.getFileName());
        if (StringUtils.isBlank(type) && ("jpg".equalsIgnoreCase(ex) || "jpeg".equalsIgnoreCase(ex) || "gif".equalsIgnoreCase(ex) || "png".equalsIgnoreCase(ex) || "bmp".equalsIgnoreCase(ex))) {
            // 兼容旧写法
            type = "photo";
        }
        if ("photo".equals(type)) {
            // 图片预览 photo
            return "modules/sys/file/imageView";
        } else {
            // 文本预览 file/pdf
            if (StringUtils.isBlank(pdfJsEdition)) {
                // swf预览方式
                String result = sysFileService.viewFile(sysFile, ex, true, request, response);
                model.addAttribute("message", result);
                return "modules/sys/file/documentView";
            } else {
                // pdfjs预览方式
                String result = sysFileService.viewFile(sysFile, ex, false, request, response);
                model.addAttribute("message", result);
                model.addAttribute("pdfJsEdition", pdfJsEdition);
                return "modules/sys/file/pdfJsView";
            }
        }
    }

    @RequestMapping(value = "getImageFile")
    public String getImageFile(HttpServletResponse response, HttpServletRequest request, @RequestParam(required = true) String id) throws Exception {
        SysFile sysFile = sysFileService.getById(id);
        if (sysFile != null) {
            sysFileService.showImage(sysFile, request, response);
        }
        return null;
    }

    /**
     * 获取swf文件流
     *
     * @param response
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "getSwfFile")
    public void getSwfFile(HttpServletResponse response, @RequestParam(required = true) String id) throws Exception {
        SysFile sysFile = sysFileService.getById(id);
        if (sysFile != null) {
            sysFileService.getSwfFile(sysFile, response);
        }
    }

    /**
     * 获取Pdf文件流
     *
     * @param response
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "getPdfjsFile")
    public void getPdfjsFile(HttpServletResponse response, @RequestParam(required = true) String id) throws Exception {
//        SysFile sysFile = sysFileService.get(id);
//        if (sysFile != null) {
//            //pdf文件取源文件，没有后缀，ppt/word文件取转换的pdf文件，后缀为pdf
//            String ex = FileUtils.getFileExtension(sysFile.getFileName());
//            sysFileService.getFile(sysFile, response, "pdf".equals(ex) ? "" : (".pdf"));
//        }
    }

    /**
     * 获取本地swf文件流（按名字）
     *
     * @param response
     * @param fileName
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "getLocalSwfFile")
    public void getLocalSwfFile(HttpServletResponse response, @RequestParam(required = true) String fileName) throws Exception {
        SysFile sysFile = new SysFile();
        String path = this.getClass().getResource("/").getPath();
        path = path.substring(0, path.lastIndexOf("WEB-INF"));
        sysFile.setFileName(fileName);
        sysFile.setFilePath(path + "static" + File.separator + "excelTemplate" + File.separator + fileName);
        OutputStream os = null;
        InputStream in = null;
        File file = new File(sysFile.getFilePath() + ".swf");
        if (file.exists()) {
            in = new FileInputStream(file);
        }
        response.setContentType("application/x-shockwave-flash");     //设置返回内容格式
        //用该文件创建一个输入流
        os = response.getOutputStream();  //创建输出流
        byte[] b = new byte[1024];
        while (in.read(b) != -1) {
            os.write(b);
        }
        in.close();
        os.flush();
        os.close();
    }

    /**
     * 获取本地pdf文件流（按名字）
     *
     * @param response
     * @param fileName 带后缀的文件名称，文件名中不能包含"."
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "getLocalPdfFile")
    public void getLocalPdfFile(HttpServletResponse response, @RequestParam(required = true) String fileName) throws Exception {
        OutputStream os = null;
        InputStream in = null;
        String path = this.getClass().getResource("/").getPath();
        path = path.substring(0, path.lastIndexOf("WEB-INF"));
        String ex = FileUtils.getFileExtension(fileName);
        String filePath = (path + "static" + File.separator + "excelTemplate" + File.separator + fileName + ("pdf".equals(ex) ? "" : (".pdf")));
        File file = new File(filePath);
        if (file.exists()) {
            in = new FileInputStream(file);
        }
        response.setContentType("application/x-shockwave-flash");     //设置返回内容格式
        //用该文件创建一个输入流
        os = response.getOutputStream();  //创建输出流
        byte[] b = new byte[1024];
        while (in.read(b) != -1) {
            os.write(b);
        }
        in.close();
        os.flush();
        os.close();
    }



    //过滤特殊字符
    private String StringFilter(String text) {
        // 只允许字母和数字
        String regEx = "[^a-zA-Z0-9]";

        // 清除掉所有特殊字符
//        String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(text);
        return m.replaceAll("").trim();
    }



    //根据businessId获取图片到页面显示
    @ResponseBody
    @RequestMapping(value = "getSysFile")
    public String getSysFile(Model model, SysFile sysFile, HttpServletRequest request, HttpServletResponse response) throws IOException {
//        if (StringUtils.isNotBlank(sysFile.getId())) {
//            sysFile = sysFileService.get(sysFile);
//            String newPath = Encodes.urlDecode(sysFile.getFilePath());
//            File file = new File(newPath);
//            if (!file.exists()) {
//                return null;
//            } else {
//                response.setContentType(sysFile.getFileType());
//                if (sysFile.getFileType().contains("video")) {
//                    //当请求的文件是video的时候，需要对返回头做一些设置，否则包括chrome在内的个别浏览器video标签无法拖动进度条
//                    //如果是video标签发起的请求就不会为null
//                    String rangeString = request.getHeader("Range");
//                    long range = Long.valueOf(rangeString.substring(rangeString.indexOf("=") + 1, rangeString.indexOf("-")));
//                    response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(sysFile.getFileName(), "UTF-8"));
//                    //视频文件的大小
//                    response.setContentLength(sysFile.getFileSize().intValue());
//                    //拖动进度条时的断点
//                    response.setHeader("Content-Range", String.valueOf(range + (sysFile.getFileSize().intValue() - 1)));
//                    response.setHeader("Accept-Ranges", "bytes");
//                }
//                FileInputStream fis = new FileInputStream(file);
//                OutputStream os = response.getOutputStream();
//                try {
//                    int count;
//                    byte[] buffer = new byte[1024 * 1024];
//                    while ((count = fis.read(buffer)) != -1)
//                        os.write(buffer, 0, count);
//                    os.flush();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } finally {
//                    if (os != null)
//                        os.close();
//                    if (fis != null)
//                        fis.close();
//                }
//            }
//        }
        return null;
    }

    private Object getError(String message) {
        Map<String, Object> result = Maps.newHashMap();
        result.put("error", 1);
        result.put("message", message);
        return result;
    }

    @RequestMapping(value = "addFiles")
    public String addFiles(Model model, @RequestParam(value = "businessId") String businessId,
                           @RequestParam(value = "moduleCode") String moduleCode,
                           @RequestParam(value = "operatorUserId") String operatorUserId) {
        model.addAttribute("businessId", businessId);
        model.addAttribute("moduleCode", moduleCode);
        model.addAttribute("operatorUserId", operatorUserId);
        return "modules/sys/file/addFiles";
    }

    @RequestMapping(value = "editFiles")
    public String editFiles(Model model, @RequestParam(value = "businessId") String businessId,
                            @RequestParam(value = "moduleCode") String moduleCode) {
        model.addAttribute("businessId", businessId);
        model.addAttribute("moduleCode", moduleCode);
        return "modules/sys/file/editFiles";
    }

//    @RequestMapping(value = "updateFiles")
//    @ResponseBody
//    public String updateFiles(Model model, @RequestParam(value = "businessId") String businessId,
//                              @RequestParam(required = false) String functionModuleCode,
//                              @RequestParam(required = false) List<String> fileIds) {
//        //更新附件信息
//        if (StringUtils.isNotBlank(functionModuleCode)) {
//            //补传附件等功能，根据单据id，附件明细编码精准修改附件
//            sysFileService.updateBusinessIdAndConfigCode(businessId, functionModuleCode, fileIds);
//        } else {
//            //兼容其他情况
//            sysFileService.updateBusinessId(businessId, fileIds);
//        }
//        return "success";
//    }

    @RequestMapping(value = {"treeView"})
    public String treeView(Model model, @RequestParam Map<String, Object> dataMap) {
        model.addAttribute("fileId", dataMap.get("fileId"));
        model.addAttribute("treeData", dataMap.get("treeData"));
        return "modules/sys/file/treeView";
    }









}
