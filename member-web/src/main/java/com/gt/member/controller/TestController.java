package com.gt.member.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.gt.member.base.BaseController;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * API 参考类
 *
 * @author zhangmz
 * @create 2017/7/8
 */
@Controller
public class TestController extends BaseController {

//    @Autowired
//    private BusUserService busUserService;

    /**
     * json
     *
     * @param name
     *
     * @return
     */
    @ResponseBody
    @GetMapping( value = { "/", "" } )
    public String hello( @RequestParam( defaultValue = " world,pengjiangli!" ) String name ) {
	    return "hello " + name;
    }

//    /**
//     * 跳转index 页面
//     *
//     * @param map
//     *
//     * @return
//     */
//    @ApiOperation( value = "首页", notes = "首页" )
//    @GetMapping( "/index" )
//    public ModelAndView index( ModelAndView map ) {
//	map.addObject( "test", "hello zhangmz!" );
//	map.setViewName( "index" );
//	map. return map;
//    }
//
//    /**
//     * 用户总数量
//     *
//     * @return String
//     */
//    @ApiOperation( value = "获取用户数量", notes = "模糊匹配手机号" )
//    @ApiImplicitParam( name = "phone", value = "手机号码", paramType = "query", dataType = "long" )
//    @ResponseBody
//    @GetMapping( value = "/user/count", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
//    public ServerResponse userCount( Long phone ) {
//	this.logger.debug( "phone is {}", phone );
//	Wrapper< BusUser > busUserWrapper = null;
//	if ( phone != null ) {
//	    busUserWrapper = new EntityWrapper<>();
//	    busUserWrapper.like( "phone", phone.toString() );
//	}
//	Integer count = this.busUserService.selectCount( busUserWrapper );
//	return ServerResponse.createBySuccess( count );
//    }
//
//    /**
//     * 分页查询
//     *
//     * @param pageSize       页数
//     * @param pageIndex      页码
//     * @param searchKeyWords 关键字搜索 匹配 name & phone
//     *
//     * @return ServerResponse
//     */
//    @ApiOperation( value = "手机号、姓名模糊查询", notes = "手机号、姓名模糊查询" )
//    @ApiImplicitParams( { @ApiImplicitParam( name = "pageSize", value = "每页显示多少条数据", paramType = "query", required = false, dataType = "int", defaultValue = "10" ),
//		    @ApiImplicitParam( name = "pageIndex", value = "当前页码", paramType = "query", required = false, dataType = "int", defaultValue = "1" ),
//		    @ApiImplicitParam( name = "searchKeyWords", value = "用户姓名或手机号", paramType = "query", required = true, dataType = "String" ) } )
//    @ResponseBody
//    @GetMapping( "/user" )
//    public ServerResponse findUsers( @RequestParam( defaultValue = "10" ) Integer pageSize, @RequestParam( defaultValue = "1" ) Integer pageIndex, String searchKeyWords ) {
//	this.logger.debug( "searchKeyWords is {}", searchKeyWords );
//	this.logger.debug( "pageIndex is {}", pageIndex );
//	this.logger.debug( "pageSize is {}", pageSize );
//	Page< BusUser > page = new Page<>( pageIndex, pageSize );
//	Wrapper< BusUser > busUserWrapper = new EntityWrapper<>();
//	busUserWrapper.like( "phone", searchKeyWords );
//	busUserWrapper.like( "name", searchKeyWords );
//	return ServerResponse.createBySuccess( this.busUserService.selectPage( page, busUserWrapper ) );
//    }
//
//    /**
//     * 查询单用户信息
//     *
//     * @param uid 用户ID
//     *
//     * @return ServerResponse
//     */
//    @ApiOperation( value = "用户ID 查询用户信息", notes = "查询用户信息" )
//    @ApiImplicitParam( name = "uid", value = "用户ID", paramType = "path", required = true, dataType = "Integer" )
//    @ResponseBody
//    @GetMapping( "/user/{uid}" )
//    public ServerResponse findUser( @PathVariable Integer uid ) {
//	return ServerResponse.createBySuccess( this.busUserService.findUser( uid ) );
//    }

}
