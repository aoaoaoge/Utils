
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class FtpUtil {

	/**
	 * 从FTP服务器的指定目录上获取与指定模式匹配的文件名
	 *
	 * @param ftp
	 *            FTP服务器连接对象
	 * @param remotePath
	 *            远程目录
	 * @param filenamePattern
	 *            文件匹配模式
	 * @return 匹配成功的文件名数组
	 * @throws IOException
	 *             如果FTP服务器读取出错
	 */
	public static String[] getMatchedString(FTPClient ftp, String remotePath,
											String filenamePattern) throws IOException {
		String[] str = ftp.listNames(remotePath);
		Pattern p = Pattern.compile(filenamePattern);
		ArrayList matchedList = new ArrayList();
		for (int i = 0; i < str.length; i++) {
			if (p.matcher(str[i]).matches())
				matchedList.add(str[i]);
		}
		return (String[]) matchedList.toArray(new String[0]);
	}

	/**
	 * 获取文件名数组中最大文件名（按ascii码大小）
	 *
	 * @param str
	 *            文件名数组
	 * @return 最大的文件名
	 */
	public static String getMaxString(String[] str) {
		Arrays.sort(str);
		return str[str.length - 1];
	}

	/**
	 * 获取FTP连接对象
	 *
	 * @param server
	 *            FTP服务器地址
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @return FTP连接对象
	 * @throws SocketException
	 *             如果FTP连接出错
	 * @throws IOException
	 *             如果FTP连接出错
	 */
	public static FTPClient getFTPClient(String server, String username,
										 String password) throws SocketException, IOException {
		FTPClient ftp = new FTPClient();
		ftp.setControlEncoding("GB2312");
		//ftp.setControlEncoding("UTF-8");//测试环境239 WINDOWS
		int reply;
		ftp.connect(server);
		reply = ftp.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			ftp.disconnect();
			throw new SocketException("连接失败。");
		}
		if (!StringUtils.isBlank(username) && !StringUtils.isBlank(password)) {
			boolean  isLogin = ftp.login(username, password);
		} else {
			// 匿名登录
			ftp.login("anonymous", "12345678@163.com");
		}
		return ftp;
	}

	/**
	 * 退出FTP服务器（释放连接）
	 *
	 * @param ftp
	 *            FTP连接对象
	 * @throws IOException
	 *             如果退出出错
	 */
	public static void logout(FTPClient ftp) throws IOException {
		ftp.logout();
		if (ftp.isConnected())
			ftp.disconnect();
	}



	public static boolean existDirectory(FTPClient ftp,String ppath,String cpath) throws IOException{
		boolean flag = false;
		ftp.changeWorkingDirectory(ppath);
		FTPFile[] ftpFileArr = ftp.listFiles();
		for(FTPFile file : ftpFileArr){
			if(file.isDirectory() && file.getName().equals(cpath)){
				flag = true;
				break;
			}
		}
		return flag;
	}

}
