package com.esg.bbollock.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.esg.bbollock.service.comSQLService;
import com.esg.bbollock.service.comXmlService;

// 폴짝 import 용
import javax.sql.DataSource;
import java.sql.Connection;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;

@RestController
public class spRequestController {
	
	
	@Autowired
	private comSQLService comSqlService;
	
	@Autowired
	private comXmlService comXmlService;

	
	@RequestMapping("/")
	public String serverInit() {
		return "server Deploy Success";
	}
		
	
	@RequestMapping("/test")
	public String test() {
		return "test";
	}
	
	
//	폴짝 데이터베이스(RDS) 연결 확인
    @Autowired
    private DataSource dataSource;
    
    @GetMapping("/db-test")
    public String testDatabaseConnection() {
        try (Connection connection = dataSource.getConnection()) {
            return "✅ Database Connection Successful!2";
        } catch (Exception e) {
            return "❌ Database Connection Failed: " + e.getMessage();
        }
    }
	
	
	
//	@RequestMapping("/spRequest")
    @PostMapping("/spRequest")
	@ResponseBody
	public List<List<Map<String, Object>>> spRequest(@RequestBody List<Map<String, Object>> sendData){

		List<List<Map<String, Object>>> returnData = new ArrayList<List<Map<String, Object>>>();
//		System.out.println("sendData : " + sendData);
		
		String strSPName = (String) sendData.get(0).get("SpName");
		List<Map<String, Object>> strSendData = (List<Map<String, Object>>) sendData.get(0).get("sendData");
		Map<String, Object> strComData = (Map<String, Object>) sendData.get(0).get("userInfo");
		

//	    try (Connection connection = dataSource.getConnection()) {
//	        String dbUrl = connection.getMetaData().getURL();
//	        System.out.println("🌐 현재 연결된 DB URL: {}: "+dbUrl);
//	    } catch (Exception e) {
//	    	System.out.println("❌ DB URL 확인 실패: {} : "+ e.getMessage());
//	    }		
//		
		String strXml = "";
		String strComXml = "";
		try {
			strXml = comXmlService.ConvertToXml(strSendData, "ValueDataSet");
			strComXml = comXmlService.ConvertToXml(strComData,"CommonDataSet");
		}catch(Exception e) {
//			System.out.println("EXEC "+ strSPName + " " + "'"+strComXml+"','"+strXml+"'");
			e.printStackTrace();
		}
		
		System.out.println("EXEC "+ strSPName + " " + "'"+strComXml+"','"+strXml+"'");

		returnData = comSqlService.comSQLService_Multi(strSPName,"'"+strComXml+"','"+strXml+"'");
		
//		System.out.println(returnData)
		return returnData;
	}



}
