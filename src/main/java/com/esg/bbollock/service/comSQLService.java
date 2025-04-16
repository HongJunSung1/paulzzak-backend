package com.esg.bbollock.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;



@Service
public class comSQLService {

//	private String strDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";       
//	private String strUrl = "jdbc:sqlserver://211.233.6.220;database=ESG;encrypt=false;trustServerCertificate=true";
//	private String strUserID = "hong_junsung";
//	private String strPassWord = "esg1234";
	
	private String strDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";       
	private String strUrl = "jdbc:sqlserver://paulzzak-db-small.c3ouguiq215l.ap-northeast-2.rds.amazonaws.com:1433;databaseName=paulzzak;encrypt=false;trustServerCertificate=true";
	private String strUserID = "admin";
	private String strPassWord = "dksemffla1!";

	public List<Map<String, Object>> comSQLService_Single(String spName, String param) {

		List<Map<String, Object>> resultData = new ArrayList<Map<String, Object>>();
		Connection connection = null;
		PreparedStatement cs = null;
		ResultSet rs = null; 

		try {
    		connection = DriverManager.getConnection(strUrl,strUserID,strPassWord);
    		String strSQL = "EXEC " + spName + " " + param;

    		cs = connection.prepareCall(strSQL);

    		rs = cs.executeQuery();

            ResultSetMetaData metaData = rs.getMetaData();

            while (rs.next())
            {
                Map<String, Object> map = new HashMap<String, Object>();

            	for (int i = 0; i < metaData.getColumnCount(); i++)
                {
            		String column = metaData.getColumnName(i + 1);
                    map.put(column, rs.getString(column));
                }

            	resultData.add(map);
            }

			if (rs != null) rs.close();
			if (cs != null) cs.close();
			if (connection != null) connection.close();

        }catch (Exception e) {
        	System.out.println("spName : " + spName);
        	e.printStackTrace();
		}finally {
		     if ( rs != null ) try{rs.close();}catch(Exception e){}
		     if ( cs != null ) try{cs.close();}catch(Exception e){}
		     if ( connection != null ) try{connection.close();}catch(Exception e){}
		 }

		return resultData;
	}


	public ArrayList<List<Map<String, Object>>> comSQLService_Multi(String spName, String param) {

		ArrayList<List<Map<String, Object>>> resultData = new ArrayList<List<Map<String, Object>>>();
		Connection connection = null;
		PreparedStatement cs = null;
		ResultSet rs = null; 

		try {
			connection = DriverManager.getConnection(strUrl,strUserID,strPassWord);
    		String strSQL = "EXEC " + spName + " " + param;
//    		System.out.println("strSQL : " + strSQL);
    		cs = connection.prepareCall(strSQL);

            boolean isExistRusult = true;
    		rs = cs.executeQuery();
    		
            while(isExistRusult) {
            	List<Map<String, Object>> tempResult = new ArrayList<Map<String, Object>>();

                while (rs.next())
                {
                	ResultSetMetaData metaData = rs.getMetaData();

                    Map<String, Object> map = new HashMap<String, Object>();

                	for (int i = 0; i < metaData.getColumnCount(); i++)
                    {
                		String column = metaData.getColumnName(i + 1);
                        map.put(column, rs.getString(column));
                    }

                	tempResult.add(map);
                }

                resultData.add(tempResult);
                
                if (rs != null) rs.close();
				isExistRusult = cs.getMoreResults();
				if (isExistRusult == true) {
					rs = cs.getResultSet();
				}
            }

			if (cs != null) cs.close();
			if (connection != null) connection.close();

        }catch (Exception e) {
        	System.out.println("spName : " + spName);
        	e.printStackTrace();
		}finally {
		     if ( rs != null ) try{rs.close();}catch(Exception e){}
		     if ( cs != null ) try{cs.close();}catch(Exception e){}
		     if ( connection != null ) try{connection.close();}catch(Exception e){}
		 }
		
		return resultData;
	}

//	public ArrayList<List<Map<String, Object>>> comSQLService_Multi(String spName, String param) {
//
//	    ArrayList<List<Map<String, Object>>> resultData = new ArrayList<>();
//	    Connection connection = null;
//	    PreparedStatement cs = null;
//	    ResultSet rs = null; 
//
//	    try {
//	        connection = DriverManager.getConnection(strUrl, strUserID, strPassWord);
//	        String strSQL = "EXEC " + spName + " " + param;
//	        System.out.println("SQL 실행: " + strSQL);
//
//	        cs = connection.prepareCall(strSQL);
//	        System.out.println(cs);
//	        boolean isExistResult = cs.execute();
//	        
//	        while (isExistResult) {
//	            rs = cs.getResultSet();
//	            List<Map<String, Object>> tempResult = new ArrayList<>();
//	            System.out.println(rs);
//	            if (rs != null) {
//	                ResultSetMetaData metaData = rs.getMetaData();
//	                while (rs.next()) {
//	                    Map<String, Object> map = new HashMap<>();
//
//	                    for (int i = 1; i <= metaData.getColumnCount(); i++) {
//	                        String column = metaData.getColumnName(i);
//	                        String value = rs.getString(column);
//	                        map.put(column, value != null ? value : ""); // NULL 값 체크
//	                    }
//
//	                    tempResult.add(map);
//	                }
//	                rs.close(); // 여기서 닫기
//	            }
//
//	            resultData.add(tempResult);
//	            isExistResult = cs.getMoreResults();
//	        }
//
//	    } catch (Exception e) {
//	        System.out.println("spName : " + spName);
//	        e.printStackTrace();
//	    } finally {
//	        try { if (rs != null) rs.close(); } catch (Exception e) {}
//	        try { if (cs != null) cs.close(); } catch (Exception e) {}
//	        try { if (connection != null) connection.close(); } catch (Exception e) {}
//	    }
//
//	    return resultData;
//	}


}