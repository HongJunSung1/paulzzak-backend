package com.esg.bbollock.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class FileService {
	
	// 파일 업로드
    public List<Map<String, Object>> saveFiles(List<MultipartFile> files, String openUrl) throws IOException {
        LocalDate now = LocalDate.now();
        String year = now.getYear() + "";
        String month = now.getMonthValue() + "";
        String day = now.getDayOfMonth() + "";
//        String root = "C:\\esgFiles\\" + openUrl;
        String root = "/home/tomcat/apache-tomcat-9.0.90_server/webapps/file/" + openUrl;
        
        Path directory = Paths.get(root, year, month, day);

        // 해당 경로에 해당하는 폴더가 없으면 생성
        if(Files.notExists(directory)) {
        	Files.createDirectories(directory);        	
        }
        List<Map<String, Object>> returnData = new ArrayList<Map<String, Object>>();
        
    	for (MultipartFile file : files) {
//        		System.out.println("File Name: " + file.getOriginalFilename());
//        		System.out.println("File Size: " + file.getSize());
//        		System.out.println("File Type: " + file.getContentType());
    		Map<String, Object> mapFileData = new HashMap<String, Object>();
    		
    		    		
    		String saveFileName = uuidFileName(file.getOriginalFilename()); //uuid를 추가한 파일명
    		Path targetPath = directory.resolve(saveFileName).normalize(); //파일 이름이 포함된 경로
    		if(Files.exists(targetPath)) {
    			throw new IOException("중복된 파일이 존재하여 저장에 실패하였습니다.");
    		}
    		
    		// 지정한 경로에 파일 생성(저장)
    		file.transferTo(targetPath);
    		
    		
    		mapFileData.put("originalFileName", file.getOriginalFilename());
    		mapFileData.put("fileSize", file.getSize());
    		mapFileData.put("uuidFileName", saveFileName);
    		mapFileData.put("filePath", directory);
    		returnData.add(mapFileData);
    	}        
    	
    	return returnData;
    }
    
    // uuid 생성
    private String uuidFileName(String originalFileName) {
        UUID uuid = UUID.randomUUID();
        return uuid.toString() + '_' + originalFileName;
    }
    
    
    // 파일 다운로드
    public Resource loadFileAsResource(String fileName, String fileDownloadPath) throws Exception {
        try {
            Path fileStorageLocation = Paths.get(fileDownloadPath).toAbsolutePath().normalize();
            Path filePath = fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new Exception("File not found " + fileName);
            }
        } catch (Exception ex) {
            throw new Exception("File not found " + fileName, ex);
        }
    }
    
    // 파일 삭제
    public boolean deleteFile(String filePath, String fileName) {
        File file = new File(filePath + File.separator + fileName);
        if (file.exists()) {
            return file.delete();
        } else {
            return false;
        }
    }
 }