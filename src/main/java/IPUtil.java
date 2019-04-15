
import java.net.InetAddress;
import javax.servlet.http.HttpServletRequest;

public class IPUtil
{
    public static String getLocalAddr(HttpServletRequest request){
        try{
            StringBuffer addr = new StringBuffer();
            //InetAddress inet = InetAddress.getLocalHost(); 
            addr.append("http://");
            addr.append(request.getServerName());
            addr.append(":".concat(String.valueOf(request.getServerPort())));
            if(!request.getContextPath().equals("")){
                addr.append(request.getContextPath());
            }
            return addr.toString();
        }catch(Exception e){
            e.printStackTrace();
            return "";
        }
    }

    public static String getLocalIP(){
        try{
            InetAddress inet = InetAddress.getLocalHost();
            return inet.getHostAddress();
        }catch(Exception e){
            return "";
        }
    }
} 
