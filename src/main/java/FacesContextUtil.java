
import java.io.InputStream;
import java.util.Map;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * 封装对FacesContext的部分调用
 *
 */
public class FacesContextUtil {
	/**
	 * 获取ViewBean
	 *
	 * @param beanName
	 *            bean的名称
	 * @return ViewBean
	 */
	public static Object getViewBean(String beanName) {
		ExternalContext externalContext = getFacesContext()
				.getExternalContext();
		Object managedBean = null;
		// get request map.
		Map<String, Object> requestMap = externalContext.getRequestMap();

		// Lookup the current bean instance in the request scope.
		managedBean = requestMap.get(beanName);
		if (managedBean != null) {
			return managedBean;
		}

		// Bean is not in the request scope. Get the session map then.
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		// Lookup the current bean instance in the session scope.
		managedBean = sessionMap.get(beanName);

		if (managedBean != null) {
			return managedBean;
		}

		// Bean is also not in the session scope. Get the application map then.
		Map<String, Object> applicationMap = externalContext
				.getApplicationMap();

		// Lookup the current bean instance in the application scope.
		managedBean = applicationMap.get(beanName);

		if (managedBean != null) {
			return managedBean;
		}

		// Create ManagedBean
		// JSF1.2 Implements

		return getFacesContext().getELContext().getELResolver()
				.getValue(getFacesContext().getELContext(), null, beanName);

		// JSF1.1 Implements
		// return
		// getFacesContext().getApplication().getVariableResolver().resolveVariable(getFacesContext(),
		// beanName);
	}

	/**
	 * 得到 FacesContext
	 *
	 * @return FacesContext
	 */
	public static FacesContext getFacesContext() {
		FacesContext facesContext = FacesContext.getCurrentInstance();

		return facesContext;
	}

	/**
	 * 得到 RequestParameterMap
	 *
	 * @return RequestParameterMap
	 */
	public static Map getRequestParameterMap() {
		return getFacesContext().getExternalContext().getRequestParameterMap();
	}

	/**
	 * 得到 HttpSession
	 *
	 * @return HttpSession
	 */
	public static HttpSession getSession() {
		HttpSession session = (HttpSession) getFacesContext()
				.getExternalContext().getSession(false);
		return session;
	}

	/**
	 * 得到 HttpServletRequest
	 *
	 * @return HttpServletRequest
	 */
	public static HttpServletRequest getRequest() {
		HttpServletRequest request = (HttpServletRequest) getFacesContext()
				.getExternalContext().getRequest();
		return request;
	}

	public static HttpServletResponse getResponse() {
		HttpServletResponse response = (HttpServletResponse) getFacesContext()
				.getExternalContext().getResponse();
		return response;
	}

	public static ClassLoader getDefaultClassLoader() {
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		} catch (Throwable ex) {
			// Cannot access thread context ClassLoader - falling back to system
			// class loader...
		}
		if (cl == null) {
			// No thread context class loader -> use class loader of this class.
			cl = FacesContextUtil.class.getClassLoader();
		}
		return cl;
	}

	public static InputStream getClassPathResourceAsStream(String classpath) {
		return getDefaultClassLoader().getResourceAsStream(classpath);
	}

}
