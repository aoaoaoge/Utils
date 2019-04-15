import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * jackson工具类
 * <p>Title: JacksonMapper.java</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: es</p>
 * <p>team: ga</p>
 * @date 2017年5月26日下午4:34:35
 * @version 1.0
 */
public class JacksonMapper {   
  
    private static  ObjectMapper mapper = null;
    private JacksonMapper() {   
  
    }   
    /**
     * 
    *获取mapper实例
    * @Title: getInstance
    * @return
    * @return ObjectMapper
    * @throws
    * @author zengyong
     */
    public static ObjectMapper getInstance() {
        if (mapper == null) {
            synchronized (JacksonMapper.class) {
                if (mapper == null) {
                    mapper = new ObjectMapper();
                }
            }
        }
//    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  
//        mapper.setDateFormat(dateFormat);
    	//对任何属性都可见
    	//mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
    	//false为忽略失败的字段
    	mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);  
    	//false为不按照字母排序，默认为true
    	mapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY,false); 
        return mapper;   
    }   
  
}   