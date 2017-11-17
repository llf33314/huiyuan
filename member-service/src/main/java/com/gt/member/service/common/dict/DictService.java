package com.gt.member.service.common.dict;

import com.gt.member.exception.BusinessException;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;

/**
 * 字典里面方法，如图片格式要求
 * @author Administrator
 *
 */
public interface DictService {
	
	
	
    
    /**
     * 根据类型获取字典详细信息
     * @param type
     * @return
     */
    public SortedMap<String, Object> getDict(String type) throws BusinessException;


    /**
     * 根据类型获取字典详细信息
     * @param type
     * @return
     */
    public List<Map<String, Object>> getDictbyList(String type) throws BusinessException;



    /**
     * 根据类型获取字典的值
     * @param type
     * @return
     */
    public String getDictRuturnValue(String type,Integer key) throws BusinessException;
    
    /**
     * userid 商家id，level：商家等级，style：类型，dictstyle字典等级与数量关联
     * @param userid
     * @param level
     * @param style
     * @param dictstyle
     * @return
     */
    public String dictBusUserNum(Integer userid, Integer level, Integer style, String dictstyle);
   
	
	/**
	 * 根据用户id获取该用户是否是管理 0是管理，1不是管理
	 * @param userid
	 * @return
	 */
	public Integer shopuserid(Integer userid);
	/**
	 * 根据子账号bus_user的用户，查找主账号bus_user 中的最初主账号的id
	 * @param user_id
	 * @return
	 */
	public Map<String,Object> pidUserMap(Integer user_id);
	
	
	/**
	 * 根据子账号bus_user的用户，查找主账号bus_user 中的最初主账号的id
	 * @param user_id
	 * @return
	 */
	public Integer pidUserId(Integer user_id);

}
