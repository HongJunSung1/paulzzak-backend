package com.esg.bbollock.controller;

import com.esg.bbollock.service.FileService;
import com.esg.bbollock.service.comSQLService;
import com.esg.bbollock.service.comXmlService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins="*",allowedHeaders = "*")
@RestController
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }
    
    @Autowired
    private comSQLService comSqlService;
    
    @Autowired
    private comXmlService comXmlService;

    // 파일 업로드
    @PostMapping("/uploadFiles")
    public List<Map<String, Object>> uploadFiles(@RequestParam("files") List<MultipartFile> files, @RequestParam("openUrl") String openUrl) {
    	List<Map<String, Object>> returnData = new ArrayList<Map<String, Object>>();
    	
        try {
        	String strSPName = "S_ESG_COM_File_Save";
        	
        	// 파일 서비스단에서 saveFiles 결과값 담기
        	List<Map<String, Object>> fileData = fileService.saveFiles(files, openUrl);
    		
        	// saveFiles 결과값을 XML 형태로 바꾸기  
        	String strXml = "";    		
        	strXml = comXmlService.ConvertToXmlDefault("", fileData);
						
        	// XML 데이터를 SP로 실행해서 결과값 넘기기
        	returnData = comSqlService.comSQLService_Single(strSPName,"'"+strXml+"'");
//        	System.out.println(strSPName + " '"+strXml+"'");
        	
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return returnData;       
    }
    
    // 파일 다운로드
    @PostMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam("fileName") String fileName, @RequestParam("filePath") String filePath) {
        Resource resource = null;
        try {
            resource = fileService.loadFileAsResource(fileName, filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
    
    // 파일 삭제
    @PostMapping("/deleteFile")
    public ResponseEntity<String> deleteFile(@RequestParam("filePath") String filePath, @RequestParam("fileName") String fileName) {
        boolean isDeleted = fileService.deleteFile(filePath, fileName);
        if (isDeleted) {
            return ResponseEntity.ok("File deleted successfully");
        } else {
            return ResponseEntity.status(404).body("File not found");
        }
    }

}