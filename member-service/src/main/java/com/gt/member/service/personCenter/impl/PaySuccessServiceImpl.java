package com.gt.member.service.personCenter.impl; /**
 * P 2016年10月11日
 *//*
package com.gt.controller.personCenter;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;




import com.gt.dao.common.WxCardShelvesMapper;
import com.gt.dao.member.MemberMapper;
import com.gt.dao.personCenter.PaySuccessCardMapper;
import com.gt.dao.personCenter.PaySuccessLogMapper;
import com.gt.dao.personCenter.PaySuccessMapper;
import com.gt.dao.user.FenbiFlowRecordMapper;
import com.gt.dao.util.DaoUtil;
import com.gt.entity.member.Member;
import com.gt.entity.personCenter.PaySuccess;
import com.gt.entity.personCenter.PaySuccessCard;
import com.gt.entity.personCenter.PaySuccessLog;
import com.gt.entity.user.FenbiFlowRecord;
import com.gt.entity.user.WxPublicUsers;
import com.gt.util.CommonUtil;
import com.gt.util.DateTimeKit;
import com.gt.util.JedisUtil;

*//**
 * @author pengjiangli  
 * @version 
 * 创建时间:2016年10月11日
 * 
 *//*
@Service
public class PaySuccessServiceImpl implements PaySuccessService{

	private static final Logger LOG = Logger
			.getLogger(PaySuccessServiceImpl.class);

	@Autowired
	private PaySuccessMapper paySuccessMapper;

	@Autowired
	private PaySuccessLogMapper paySuccessLogMapper;

	@Autowired
	private DaoUtil daoUtil;

	@Autowired
	private PaySuccessCardMapper paySuccessCardMapper;

	@Autowired
	private PersonCenterService personCenterService;

	@Autowired
	private WxCardShelvesMapper wxCardShelvesMapper;

	@Autowired
	private FenbiFlowRecordMapper fenbiFlowRecordMapper;
	
	@Autowired
	private MemberMapper memberMapper;
	
	
	*//**
	 * 统计类型 0消费金额 1消费次数 2当次消费金额
	 *//*
	@Override
	public Map<String, Object> findPaySuccess(Integer model, Integer publicId,
			Integer memberId, String orderNo, Date date, Double totalMoney,Integer isSave,Integer isCount) {
		Map<String, Object> returnMap=new HashMap<String, Object>();
		try {
			Member member=memberMapper.selectByPrimaryKey(memberId);
			
			PaySuccess paysuccess=paySuccessMapper.findBybusIdAndModel(member.getBusid(),model);
			if(CommonUtil.isEmpty(paysuccess)){
				returnMap.put("code", -1);
				return returnMap;
			}
			
			JedisUtil.set(orderNo, orderNo);
			
			
			//清除上月数据
			String beginDate=DateTimeKit.getCurrentYear()+"-"+DateTimeKit.getCurrentMonth()+"-01 00:00:00";
			String sql="delete from t_pay_successlog where memberId="+memberId +" and date<='"+beginDate+"'";
			daoUtil.execute(sql);
			
			if(isSave==1){
				PaySuccessLog log=new PaySuccessLog();
				log.setMemberid(memberId);
				log.setModel(model);
				log.setTotalmoney(totalMoney);
				log.setDate(date);
				log.setOrderno(orderNo);
				paySuccessLogMapper.insertSelective(log);
			}
			
			if(paysuccess.getCounttype()==2){
				returnMap.put("code", 2);
				returnMap.put("orderMoneys", totalMoney);
				returnMap.put("orderMoney", totalMoney);
				return returnMap;
			}
			
			String endDate=DateTimeKit.getDateTime();
			StringBuffer sb=new StringBuffer("SELECT COUNT(id) as count,SUM(totalMoney) as totalMoneys FROM t_pay_successlog");
			sb.append(" where model="+model +" and memberId="+memberId +" and date>='"+beginDate +"' and date<='"+endDate +"'");
			Map<String, Object> map=daoUtil.queryForMap(sb.toString());
			if(paysuccess.getCounttype()==0){
				returnMap.put("code", paysuccess.getCounttype());
				if(CommonUtil.isEmpty(map.get("totalMoneys"))){
					if(isCount==1){
						returnMap.put("orderMoneys", totalMoney);
					}else{
						returnMap.put("orderMoneys", 0);
					}
					
				}else{
					if(isCount==1){
						returnMap.put("orderMoneys", CommonUtil.toDouble(map.get("totalMoneys"))+totalMoney);
					}else{
						returnMap.put("orderMoneys", map.get("totalMoneys"));
					}
					
				}
				
			}else{
				returnMap.put("code", paysuccess.getCounttype());
				if(isCount==1){
					returnMap.put("orderMoneys", CommonUtil.toInteger(map.get("count"))+1);
				}else{
					returnMap.put("orderMoneys", map.get("count"));
				}
			}
		} catch (Exception e) {
			returnMap.put("code", -1);
		}
		returnMap.put("orderMoney", totalMoney);
		return returnMap;
	}
	
	
	@Transactional(rollbackFor = { Exception.class })
	public Map<String, Object> savePaySuccess(Map<String, Object> param,int busId) throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			PaySuccess paysuccess = (PaySuccess) JSONObject.toBean(
					JSONObject.fromObject(param.get("jsonStr")),
					PaySuccess.class);
			paysuccess.setBusid(busId);
			if(CommonUtil.isNotEmpty(paysuccess.getImage())){
				paysuccess.setImage(paysuccess.getImage().split("/upload")[1]);
			}
			if (CommonUtil.isEmpty(paysuccess.getId())) {
				paySuccessMapper.insertSelective(paysuccess);
				if (paysuccess.getOptiontype().intValue() == 2) {
					List<PaySuccessCard> paySuccessCards = JSONArray.toList(
							JSONArray.fromObject(paysuccess.getCard()),
							PaySuccessCard.class);
					for (PaySuccessCard paySuccessCard : paySuccessCards) {
						paySuccessCard.setPaysuccessid(paysuccess.getId());
						this.paySuccessCardMapper
								.insertSelective(paySuccessCard);
					}
				}
			} else {
				this.paySuccessMapper.updateByPrimaryKeySelective(paysuccess);
				if (paysuccess.getOptiontype().intValue() == 2) {
					this.paySuccessCardMapper.deleteBySuccessId(paysuccess
							.getId());
					List<PaySuccessCard> paySuccessCards = JSONArray.toList(
							JSONArray.fromObject(paysuccess.getCard()),
							PaySuccessCard.class);
					for (PaySuccessCard paySuccessCard : paySuccessCards) {
						paySuccessCard.setPaysuccessid(paysuccess.getId());
						this.paySuccessCardMapper
								.insertSelective(paySuccessCard);
					}
				}
			}
			
			List<FenbiFlowRecord> list=fenbiFlowRecordMapper.findByUserIdAndFreezeTypeAndPkid(busId, 32, 1);
			if(CommonUtil.isEmpty(list) || list.size()==0){
				//支付有礼粉币冻结
				FenbiFlowRecord fenbi = new FenbiFlowRecord();
				fenbi.setBusUserId(busId);
				fenbi.setRecType(1);
				fenbi.setRecCount(0.0);
				fenbi.setRecUseCount(0.0);
				fenbi.setRecDesc("支付有礼");
				fenbi.setRecFreezeType(32);
				fenbi.setRecFkId(1);
				fenbi.setRollStatus(1);
				fenbi.setFlowType(0);
				fenbi.setFlowId(0);
				fenbiFlowRecordMapper.insertSelective(fenbi);
			}
			

			map.put("result", true);
			map.put("message", "保存成功");
		} catch (Exception e) {
			LOG.error("保存数据失败", e);
			throw new Exception();
		}
		return map;
	}

}
*/