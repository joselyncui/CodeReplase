package vicky.com;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class GenOnClick {
	public static void redFileGenOnClick(File file){
		List<OnClickModel> clicks = getOnClick(file);
		//如果没有click 则直接返回
		if(clicks.isEmpty()){
			return;
		}
		
		File temp = null;
		BufferedReader br = null;
		BufferedWriter bw = null;
		boolean isInitUiContains = false;
		
		try{ 
			
			//不指定编码方式乱码
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
			temp = new File(file.getParentFile(),"temp-"+ file.getName());
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(temp),"UTF-8"));
			
	        StringBuilder genClickBuilder = new StringBuilder();
			 
			while (br.ready()) {
				
	            String line = br.readLine();
//	            System.out.println(line);
	            if(line.contains("@EActivity")){
	            	bw.write("\r\n"+line);
	            	line = br.readLine().trim();
//	            	if(!line.contains("View.OnClickListener")){
//	            		String str = line.contains("implements")?",View.OnClickListener":" implements View.OnClickListener";
//	            		line = line.substring(0, line.length()-1);
//	            		line += str;
//	            		line += " {";
//	            	}
	            } else if(line.contains("@Click")){//查找@Click 跳过
	            	 continue;
	             }else if(line.contains("initUI")){
	            	isInitUiContains = true;
	            	bw.write("\r\n"+line);
	            	buildClickListenerStrs(clicks, genClickBuilder);
	            	line = genClickBuilder.toString();
	            } else if(!br.ready()&& ! isInitUiContains&&!"".equals(genClickBuilder.toString())){
	            	 buildClickListenerStrs(clicks, genClickBuilder);
	            	 StringBuilder genUIBuilder = new StringBuilder();
	            	 genUIBuilder.append("@Override\r\npublic void initUI(){\r\n");
	            	 genUIBuilder.append(genClickBuilder);
	            	 genUIBuilder.append("}\r\n");
	            	 
	            	 bw.write("\r\n"+genUIBuilder.toString());
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
	
	public static List<OnClickModel> getOnClick(File file){
		 BufferedReader br = null;
		 List<OnClickModel> models = new ArrayList<>();
		 
		 try{ 
			 br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"utf-8"));
			 
			 while (br.ready()) {
				 
	             String line = br.readLine();
	             String[] ids = {};
	             if(line.contains("@Click")){//查找@Click
	            	 if(line.contains("{")){//@Click({R.id.titleLinLay,R.id.arrowImgView,R.id.bottomView})
	            		 String idStr = RegexUtil.getRegexStr(line, RegexUtil.IDS_REGEX);
	            		ids = idStr.split(",");
	            	 } else {//@Click(R.id.titleLinLay)
		            	 ids = new String[]{RegexUtil.getRegexStr(line, RegexUtil.ID_REGEX)};
	            	 }
	            	 
	            	 line = br.readLine();
	            	 String funName = RegexUtil.getRegexStr(line,RegexUtil.FUN_NAME_REGEX);
	            	 
	            	 for(int i = 0,size = ids.length;i<size;i++){
	            		 OnClickModel model = new OnClickModel();
	            		 model.idStr = ids[i];
	            		 model.funName = funName;
		            	 models.add(model);
	            	 }
	             }
			 }
		 } catch(Exception e){
			 e.printStackTrace();
		 } finally{
			 CloseUtil.safeClose(br);
		 }
		 
		 return models;
	 }
	
	private static void buildClickListenerStrs(List<OnClickModel> clicks,StringBuilder builder){
		for(int i = 0, size = clicks.size(); i < size ; i++){
			buildClickListenerStr(clicks.get(i),builder);
    	}
	}
	private static void buildClickListenerStr(OnClickModel model,StringBuilder builder){
		builder.append("findViewById(");
		builder.append(model.idStr);
		builder.append(").setOnClickListener(new View.OnClickListener(){\r\n");
		builder.append("@Override\r\npublic void onClick(View v) {\r\n");
		builder.append(model.funName);
		builder.append(";\r\n");
		builder.append("}\r\n});\r\n");
	}
	
	
}
