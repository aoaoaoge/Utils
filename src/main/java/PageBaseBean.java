
import java.io.Serializable;

public class PageBaseBean implements Serializable {

	private static final long serialVersionUID = -8174992422594627376L;
	private int prePage; // 上一页
	private int nextPage; // 下一页
	private int maxPages; // 总页数
	private int maxItems; // 总记录条数
	private int maxPageItems; // 每页显示的最大记录条数
	private int pageNumber; // 当前页
	private int beginItemIndex; // 当前页记录开始索引
	private int endItemIndex; // 当前页记录结束索引

	public int getMaxPages() {
		return maxPages;
	}

	public void setMaxPages(int maxPages) {
		this.maxPages = maxPages;
	}

	public int getMaxItems() {
		return maxItems;
	}

	public void setMaxItems(int maxItems) {
		this.maxItems = maxItems;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getMaxPageItems() {
		return maxPageItems;
	}

	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
	}

	public int getBeginItemIndex() {
		return beginItemIndex;
	}

	public void setBeginItemIndex(int beginItemIndex) {
		this.beginItemIndex = beginItemIndex;
	}

	public int getEndItemIndex() {
		return endItemIndex;
	}

	public void setEndItemIndex(int endItemIndex) {
		this.endItemIndex = endItemIndex;
	}

	public int getPrePage() {
		return prePage;
	}

	public void setPrePage(int prePage) {
		this.prePage = prePage;
	}

	public int getNextPage() {
		return nextPage;
	}

	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}

	/*
	 * 此方法用于计算分页后各属性的具体值
	 */
	public void compute(int maxItems, int maxPageItems, int pageNumber) {
		// System.out.println("maxPageItems in compute is:"+maxPageItems);
		int maxPages = (maxItems + (maxPageItems - 1)) / maxPageItems;
		int beginItemIndex = (maxPageItems * pageNumber) - maxPageItems + 1;
		int endItemIndex = maxPageItems * pageNumber;
		if (maxPages == 0) {
			beginItemIndex = endItemIndex = 0;
		}
		if (endItemIndex > maxItems) {
			endItemIndex = maxItems;
		}
		this.setMaxPages(maxPages);
		this.setBeginItemIndex(beginItemIndex);
		this.setEndItemIndex(endItemIndex);
		this.setMaxItems(maxItems);
		this.setPageNumber(pageNumber);
		this.setPrePage(pageNumber - 1);
		this.setNextPage(pageNumber + 1);
		this.setMaxPageItems(maxPageItems);
	}

}
