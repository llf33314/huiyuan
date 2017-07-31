package com.gt.member.util;
import java.util.List;

/**
 * 分页工具类
 * @author qusk
 * @date 2015年11月10日
 * @description
 */
@SuppressWarnings("rawtypes")
public class Page
{

	/**当前页 **/
    private int curPage = 0;     
    
    /**总页数 **/
    private int pageCount = 0;    
    
    /**总条数 **/
    private int rowCount;      
    
    /**每页显示记录数 **/
    private int pageSize;   
    
    /**每页要显示的集合 **/
	private List subList;
	
	/**数据访问的地址 **/
	private String url;
	
	/**是否存在上一页 **/
	private boolean isPrePage = true;
	
	/**是否存在下一页 **/
	private boolean isNextPage = true;

    // 每页显示记录数数组
    private int[] pageSizes = new int[] { 10, 20, 30, 50, 100 };


    // 默认每页显示记录数
    public static final int defaultPageSize = 10;

    //默认当前页
    public static final int DEFAULT_CURRENT_PAGE = 1;

    public Page()
    {
    }

    /**
     * 初始化分页
     * @param curPage 当前页
     * @param pageSize 每页条数
     * @param rowCount 总条数
     * @param url 后台地址
     */
    public Page(int curPage,int pageSize,int rowCount,String url)
    {
    	this.rowCount = rowCount;
        this.pageSize = pageSize; 
        this.countMaxPage();
        if (curPage <= 0)
        {
        	this.curPage = DEFAULT_CURRENT_PAGE;
        }else{
        	if(curPage > pageCount){
        		this.curPage = pageCount;
        	}else{
        		this.curPage = curPage;
        	}
        }
        
        if (this.curPage <= 0)
        {
        	this.curPage = DEFAULT_CURRENT_PAGE;
        }
        if(this.curPage <= 1) isPrePage = false;
        if(this.curPage >= pageCount) isNextPage = false;
        this.url = url+"?curPage=";
    }




    /**
     * 计算总页数
     */
    private void countMaxPage()
    {
        if (rowCount % pageSize == 0)
        {
        	pageCount = rowCount / pageSize;
        }
        else
        {
        	pageCount = rowCount / pageSize + 1;
        }

    }

    /**
     * 获取每页显示集合
     *
     * @return
     */
    public List getSubList()
    {
        return this.subList;
    }

    /**
     * <设置每页显示集合对象>
     * <功能详细描述>
     * @param subList [参数说明]
     */
    public void setSubList(List subList)
    {
        this.subList = subList;
    }

    /**
     * 读取每页显示记录数
     *
     * @return int
     */
    public int getPageSize()
    {
        return pageSize;
    }

    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;

        this.countMaxPage();
        if (curPage > pageCount)
        {
            this.curPage = pageCount;
        }
    }

    /**
     * 读取当前显示页数
     *
     * @return int
     */
    public int getCurPage()
    {
        return curPage;
    }

    public void setCurPage(int curPage)
    {
        this.curPage = curPage;
    }

    /**
     * 读取总页数
     *
     * @return int
     */
    public int getPageCount()
    {
        return pageCount;
    }

    /**
     * 读取总记录数
     *
     * @return int
     */
    public int getRowCount()
    {
        return rowCount;
    }

    /**
     * 判断是否存在下一页
     *
     * @return boolean
     */
    public boolean isNextPageAvailable()
    {
        return curPage >= 1 && curPage < pageCount;
    }

    /**
     * 判断是否存在上一页
     *
     * @return boolean
     */
    public boolean isPreviousPageAvailable()
    {
        return curPage > 1 && curPage <= pageCount;
    }

    /**
     * 获取下一页
     *
     * @return
     */
    public int getNextPage()
    {
        int next = curPage + 1;
        if (pageCount < next)
        {
        	next = pageCount;
        }
        return next;
    }

    /**
     * 获取前一页
     *
     * @return
     */
    public int getPrevPage()
    {
        int prev = curPage - 1;
        if (prev <= 1)
        {
        	prev = 1;
        }
        return prev;
    }

    /**
     * 获取第一页
     *
     * @return
     */
    public int getFirstPage()
    {
        return 1;
    }

    /**
     * 获取最后一页
     *
     * @return
     */
    public int getLastPage()
    {
        return pageCount;
    }

    /**
     * 判断是否是首页
     *
     * @return
     */
    public boolean isFirstPage()
    {
        return (curPage == 1) ? true : false;
    }

    /**
     * 判断是否是尾页
     *
     * @return
     */
    public boolean isLastPage()
    {
        return (curPage == pageCount) ? true : false;
    }

    /**
     * 获取当前页面的开始记录
     *
     * @return
     */
    public int getBeginRecord()
    {
        int begingRecord = (this.curPage - 1) * this.pageSize;
        return (begingRecord <= 0 ? 0 : begingRecord);
    }

    /**
     * 获取分页开始值
     *
     * @param curPage
     *            int 当前页
     * @param pageSize
     *            int 每页的记录数
     * @return
     */
    public int getBeginRecord(int curPage, int pageSize)
    {
        return (curPage - 1) * pageSize;
    }

    public int[] getPageSizes()
    {
        return (null == pageSizes ? null : pageSizes.clone());
    }

    public void setPageSizes(int[] pageSizes)
    {
        this.pageSizes = pageSizes;
    }

    public void setPageCount(int pageCount)
    {
        this.pageCount = pageCount;
    }

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
	
	public boolean getIsPrePage() {
		return isPrePage;
	}

	public boolean getIsNextPage() {
		return isNextPage;
	}
	
	

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	/**
	 * 获取每页开始条数
	 * @param curPage 当前页
	 * @param pageSize 每页的条数
	 * @return
	 */
	public  int getFirstResults(){
		return (this.curPage-1) * this.pageSize;
	}
    
}
