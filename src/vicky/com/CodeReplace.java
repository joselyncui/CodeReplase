package vicky.com;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeReplace {
	private static ArrayList<String> fileList = new ArrayList<String>();
	
	public static void main(String args[]){
//		String path = "D:\\test\\java";
//		String path = readDataFromConsole("�����ļ���·��:\r\n");
//		getFiles(path);
//		getList(path);
		
//		double money = 140000.99d;
//		System.out.println(money+"");
		
//		String str = "";
//		String[] arrays = str.split(";;");
//		System.out.println(arrays[0]+"  " + arrays.length);
		
		System.out.println("�� ".length());
		
		
//		File file = new File("D:\\test\\java\\com\\hs\\yjseller\\shopmamager\\ShopCarActivity.java");
		
//		GenOnClick.redFileGenOnClick(file);
//		GenFindViewById.readFileGenFindView(file);
		
//		String s = "private void next() {";
//		String regex = "[a-zA-Z]+\\s*\\(\\)";
//		Pattern p=Pattern.compile(regex);
		
//		String s = "@Click({R.id.titleLinLay,R.id.arrowImgView,R.id.bottomView})";
//		String s ="@Click(R.id.titleLinLay)";
//		String regex = "(?<=\\(\\{)[^\\}\\)]+";//ƥ��()�м������ R.id.temp;
//		String s = "public void h_fun_fun(){";
//		Pattern p=Pattern.compile(RegexUtil.FUN_NAME_REGEX); 
//		Matcher m=p.matcher(s); 
//		 
//		while(m.find()){ 
//		   System.out.println(m.group(0)); 
//		} 
		
//		List<String> list = new LinkedList<>();
//		System.out.println(list.get(1));
		
		String str = "value=http://img.shopping.vd.cn/online/upload/20170704/20170704111810_497.jpg?mdw=1632&mdh=1224;;http://img.shopping.vd.cn/online/upload/20170704/20170704111820_887.jpg?mdw=1224&mdh=1632;;http://img.shopping.vd.cn/online/upload/20170704/20170704111830_982.jpg?mdw=1224&mdh=1632";
		String[] values = str.split(";;");
		System.out.println(values.length);
//		
		for(int i = 0; i < values.length;i++){
			System.out.println(values[i]);
		}
	}
	
	static void getList(String filePath){
		File root = new File(filePath);
	    File[] files = root.listFiles();
	    for(File file:files){     
	     if(file.isDirectory()){
	      /*
	       * �ݹ����
	       */
	      getList(file.getAbsolutePath());
	     }else{
//	      fileList.add(file.getAbsolutePath());
	      redFile(file);
	      
	     }     
	    }
	}
	
	public static void redFile(File file){
		if(!file.getAbsolutePath().endsWith(".java")){
			return;
		}
		
		BufferedReader br = null;
		try{ 
			
			//��ָ�����뷽ʽ����
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
	
			 
			while (br.ready()) {
				
	            String line = br.readLine();
//	            System.out.println(line);
	            if(line.contains("Months")){
	            	System.out.println(file.getAbsolutePath());
	            	break;
	            } 
	       }
		 } catch(Exception e){
			 e.printStackTrace();
		 } finally{
			 CloseUtil.safeClose(br);
			
		 }
		
	}
	
	 /*
	  * ͨ���ݹ�õ�ĳһ·�������е�Ŀ¼�����ļ�
	  */
	 static void getFiles(String filePath){
	  File root = new File(filePath);
	    File[] files = root.listFiles();
	    for(File file:files){     
	     if(file.isDirectory()){
	      /*
	       * �ݹ����
	       */
	      getFiles(file.getAbsolutePath());
	      System.out.println("��ʾ"+filePath+"��������Ŀ¼�����ļ�"+file.getAbsolutePath());
	     }else{
	      System.out.println("��ʾ"+filePath+"��������Ŀ¼"+file.getAbsolutePath());
	      fileList.add(file.getAbsolutePath());
	      //ֻ���� activity �� fragment
	      if(file.getName().contains("Activity")|| file.getName().contains("Fragment")){
	    	  System.out.println("inner-------------------");
	    	  GenOnClick.redFileGenOnClick(file);
	    	  GenFindViewById.readFileGenFindView(file);
	      }
	      
	     }     
	    }
	 }
	 
	 	/**
	     * ��ȡ�ն�����
	     *  
	     * @param prompt 
	     * @return input string 
	     */  
	    private static String readDataFromConsole(String prompt) {  
	        Scanner scanner = new Scanner(System.in);  
	        System.out.print(prompt);  
	        return scanner.nextLine();  
	    } 
	 
	 
	 
	
}
