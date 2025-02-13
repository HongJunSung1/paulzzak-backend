package com.esg.bbollock.service;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;



@Service
public class comXmlService {
	public String ConvertToXml(Map data, String dataset) throws ParserConfigurationException, TransformerException {
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument(); 
		Node root = document.createElement("Root");
		
		document.appendChild(root); 

		String DataSetName = dataset;
		
		//Parameter로 넘어온 Data Set이 없거나 Null 일 경우가 있기 때문에 우선 비어 있는 DataSet을 임의로 생성 한 후 Document를 생성
		Element ds = document.createElement(DataSetName);
		root.appendChild(ds);

		// Common용 Data Set이 아닌 경우에만 공통용 컬럼을 추가

		if (data.size() > 0) 
		{
			Set key = data.keySet();
			for (Iterator iterator = key.iterator(); iterator.hasNext();) {
				String keyName = (String) iterator.next();
	            String valueName = data.get(keyName).toString();

	            Element colName = document.createElement(keyName.toString());
	            colName.appendChild(document.createTextNode(valueName.toString()));
	            ds.appendChild(colName);
			}
		}
		return setDocumentToXml(document);
	}

	
	public String ConvertToXml(List data, String dataset) throws ParserConfigurationException, TransformerException {
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument(); 
		Node root = document.createElement("Root");
		String DataSetName = "DataSet";

		document.appendChild(root);

		if (data.size() > 0){ 
			for(Object mData : data) {
				
				Map stdData = (Map)mData;
				
				switch(dataset) {
	               case "CommonDataSet":
	                  DataSetName = dataset;
	                  break;
	               
	               case "ValueDataSet":
	                  DataSetName = stdData.get("DataSet").toString();
	                  stdData.remove("DataSet");
	                  break;
	               default :
	                  DataSetName = "DataSet";
	                  break;
	            }


//				Element ds = document.createElement(DataSetName); 
//				root.appendChild(ds); 
				{ 
					Set key = stdData.keySet();	
					
					if(stdData.get("grid") != null) {
						List<Map> gridName = (ArrayList<Map>)stdData.get("grid");
						for(int i = 0 ; i < gridName.size(); i++) {
							Element ds = document.createElement(DataSetName); 
							root.appendChild(ds);
							stdData = (Map) gridName.get(i);
							key = stdData.keySet();	
							
							for (Iterator iterator = key.iterator(); iterator.hasNext();) {
								String keyName = (String) iterator.next();
								String valueName = "";
								if(stdData.get(keyName) == null) {
									valueName = "";	
								}else {
									valueName = stdData.get(keyName).toString();
								}
					            Element colName = document.createElement(keyName.toString());

//					            valueName = "<![CDATA[" + valueName.toString() + "]]>";
//					            valueName = valueName.replaceAll("&lt;", "<");
//					            valueName = valueName.replaceAll("&gt;", ">");

					            colName.appendChild(document.createTextNode(valueName));
					            ds.appendChild(colName);
							}
						}
					}else {
						Element ds = document.createElement(DataSetName); 
						root.appendChild(ds);
						for (Iterator iterator = key.iterator(); iterator.hasNext();) {
							String keyName = (String) iterator.next();
							String valueName = "";
							if(stdData.get(keyName) == null) {
								valueName = "";	
							}else {
								valueName = stdData.get(keyName).toString();
							}
							Element colName = document.createElement(keyName.toString());
							
//			            valueName = "<![CDATA[" + valueName.toString() + "]]>";
//			            valueName = valueName.replaceAll("&lt;", "<");
//			            valueName = valueName.replaceAll("&gt;", ">");
							
							colName.appendChild(document.createTextNode(valueName));
							ds.appendChild(colName);
						}
					}

					
				}
			}
			
		} else {
			//Parameter로 넘어온 Data Set이 없거나 Null 일 경우에는 비어 있는 DataSet을 임의로 생성
			Element ds = document.createElement(DataSetName);
			root.appendChild(ds);

            Element colStatus = document.createElement("Status");
            colStatus.appendChild(document.createTextNode("0"));

            ds.appendChild(colStatus);

            Element colMessage = document.createElement("Message");
            colMessage.appendChild(document.createTextNode(""));

            ds.appendChild(colMessage);
		}
		return setDocumentToXml(document);
	}
	
	
	
	private String setDocumentToXml(Document doc) throws TransformerException {
		
		DOMSource xmlDOM = new DOMSource(doc);

		// XML 파일로 쓰기
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();

		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");	
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes"); //<?xml version="1.0" encoding="UTF-8" standalone="no"?> 제거
		
		StringWriter sw = new StringWriter();
		StreamResult result = new StreamResult(sw);

		transformer.transform(xmlDOM, result);
		
		String strResult = sw.toString();

		return strResult;
	}
	
	
	public String ConvertToXmlDefault(String dataset, List data) throws ParserConfigurationException, TransformerException {
	      Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument(); 
	      Node root = document.createElement("Root");
	      String DataSetName = "DataSet";

	      document.appendChild(root);

	      if (data.size() > 0) 
	      { 
	         for(Object mData : data) {
	            
	            Map stdData = (Map)mData;
	            /*
	               Data Set Name 설정
	               현재는 Common과 Value만 있지만 향후에 추가될 가능성을 고려하여 Switch문으로 작성함
	               #Default는 DataSet으로 설정
	            */
	            switch(dataset) {
	               case "CommonDataSet":
	                  DataSetName = dataset;
	                  break;
	               
	               case "ValueDataSet":
	                  DataSetName = stdData.get("DataSet").toString();
	                  stdData.remove("DataSet");
	                  break;
	               default :
	                  DataSetName = "DataSet";
	                  break;
	            }

	            Element ds = document.createElement(DataSetName); 
	            root.appendChild(ds); 
	            { 
	               // Common용 Data Set이 아닌 경우에만 공통용 컬럼을 추가
	               if(dataset != "CommonDataSet") {
	                  Element colStatus = document.createElement("Status");
	                     colStatus.appendChild(document.createTextNode("0"));
	   
	                     ds.appendChild(colStatus);
	   
	                     Element colMessage = document.createElement("Message");
	                     colMessage.appendChild(document.createTextNode(""));
	   
	                     ds.appendChild(colMessage);
	               }

	               Set key = stdData.keySet();
	               for (Iterator iterator = key.iterator(); iterator.hasNext();) {
	                  String keyName = (String) iterator.next();
	                  String valueName = "";
	                  if(stdData.get(keyName) == null) {
	                     valueName = "";   
	                  }else {
	                     valueName = stdData.get(keyName).toString();
	                  }
	                     Element colName = document.createElement(keyName.toString());

//	                     valueName = "<![CDATA[" + valueName.toString() + "]]>";
//	                     valueName = valueName.replaceAll("&lt;", "<");
//	                     valueName = valueName.replaceAll("&gt;", ">");

	                     colName.appendChild(document.createTextNode(valueName));
	                     ds.appendChild(colName);
	               }
	            }
	         }
	         
	      } else {
	         //Parameter로 넘어온 Data Set이 없거나 Null 일 경우에는 비어 있는 DataSet을 임의로 생성
	         Element ds = document.createElement(DataSetName);
	         root.appendChild(ds);

	            Element colStatus = document.createElement("Status");
	            colStatus.appendChild(document.createTextNode("0"));

	            ds.appendChild(colStatus);

	            Element colMessage = document.createElement("Message");
	            colMessage.appendChild(document.createTextNode(""));

	            ds.appendChild(colMessage);
	      }
	      return setDocumentToXml(document);
	   }
	
	
}
