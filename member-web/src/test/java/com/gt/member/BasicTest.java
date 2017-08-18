package com.gt.member;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 单元测试基类
 *
 * @author zhangmz
 * @create 2017/6/16
 */
@RunWith( SpringRunner.class )
@SpringBootTest
public class BasicTest {

    protected Logger logger = LoggerFactory.getLogger( BasicTest.class );
    // 开始时间
    private Long start_time;

    @Before
    public void start() {
	start_time = System.currentTimeMillis();
	this.logger.info( "=======================================  单元测试Start =======================================" );
    }



    @After
    public void end() {
	this.logger.info( "执行结束，方法执行 {} 毫秒", ( System.currentTimeMillis() - start_time ) );
	this.logger.info( "=======================================  单元测试End =======================================" );
    }

}
