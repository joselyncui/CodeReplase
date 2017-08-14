package vicky.com;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class GenFindViewById {
	
	 public static void readFileGenFindView(File file){
		 File temp = null;
		 BufferedReader br = null;
		 BufferedWriter bw = null;
		 boolean isContainInitUi = false;
		 StringBuilder genIdBuilder = new StringBuilder();
		 String layoutId = "";
		 boolean isActivity = true;
		 
		 try{ 
			 br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
			 temp = new File(file.getParentFile(),"temp-"+ file.getName());
			 bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(temp),"UTF-8"));
			 
			 while (br.ready()) {
				 
	             String line = br.readLine();
	             if(line.contains("androidannotations")){//过滤import
	            	 continue;
	             } else if(line.contains("@ViewById")){
	            	 String id = RegexUtil.getRegexStr(line, RegexUtil.ID_REGEX);
	            	 System.out.println("line--id " + line+"  " + id);
	            	 line = br.readLine();
	            	
	            	 genFindViewById(genIdBuilder,line,id);
	             } else if(line.contains("initUI")){
	            	 bw.write("\r\n"+line);
	            	 line = genIdBuilder.toString();
	            	 isContainInitUi = true;
	             } else if(line.contains("@AfterViews")||line.contains("@Extra")){
//	            	 continue;
	             }else if(line.contains("@EActivity")){
	            	 layoutId = RegexUtil.getRegexStr(line, RegexUtil.ID_REGEX);
	            	 System.out.println("layoutId " + layoutId);
	            	 continue;
	             } else if(line.contains("@EFragment")){
	            	 isActivity = false;
	            	 layoutId = RegexUtil.getRegexStr(line, RegexUtil.ID_REGEX);
	            	 continue;
	             } else if(line.contains("Activity_.intent")){
	            	 line = "//" + line;
	             }
	            
	             //如果读取到最后一行并且文本中没有initUI方法，则添加
	             if(!br.ready() && ! isContainInitUi&&!"".equals(genIdBuilder.toString())){
	            	 StringBuilder genUIBuilder = new StringBuilder();
	            	 genUIBuilder.append("@Override\r\npublic void initUI(){\r\n");
	            	 genUIBuilder.append(genIdBuilder);
	            	 genUIBuilder.append("}\r\n");
	            	 
	            	 bw.write("\r\n"+genUIBuilder.toString());
	             }
	             
	             
	             if(!br.ready()&& !"".equals(layoutId)){
	            	 StringBuilder genViewLayoutBuilder = new StringBuilder();
	            	 if(isActivity){//生成 getResourceById
	            		 genViewLayoutBuilder.append("@Override\r\npublic int getLayoutResID(){\r\n");
	            		 genViewLayoutBuilder.append("return " + layoutId+";\r\n");
	            		 genViewLayoutBuilder.append("}\r\n");
	            	 } else {//onCreateView
	            		 genViewLayoutBuilder.append("@Override\r\n public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {\r\n");
	            		 genViewLayoutBuilder.append("mRootView = inflater.inflate(");
	            		 genViewLayoutBuilder.append(layoutId);
	            		 genViewLayoutBuilder.append(", container, false);");
	            		 genViewLayoutBuilder.append("\r\n return mRootView;\r\n}");
	            		 
	            	 }
	            	
	            	 bw.write("\r\n"+genViewLayoutBuilder.toString());
	             }
	             bw.write("\r\n"+line);
	         }
			 
			 bw.flush();
		 } catch(Exception e){
			 e.printStackTrace();
		 } finally{
			 CloseUtil.safeClose(br);
			 CloseUtil.safeClose(bw);
			 if (temp != null) {
	             file.delete();
	             temp.renameTo(file);
	         }
		 }
		
	 }
	 
	 private static String genFindViewById(StringBuilder genIdBuilder,String line,String id){
		 int index = line.indexOf(";");
		 String newLine = line.substring(0, index);//去除注释
    	 newLine.trim();
    	 
    	 //拼接字符串  View view = (View) findViewById(R.id.view);
    	 String[] args = newLine.split(" ");
    	 int length = args.length;
    	 genIdBuilder.append(args[length-1]);
    	 genIdBuilder.append("=(");
    	 genIdBuilder.append(args[length-2]);
    	 if(null!=id&&!"".equals(id)){
    		 genIdBuilder.append(")findViewById(");
    		 genIdBuilder.append(id);
    		 System.out.println("id ---" + id);
    		 
    	 } else{
    		 genIdBuilder.append(")findViewById(R.id.");
        	 genIdBuilder.append(args[length-1]);
    	 }
    	 
    	 genIdBuilder.append(");\r\n");
    	 return genIdBuilder.toString();
	 }
	 
	 

}
