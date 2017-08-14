package vicky.com;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {
	public static final String ID_REGEX ="(?<=\\()[^\\)]+";//ƥ��()�м������ R.id.temp
	public static final String IDS_REGEX = "(?<=\\(\\{)[^\\}\\)]+";//ƥ�䣨���м��id {R.id.temp1,R.id.temp2}
	public static final String FUN_NAME_REGEX = "[a-zA-Z_]+\\s*\\(\\)";//ƥ�䷽���� fun()
	 
	public static String getRegexStr(String str,String regex){
		 Pattern p=Pattern.compile(regex); 
   	 Matcher m=p.matcher(str); 
   	 
   	 while(m.find()){ 
   	   return m.group(0);
   	 } 
   	 return "";
	}

}
