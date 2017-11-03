package com.gt.member.service.member.impl;//package com.gt.member.service.old.member.impl;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.gt.dao.member.MemberMapper;
//import com.gt.dao.member.MemberParameterMapper;
//import com.gt.entity.member.MemberEntity;
//import com.gt.entity.member.MemberParameter;
//import com.gt.controller.member.LoginService;
//import com.gt.util.CommonUtil;
//import com.gt.util.JedisUtil;
//import com.gt.util.PropertiesUtil;
//import com.gt.util.SHA1;
//
///**
// * 用户手机端登录
// *
// * @author Administrator
// *
// */
//@Service
//public class LoginServiceImpl implements LoginService {
//
//	private static final Logger LOG = Logger.getLogger(LoginServiceImpl.class);
//
//	@Autowired
//	private MemberMapper memberMapper;
//
//	@Autowired
//	private MemberParameterMapper memberParameterMapper;
//
//	@Override
//	public Map<String, Object> findMemberByPhone(HttpServletRequest request,
//			Integer busId, String phone, String pwd) {
//		Map<String, Object> map = new HashMap<String, Object>();
//		try {
//			if (CommonUtil.isEmpty(phone)) {
//				map.put("result", false);
//				map.put("message", "手机或密码不正确");
//				return map;
//			}
//			if (CommonUtil.isEmpty(pwd)) {
//				map.put("result", false);
//				map.put("message", "手机或密码不正确");
//				return map;
//			}
//			// 密码加密
//			pwd = SHA1.encode(pwd);
//			List<MemberEntity> memberList = memberMapper.selectByPhoneAndPwd(busId,
//					phone, pwd);
//			if (CommonUtil.isEmpty(memberList) || memberList.size() != 1) {
//				map.put("result", false);
//				map.put("message", "账户不存在");
//				return map;
//			}
//
//			MemberEntity member = memberList.get(0);
//
//			if(CommonUtil.isEmpty(member.getHeadimgurl())){
//				MemberParameter mp=memberParameterMapper.findByMemberId(member.getId());
//				if(CommonUtil.isNotEmpty(mp)){
//					member.setHeadimgurl(PropertiesUtil.getWebHome()+mp.getHeadimg());
//				}
//			}
//
//			CommonUtil.setLoginMember(request, member);
//			map.put("result", true);
//			map.put("message", "登录成功");
//		} catch (Exception e) {
//			LOG.error("登录异常", e);
//			map.put("result", false);
//			map.put("message", "登录异常");
//		}
//		return map;
//	}
//
//	@Override
//	public Map<String, Object> register(HttpServletRequest request,Map<String, Object> map) {
//		Map<String, Object> returnMap = new HashMap<String, Object>();
//		try {
//			Integer busId = CommonUtil.toInteger(map.get("busId"));
//			String phone = CommonUtil.toString(map.get("phone"));
//			String onePwd = CommonUtil.toString(map.get("onePwd"));
//			String twoPwd = CommonUtil.toString(map.get("twoPwd"));
//			String vcode = CommonUtil.toString(map.get("vcode"));
//			if (CommonUtil.isEmpty(vcode)) {
//				returnMap.put("result", false);
//				returnMap.put("message", "请输入验证码");
//				return returnMap;
//			}
//			String vcode1 = JedisUtil.get(vcode);
//			if (CommonUtil.isEmpty(vcode1)) {
//				returnMap.put("result", false);
//				returnMap.put("message", "验证码超时或错误");
//				return returnMap;
//			}
//			if (CommonUtil.isEmpty(onePwd) || CommonUtil.isEmpty(twoPwd)) {
//				returnMap.put("result", false);
//				returnMap.put("message", "请填写密码");
//				return returnMap;
//			}
//			if (!onePwd.equals(twoPwd)) {
//				returnMap.put("result", false);
//				returnMap.put("message", "密码不一致请重新填写");
//				return returnMap;
//			}
//			MemberEntity member = memberMapper.findByPhone(busId, phone);
//			if (CommonUtil.isNotEmpty(member)
//					&& CommonUtil.isNotEmpty(member.getPwd())) {
//				returnMap.put("result", false);
//				returnMap.put("message", "用户已注册。");
//				return returnMap;
//			}
//
//			if (CommonUtil.isEmpty(member)) {
//				// 新增用户
//				member = new MemberEntity();
//				member.setPhone(phone);
//				member.setBusid(busId);
//				member.setPwd(SHA1.encode(onePwd));
//				member.setLoginmode((byte) 1);
//				member.setNickname("Fans_"+phone.substring(4));
//				memberMapper.insertSelective(member);
//				CommonUtil.setLoginMember(request, member);
//			} else {
//				// 修改用户
//				MemberEntity m1 = new MemberEntity();
//				m1.setId(member.getId());
//				m1.setPhone(phone);
//				m1.setPwd(SHA1.encode(onePwd));
//				memberMapper.updateByPrimaryKeySelective(m1);
//				CommonUtil.setLoginMember(request, m1);
//			}
//
//			MemberParameter memberParameter=memberParameterMapper.findByMemberId(member.getId());
//			if(CommonUtil.isEmpty(memberParameter)){
//				MemberParameter mp=new MemberParameter();
//				mp.setMemberid(member.getId());
//				memberParameterMapper.insertSelective(mp);
//			}
//
//			JedisUtil.del(vcode1);
//			returnMap.put("result", true);
//			returnMap.put("message", "注册成功");
//		} catch (Exception e) {
//			LOG.error("测试异常", e);
//			returnMap.put("result", false);
//			returnMap.put("message", "注册失败");
//		}
//		return returnMap;
//	}
//
//	@Override
//	public Map<String, Object> quickLogin(HttpServletRequest request,
//			Integer busId, String phone, String vcode) {
//		Map<String, Object> returnMap = new HashMap<String, Object>();
//		try {
//			if (CommonUtil.isEmpty(vcode)) {
//				returnMap.put("result", false);
//				returnMap.put("message", "请输入验证码");
//				return returnMap;
//			}
//			String vcode1 = JedisUtil.get(vcode);
//			if (CommonUtil.isEmpty(vcode1)) {
//				returnMap.put("result", false);
//				returnMap.put("message", "验证码超时或错误");
//				return returnMap;
//			}
//			JedisUtil.del(vcode1);
//			MemberEntity member = memberMapper.findByPhone(busId, phone);
//			if (CommonUtil.isNotEmpty(member)) {
//				MemberParameter mp=memberParameterMapper.findByMemberId(member.getId());
//				if(CommonUtil.isNotEmpty(mp)){
//					member.setHeadimgurl(mp.getHeadimg());
//					CommonUtil.setLoginMember(request, member);
//				}else{
//					CommonUtil.setLoginMember(request, member);
//				}
//			} else {
//				returnMap.put("result", -1);
//				returnMap.put("message", "用户不存在");
//				return returnMap;
//			}
//			returnMap.put("result", true);
//			returnMap.put("message", "登录成功");
//		} catch (Exception e) {
//			LOG.error("登录异常", e);
//			returnMap.put("result", false);
//			returnMap.put("message", "登录异常");
//		}
//		return returnMap;
//	}
//
//	@Override
//	public Map<String, Object> resetPwd(Map<String, Object> map) {
//		Map<String, Object> returnMap = new HashMap<String, Object>();
//		try {
//			Integer busId = CommonUtil.toInteger(map.get("busId"));
//			String phone = CommonUtil.toString(map.get("phone"));
//			String pwd = CommonUtil.toString(map.get("pwd"));
//			String vcode = CommonUtil.toString(map.get("vcode"));
//			if (CommonUtil.isEmpty(vcode)) {
//				returnMap.put("result", false);
//				returnMap.put("message", "请输入验证码");
//				return returnMap;
//			}
//			String vcode1 = JedisUtil.get(vcode);
//			if (CommonUtil.isEmpty(vcode1)) {
//				returnMap.put("result", false);
//				returnMap.put("message", "验证码超时或错误");
//				return returnMap;
//			}
//			JedisUtil.del(vcode1);
//			if (CommonUtil.isEmpty(pwd)) {
//				returnMap.put("result", false);
//				returnMap.put("message", "请填写密码");
//				return returnMap;
//			}
//			MemberEntity member = memberMapper.findByPhone(busId, phone);
//			if (CommonUtil.isEmpty(member)) {
//				returnMap.put("result", false);
//				returnMap.put("message", "用户不存在。");
//				return returnMap;
//			}
//
//			// 修改用户
//			MemberEntity m1 = new MemberEntity();
//			m1.setId(member.getId());
//			m1.setPhone(phone);
//			m1.setPwd(SHA1.encode(pwd));
//			memberMapper.updateByPrimaryKeySelective(m1);
//
//			returnMap.put("result", true);
//			returnMap.put("message", "修改密码成功");
//		} catch (Exception e) {
//			LOG.error("修改密码异常", e);
//			returnMap.put("result", false);
//			returnMap.put("message", "修改密码异常");
//		}
//		return returnMap;
//	}
//
//}
