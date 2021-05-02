/**
* FAIR Maturity Indicators Generator for NanoSafety Data Reusability
* 
*Copyright (C) 2021  Ammar Ammar <ammar257ammar@gmail.com> ORCID:0000-0002-8399-8990
*
*This program is free software: you can redistribute it and/or modify
*it under the terms of the GNU Affero General Public License as published by
*the Free Software Foundation, either version 3 of the License, or
*(at your option) any later version.
*
*This program is distributed in the hope that it will be useful,
*but WITHOUT ANY WARRANTY; without even the implied warranty of
*MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*GNU Affero General Public License for more details.
*
*You should have received a copy of the GNU Affero General Public License
*along with this program.  If not, see <http://www.gnu.org/licenses/>.
*
*/

package io.github.ammar257ammar.maturityIndicatorsGenerator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.bind.DatatypeConverter;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.LinkedHashModel;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFHandlerException;
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.helpers.StatementCollector;
import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jsonldjava.core.JsonLdOptions;
import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.utils.JsonUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;
import io.github.ammar257ammar.maturityIndicatorsGenerator.entity.GoodExample;

/**
 * A utility class to generate maturity indicatrs markdown and nanopublication from Java properties files, and to generate a JSON representation of them 
 * 
 * @author Ammar Ammar
 *
 */

public class MiUtils {
	
