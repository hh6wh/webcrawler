package wrappers;
import java.util.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.*;
import java.net.*;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Example program to list links from a URL.
 */
public class Crawler{
	
	private static String[] postUrls = new String[3500];
	private static String[] storefilename = new String[3000];
	private static String postAddr = "http://www.medhelp.org/posts";
	private static String wrongfulAddr = "http://www.medhelp.org/posts/new_with_new_subject";
	private static Wrapper4MedHelp wrapper = new Wrapper4MedHelp();
	private static int page = 1;
	private static int numofurl = 0;
	public static void main(String[] args) throws IOException {

//        ArrayList<String> list = new ArrayList<String>();
//        
//        list.add("http://www.medhelp.org/forums/Allergy/show/67");
//        list.add("http://www.medhelp.org/forums/Addiction-Alcohol-Drug-Rehab/show/1232");
//        list.add("http://www.medhelp.org/forums/Back--Neck/show/63");
//        
//         for(String elem : list){
//          for (page = 1; page < 2; page++){
//        	  if(numofurl < 10){	
//        		String url = elem + "?page="+page;
//        		
//        		System.out.println(url);
//        		//connect to the anchor
//        		Document doc = Jsoup.connect(url).timeout(0).get();
//        		Elements links = doc.select("a[href]");
//        	
//        		for (Element link : links) {
//        			String addr = link.attr("abs:href");
//        			//pick out the url of the posts
//        			if ((addr.indexOf(postAddr) != -1 )&&((addr.indexOf(wrongfulAddr)) == -1) && (addr.indexOf("post_")== -1)&&(addr.indexOf("Please-read-before-posting")== -1)&&(addr.indexOf("Please-Read-Before-Posting") == -1))	
//        			{	
//        				if(addr.indexOf("?page=")== -1){
//            				addr = addr + "?page="+1;
//            			}
//        				
//        				//add the url of posts found in anchor
//        				postUrls[numofurl] = addr; 
//                		System.out.println(postUrls[numofurl]);
//                		//find and add url of different pages found in posts
//        				numofurl = countpagewithinposts(addr, numofurl, postUrls);
//        				numofurl ++;
//        			}
//        	
//        			//be polite
//        			try {
//        				Thread.sleep(10);                 //1000 milliseconds is one second.
//        			} catch(InterruptedException ex) {
//        				Thread.currentThread().interrupt();
//        			}
//        		}
//        	}else
//        	{
//        		break;
//        	}
//          }
//        }
//        for (int i = 0; i < 10; i++){
//        	System.out.println(postUrls[i]);
//        	URL finalurl = new URL(postUrls[i]);
//        	storefilename[i] = url2html(finalurl, page, i);
//        }
        
//////////////////testing//////////////////////////////////        
		File folder = new File("/Users/LH/Documents/Information Retrival/MedForums/html");
		File[] listOfFiles = folder.listFiles();
		String[] folderhtml = new String[3500];
		
		for (int i = 0; i < listOfFiles.length; i++) {
			  String name = "";
			  String temp[];
			  int getpage;
			  int aaa;
		      if (listOfFiles[i].isFile()) {
		        System.out.println("File " + listOfFiles[i].getName());
		        name = listOfFiles[i].getName();
		        if(name.indexOf(".html") != -1){
		        	temp = listOfFiles[i].getName().split(".html");
		        	aaa = temp[0].lastIndexOf("-");
		        	getpage = Integer.parseInt(temp[0].substring(aaa+1, temp[0].length()));
		        	folderhtml[getpage] = listOfFiles[i].getName();
		        }
		      }	      
		    }
		
		
        for (int k = 2998; k > -1; k--){
        	System.out.println(folderhtml[k]);
        	wrapper.parseHTML("./html/"+folderhtml[k]);
        }
/////////////////////////////////////////////////////////////
        
//        for (int k = 9; k > -1; k--){
//        	System.out.println(storefilename[k]);
//        	wrapper.parseHTML(storefilename[k]);
//        }
	}
    
	
	public static int countpagewithinposts(String addr, int numofurl, String[] postUrls){
		while(addr != null){
			try{	
				Document docwithinpage = Jsoup.connect(addr).timeout(0).get();
				Elements linkswithinpage = docwithinpage.select("a[class=msg_next_page]");
				
				int getredofduplicate = 0;
				if(!linkswithinpage.isEmpty())
	    		{
	    			for(Element linkwithinpage : linkswithinpage){	
						if(getredofduplicate ==1){
								numofurl ++;
	    						postUrls[numofurl] = linkwithinpage.attr("abs:href"); 
	    						addr = postUrls[numofurl];
	    						System.out.println(postUrls[numofurl]);
	    						
						}
	    						getredofduplicate ++;
	    			}
	    			linkswithinpage.append(addr);
	    		}else{
	    			 addr = null;
	    		}	
			}catch(IOException e){
				System.out.println("error!");
			}
		
		}
		return numofurl;
	}
	
	
	public static String url2html(URL suburl, int page, int numofurl){
		String filename = "";
		try{
			filename = writeHTML(suburl, numofurl);
			return filename;
		}catch(Exception e){
			return "false";
		}
		
	}
	
	
	public static String writeHTML(URL url, int n) throws Exception{
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		String inputLine = "";
		String[] temp;
		String delimiter = "/";
		temp = url.toString().split(delimiter);
		String topic1 = temp[4];
		String topic2 = temp[5];
		String[] temp2;
		String delimiter2 = "=";
		temp2 = url.toString().split(delimiter2);
		String topic3 = temp2[1];
		String fname = "hh6wh-"+topic1+"-"+topic2+"-"+"page"+topic3+"-"+n+".html";
		File file = new File(fname);
        while ((inputLine = in.readLine()) != null){
            saveStringToHTML(inputLine, file);
            saveStringToHTML("\n", file);
            System.out.println(inputLine);
        }
        in.close();
        return "hh6wh-"+topic1+"-"+topic2+"-"+"page"+topic3+"-"+n;
	}
	
	
	public static void saveStringToHTML(String s, File file) {
	    FileWriter fileWriter = null;
	    try {
	      fileWriter = new FileWriter(file, true); //turn on append mode 
	      fileWriter.write(s);
	      fileWriter.close();
	    } catch (IOException ex) {
            Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fileWriter.close();
            } catch (IOException ex) {
                Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
	      }    
}
