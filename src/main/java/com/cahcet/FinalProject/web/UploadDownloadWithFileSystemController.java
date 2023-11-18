package com.cahcet.FinalProject.web;

import com.cahcet.FinalProject.model.FileModel;
import com.cahcet.FinalProject.service.Encrypt;
import com.cahcet.FinalProject.service.EncryptEncodeSplit;
import com.cahcet.FinalProject.service.FileModelService;
import com.cahcet.FinalProject.web.dto.FileModelDto;
import com.cahcet.FinalProject.web.dto.FileUploadResponse;
import com.cahcet.FinalProject.service.FileStorageService;
import com.cahcet.FinalProject.web.dto.UserRegistrationDto;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.RedirectView;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@RestController
public class UploadDownloadWithFileSystemController {

    private FileModelService fileModelService;
    private FileStorageService fileStorageService;
    private IvParameterSpec iv = Encrypt.generateIv();

    public UploadDownloadWithFileSystemController(FileModelService fileModelService,FileStorageService fileStorageService) {
        this.fileModelService = fileModelService;
        this.fileStorageService = fileStorageService;
    }
    @ModelAttribute("user")
    public FileModelDto fileModelDto() {
        return new FileModelDto();
    }
    @PostMapping("/single/upload")
    RedirectView singleFileUplaod(@ModelAttribute("file")FileModelDto fileModelDto, @RequestParam("file") MultipartFile file,@CookieValue(name = "key") String value) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        String fileName = fileStorageService.storeFile(file);
        String algorithm = "AES/CBC/PKCS5Padding";
        File uploaded = new File("C:\\Users\\gokul\\OneDrive\\Documents\\Project\\SCS\\fileStorage\\"+fileName);
        SecretKey secretKey = Encrypt.convertStringToSecretKey(value);
//        Encrypt.encrypt(algorithm,secretKey,iv,uploaded,uploaded);

        System.out.println(uploaded.exists());
        String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(fileName)
                .toUriString();
        List<String>parts = EncryptEncodeSplit.splitFile(uploaded,5);
        String partName ="";
        for(String part:parts){
            partName=partName+part+" ";
        }
        System.out.println(partName);
        partName.trim();
        fileModelDto.setFileName(fileName);
        fileModelDto.setPartName(partName);
        fileModelService.save(fileModelDto);

        RedirectView redirectView = new RedirectView();
        if(uploaded.exists()){
//            return "redirect:/upload?success";
        redirectView.setUrl("/success");
        uploaded.delete();
        return redirectView;
        }
        else {
            redirectView.setUrl("/failed");

            return redirectView;
        }
//        String contentType = file.getContentType();

//        FileUploadResponse response = new FileUploadResponse(fileName, contentType, url);
//        RedirectView redirectView = new RedirectView();
//        redirectView.setUrl("/upload?success&url="+url);
//        return redirectView;

    }

    @GetMapping("/download/{fileName}")
    ResponseEntity<Resource> downLoadSingleFile(@PathVariable String fileName, HttpServletRequest request,@CookieValue(name="key")String value) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        String algorithm = "AES/CBC/PKCS5Padding";
        File out = new File("C:\\Users\\gokul\\OneDrive\\Documents\\Project\\SCS\\fileStorage\\"+fileName);
        List<String>partnames = new ArrayList<>();
        for(String partname : fileModelService.getPartNameByFilename(fileName).get(0).split("\\s+")){
            partnames.add(partname.replace("\\","\\\\").trim());
            System.out.println(partname);
        }try {
            System.out.println("Here");
            EncryptEncodeSplit.mergeFiles(partnames, out);
//        Encrypt.decrypt(algorithm,Encrypt.convertStringToSecretKey(value),iv,out,out);
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        Resource resource = fileStorageService.downloadFile(fileName);

//        MediaType contentType = MediaType.APPLICATION_PDF;

        String mimeType;

        try {
            mimeType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
        mimeType = mimeType == null ? MediaType.APPLICATION_OCTET_STREAM_VALUE : mimeType;

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mimeType))
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;fileName="+resource.getFilename())
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline;fileName=" + resource.getFilename())
                .body(resource);
    }

//    @PostMapping("/multiple/upload")
//    List<FileUploadResponse> multipleUpload(@RequestParam("files") MultipartFile[] files) {
//
//        if (files.length > 7) {
//            throw new RuntimeException("too many files");
//        }
//        List<FileUploadResponse> uploadResponseList = new ArrayList<>();
//        Arrays.asList(files)
//                .stream()
//                .forEach(file -> {
//                    String fileName = fileStorageService.storeFile(file);
//
//                    ///http://localhost:8081/download/abc.jpg
//                    String url = ServletUriComponentsBuilder.fromCurrentContextPath()
//                            .path("/download/")
//                            .path(fileName)
//                            .toUriString();
//
//                    String contentType = file.getContentType();
//
//                    FileUploadResponse response = new FileUploadResponse(fileName, contentType, url);
//                    uploadResponseList.add(response);
//                });
//
//        return uploadResponseList;
//    }
//
//    @GetMapping("zipDownload")
//    void zipDownload(@RequestParam("fileName") String[] files, HttpServletResponse response) throws IOException {
////zipoutstream - zipentry+zipentry
//
//        try (ZipOutputStream zos = new ZipOutputStream(response.getOutputStream())) {
//            Arrays.asList(files)
//                    .stream()
//                    .forEach(file -> {
//                        Resource resource = fileStorageService.downloadFile(file);
//
//                        ZipEntry zipEntry = new ZipEntry(resource.getFilename());
//
//                        try {
//                            zipEntry.setSize(resource.contentLength());
//                            zos.putNextEntry(zipEntry);
//
//                            StreamUtils.copy(resource.getInputStream(), zos);
//
//                            zos.closeEntry();
//                        } catch (IOException e) {
//                            System.out.println("some exception while ziping");
//                        }
//                    });
//            zos.finish();
//
//        }
//
//        response.setStatus(200);
//        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;fileName=zipfile");
//    }
}