	public static void generateJsonFromMiLists(String path, String destJsonFile) throws FileNotFoundException, IOException {
		
		File[] miLists = new File(path).listFiles();
				
		JSONObject root = new JSONObject();    	

		JSONArray jsonMiLists = new JSONArray();    	
				
		for(File miList: miLists){
			
			if(miList.isDirectory() && !miList.getName().equals("misc")) {
				
				File[] miProps = new File(miList.getAbsolutePath()).listFiles();

				JSONObject jsonListDesc = new JSONObject();    	

				JSONObject jsonRefPublication = new JSONObject();    	

				JSONObject jsonRefPublicationId = new JSONObject();    	

				JSONArray jsonListItems = new JSONArray();
				
				for(File miProp: miProps) {
										
					if(miProp.isFile() && miProp.getName().equals("README.md")) {
						
						try(BufferedReader br = new BufferedReader(new FileReader(miProp))){
							
							boolean titePassed = false;
							
							String line;
							
							while((line = br.readLine()) != null) {
								
								if(line.startsWith("## List")) {
									titePassed = true;
									continue;
								}else {
									if(titePassed && !line.trim().equals("")) {
										
										jsonListDesc.put("title", line.trim());
										
										titePassed = false;
									}
								}
								
								if(line.startsWith("**Title:**")) {
									jsonRefPublication.put("title", line.replace("**Title:**", "").trim());
								}
								
								if(line.startsWith("**Reference Website:**")) {
									jsonRefPublicationId.put("URL", line.replace("**Reference Website:**", "").trim());
									jsonRefPublicationId.put("type", "Website");
								}
								
								if(line.startsWith("**DOI:**")) {
									jsonRefPublicationId.put("URL", line.replace("**DOI:**", "").trim());
									jsonRefPublicationId.put("type", "DOI");
								}
								
								if(line.startsWith("* **DOI SHA256:**")) {
									jsonRefPublicationId.put("SHA-256", line.replace("* **DOI SHA256:**", "").trim());
									jsonListDesc.put("id", line.replace("* **DOI SHA256:**", "").trim().subSequence(0, 10));
								}
							}							
						}
						
						jsonRefPublication.put("reference id", jsonRefPublicationId);
						jsonListDesc.put("reference", jsonRefPublication);

						
					}else if(miProp.isFile() && miProp.getName().endsWith(".mi")) {
						
						Properties properties = new Properties();
						 
						JSONObject jsonListItem = new JSONObject();    	

						try(InputStream inputStream = new FileInputStream(miProp)){
						 
							properties.load(inputStream);	
														
							String jsonExample = properties.getProperty("GOOD_PRACTICE_EXAMPLE");
							
							jsonExample = jsonExample.replace("Dataset DOI", "http://example.org");
							jsonExample = jsonExample.replace("Dataset URL", "https://example.org");

							Object jsonObject = JsonUtils.fromString(jsonExample);

							Object compact = JsonLdProcessor.compact(jsonObject, new HashMap<>(), new JsonLdOptions());
							String compactContent = JsonUtils.toString(compact);

							ObjectMapper objectMapper = new ObjectMapper();
							objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

							GoodExample example = objectMapper.readValue(compactContent, GoodExample.class);
							
							jsonListItem.put("name", properties.getProperty("NAME"));
							jsonListItem.put("variable", example.getVariableMeasured().getName());
							
							jsonListItems.put(jsonListItem);
							
						}
					}		
				}
				
				jsonListDesc.put("maturity indicators", jsonListItems);
					
				jsonMiLists.put(jsonListDesc);		
			}// if is directory
		}// for folders
		
		root.put("lists", jsonMiLists);
		
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(destJsonFile))){
			writer.write(root.toString(4));
		}
	} 
	
	public static int generateMaturityIndicators(String path, String destPath, int countUpdated) throws IOException{
		
		Map<String, String> map = new HashMap<String, String>();
		
		File[] miProps = new File(path).listFiles();
				
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		int numOfMi = 0;
		
		for(File propFile: miProps){
			
			if(propFile.isDirectory()) {
				
				File destFolder = new File(destPath+"/"+propFile.getName());
				
				if(!destFolder.exists()) {
					if(destFolder.mkdir()) {
						countUpdated = generateMaturityIndicators(propFile.getAbsolutePath(), destFolder.getAbsolutePath(), countUpdated);
					}
				}else {
					countUpdated = generateMaturityIndicators(propFile.getAbsolutePath(), destFolder.getAbsolutePath(), countUpdated);
				}
				
			}else if(propFile.getName().endsWith("mi")){
					
				numOfMi++;
				
				String propName = propFile.getName().substring(0,propFile.getName().lastIndexOf("."));
				
				File targetIndicatorNP = new File(destPath+"/"+propName);
				File targetIndicatorMD = new File(destPath+"/"+propName+".md");
								
				if((!targetIndicatorNP.exists() && !targetIndicatorMD.exists()) ||
						(targetIndicatorNP.exists() && targetIndicatorMD.exists() &&
								propFile.lastModified() > targetIndicatorMD.lastModified() &&
								propFile.lastModified() > targetIndicatorNP.lastModified())){

					System.out.println("WORKING with: "+propFile.getName());
		
					Properties properties = new Properties();
					 
					try(InputStream inputStream = new FileInputStream(propFile);
							  BufferedWriter bwMD = new BufferedWriter(new FileWriter(targetIndicatorMD));
							  BufferedWriter bwNP = new BufferedWriter(new FileWriter(targetIndicatorNP))){
					 
						properties.load(inputStream);							
						map.put(propFile.getParentFile().getName()+"/"+properties.getProperty("IDENTIFIER")+".md", properties.getProperty("NAME"));
						  
						Configuration cfg = new Configuration(new Version("2.3.30"));
						cfg.setDefaultEncoding("UTF-8");
						
					    //cfg.setDirectoryForTemplateLoading(new File("src/main/resources/templates/"));
					    cfg.setClassForTemplateLoading(App.class, "/templates/");

						Template templateMD = cfg.getTemplate("TEMPLATE_MD.stg");
						Template templateNP = cfg.getTemplate("TEMPLATE_NP.stg");

						Map<String, Object> mapMD = new HashMap<String, Object>();
						Map<String, Object> mapNP = new HashMap<String, Object>();

						mapMD.put("TITLE", "FAIR Maturity Indicator " + properties.getProperty("TITLE"));
						mapNP.put("TITLE", "FAIR Maturity Indicator " + properties.getProperty("TITLE"));

						mapMD.put("AUTHORS", getAuthorLines(properties));
						mapNP.put("AUTHORS_ORCIDS", getAuthorOrcids(properties));
						mapNP.put("AUTHORS_TRIPLES", getAuthorTriples(properties));
						mapNP.put("FIRST_AUTHOR", getFirstAuthorOrcid(properties));
						
						mapMD.put("PUBLICATION_DATE", properties.getProperty("PUBLICATION_DATE"));
						mapNP.put("PUBLICATION_DATE", properties.getProperty("PUBLICATION_DATE"));
						  
						mapMD.put("LAST_EDIT", dateFormat.format(new Date()));
						mapNP.put("LAST_EDIT", dateFormat.format(new Date()));
						  
						mapMD.put("ACCEPTED", properties.getProperty("ACCEPTED"));

						mapMD.put("IDENTIFIER", properties.getProperty("IDENTIFIER"));
						mapNP.put("IDENTIFIER", properties.getProperty("IDENTIFIER"));
						  
						mapMD.put("IDENTIFIER_DESC", properties.getProperty("IDENTIFIER_DESC"));
						mapMD.put("IDENTIFIER_URL", "https://w3id.org/fair/maturity_indicator/terms/Gen2/"+properties.getProperty("IDENTIFIER"));
						  
						mapMD.put("NAME", properties.getProperty("NAME"));
						mapMD.put("NAME_DESC", properties.getProperty("NAME_DESC"));

						mapMD.put("WHICH_PRINCIPLE", properties.getProperty("WHICH_PRINCIPLE"));
						mapNP.put("WHICH_PRINCIPLE", properties.getProperty("WHICH_PRINCIPLE"));

						mapMD.put("WHAT_MEASURED", properties.getProperty("WHAT_MEASURED"));
						mapNP.put("WHAT_MEASURED", properties.getProperty("WHAT_MEASURED").replace("\"", "\\\""));
						  
						mapMD.put("WHY_MEASURED", properties.getProperty("WHY_MEASURED"));
						mapNP.put("WHY_MEASURED", properties.getProperty("WHY_MEASURED").replace("\"", "\\\""));
						  
						mapMD.put("WHAT_MUST_PROVIDED", properties.getProperty("WHAT_MUST_PROVIDED"));
						mapNP.put("WHAT_MUST_PROVIDED", properties.getProperty("WHAT_MUST_PROVIDED").replace("\"", "\\\""));

						mapMD.put("HOW_MEASUREMENT_EXECUTED", properties.getProperty("HOW_MEASUREMENT_EXECUTED"));
						mapNP.put("HOW_MEASUREMENT_EXECUTED", properties.getProperty("HOW_MEASUREMENT_EXECUTED").replace("\"", "\\\""));
						  
						mapMD.put("WHAT_CONSIDERED_VALID_RESULTS", properties.getProperty("WHAT_CONSIDERED_VALID_RESULTS"));
						mapNP.put("WHAT_CONSIDERED_VALID_RESULTS", properties.getProperty("WHAT_CONSIDERED_VALID_RESULTS").replace("\"", "\\\""));
						  
						mapMD.put("FOR_WHICH_DIGITAL_RESOURCE", properties.getProperty("FOR_WHICH_DIGITAL_RESOURCE"));
						mapNP.put("FOR_WHICH_DIGITAL_RESOURCE", properties.getProperty("FOR_WHICH_DIGITAL_RESOURCE").replace("\"", "\\\""));

						mapMD.put("GOOD_PRACTICE_EXAMPLE", properties.getProperty("GOOD_PRACTICE_EXAMPLE"));
						mapNP.put("GOOD_PRACTICE_EXAMPLE", properties.getProperty("GOOD_PRACTICE_EXAMPLE").replace("\"", "\\\""));
						  
						mapMD.put("COMMENTS", properties.getProperty("COMMENTS"));
						mapNP.put("COMMENTS", properties.getProperty("COMMENTS").replace("\"", "\\\""));
						  
						MessageDigest md = MessageDigest.getInstance("MD5");
					    md.update(properties.getProperty("PUBLICATION_DATE").getBytes());
					    
					    byte[] digest = md.digest();
					    
					    String dateHash = DatatypeConverter.printHexBinary(digest).toLowerCase();
					    String thisIRI = "https://w3id.org/fair/maturity_indicator/np/Gen2/"+properties.getProperty("IDENTIFIER")+"/"+dateHash;
					    
						mapNP.put("THIS_IRI", thisIRI);
						mapNP.put("SUB_IRI", thisIRI+"#");
						
						templateMD.process(mapMD, bwMD);
						bwMD.flush();
						  
						templateNP.process(mapNP, bwNP);
						bwNP.flush();
						  
						// Online Trig validator
						//https://linked.bodc.ac.uk/validate/data

						if(verifyNanoPublication(targetIndicatorNP)) {
							countUpdated++;							
						}else {
							System.out.println("TRIG file: "+ targetIndicatorNP.getName() + " could not be verified");
						}
						  
					 } catch (IOException e) {
						 e.printStackTrace();
					 } catch (TemplateException e) {
						e.printStackTrace();
					} catch (NoSuchAlgorithmException e) {
						e.printStackTrace();
					} 
				}
			}
		}
		
		if(numOfMi > 0) {
			buildReadme(path, destPath, map);			
		}
		
		return countUpdated;
	}
	
	private static void buildReadme(String path, String destPath, Map<String, String> map) {

		String url = "https://github.com/ammar257ammar/NanoMaturityIndicators/blob/main/";
		StringBuffer mis = new StringBuffer();
		
		for (Map.Entry<String,String> entry : map.entrySet()) {

			mis.append("1. "+entry.getValue() + " [link]("+url+entry.getKey()+")");
			mis.append(System.lineSeparator());
		}
		
		try {
			Files.copy(Paths.get(path+"/README.md"), Paths.get(destPath+"/README.md"), StandardCopyOption.REPLACE_EXISTING);
			
			File readme = new File(destPath+"/README.md");
			
			if(readme.exists()) {
				
			    Files.write(Paths.get(readme.getAbsolutePath()), mis.toString().getBytes(), StandardOpenOption.APPEND);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static boolean verifyNanoPublication(File targetIndicatorNP) {
				
		FileInputStream fis = null;
		
		try {
			
			fis = new FileInputStream(targetIndicatorNP);
			
			RDFParser rdfParser = Rio.createParser(RDFFormat.TRIG);
			Model model = new LinkedHashModel();
			rdfParser.setRDFHandler(new StatementCollector(model));
			
			rdfParser.parse(fis, targetIndicatorNP.getAbsolutePath());
			
			return true;
			
		}catch (IOException e) {
			e.printStackTrace();
			return false;
		}catch (RDFParseException e) {
			e.printStackTrace();
			return false;
		}catch (RDFHandlerException e) {
			e.printStackTrace();
			return false;
		}
		finally {
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static String getFirstAuthorOrcid(Properties props){
		
		String author = "";
		
		if(!props.getProperty("AUTHOR1_ORCID").trim().equals("")){
			author = "orcid:"+props.getProperty("AUTHOR1_ORCID").trim();
		}
		
		return author;
	}
	
	private static String getAuthorOrcids(Properties props){
		
		List<String> orcids = new ArrayList<String>();
		
		if(!props.getProperty("AUTHOR1_ORCID").trim().equals("")){
			orcids.add("orcid:"+props.getProperty("AUTHOR1_ORCID").trim());
		}
		
		return String.join(", ", orcids);
	}
	
	private static String getAuthorLines(Properties props){
		
		List<String> orcids = new ArrayList<String>();
		
		String author1 = "";
	
		if(!props.getProperty("AUTHOR1_NAME").trim().equals("")){
			author1 += props.getProperty("AUTHOR1_NAME").trim();
		}
		
		if(!props.getProperty("AUTHOR1_ORCID").trim().equals("")){
			author1 += ", ORCID:" + props.getProperty("AUTHOR1_ORCID").trim();
		}

		if(!author1.equals("")){
			orcids.add(author1);
		}
		
		return String.join("<br>", orcids);
	}
	
	private static String getAuthorTriples(Properties props){
		
		List<String> orcids = new ArrayList<String>();
		
		if(!props.getProperty("AUTHOR1_ORCID").trim().equals("") &&
				!props.getProperty("AUTHOR1_NAME").trim().equals("")){
			
			orcids.add("orcid:"+props.getProperty("AUTHOR1_ORCID").trim() + 
					" foaf:name " +
					"\""+ props.getProperty("AUTHOR1_NAME").trim() +"\" .");
		}
		
		return String.join("\n  ", orcids);
	}

}
