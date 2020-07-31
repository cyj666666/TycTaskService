package amarsoft.com.utils;

public class LogsUtils
{
  public static String getSqlLog(String sql, Object... params){
	  String newSql = "Current Execute SQL:[";
	  sql = sql.trim();
	  String[] sqls = sql.split("\\?");
	  for(int i=0;i<sqls.length;i++){
		  if(i==sqls.length-1) {
			  newSql+=sqls[i];
		  }else{
			  newSql+=sqls[i]+"'"+params[i]+"'";
		  }
	  }
	  if(sql.endsWith("?")){
		  newSql += "'"+params[params.length-1]+"'";
	  }
	  return newSql+"]";
  }
  
}
