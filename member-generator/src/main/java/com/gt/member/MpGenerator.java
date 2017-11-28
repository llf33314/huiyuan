package com.gt.member;

import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author zhangmz`
 * @create 2017/7/9
 */
public class MpGenerator {

    // 项目的绝对路径
    public static final  String   outPutBaseDir      = "F:/work/member/member-generator/";
    // 生成*.java 的文件路径
    public static final  String   outPutJavaModel    = outPutBaseDir + "src/main/java";
    // 需要生成的表
    public static final  String[] generatorTableName = new String[] {
		    "t_member_recommend"
    };
    // 去除表前缀 例如：bus_user 填入bus 生成的 user
    public static final  String[] tablePrefix        = new String[] { "t_wx","t_" };
    // 生成的包路径
    public static final  String   packagePath        = "com.gt.member";
    public static final  String   moduleName         = "";
    // 生成mapper.xml 文件路径
    public static final  String   generatorXmlPath   = outPutBaseDir + "src/main/java/com/gt/member/generator/xml/";
    // Mapper.java Mapper 后缀修改 DAO
    public static final  String   setMapperName      = "%sDAO";
    // Mapper.xml Mapper 后缀修改 DAO
    public static final  String   setXmlName         = "%sDAO";
    // Service.java
    public static final  String   setServiceName     = "%sService";
    // ServiceImpl.java
    public static final  String   setServiceImplName = "%sServiceImpl";
    // 数据库方言
    public static final  DbType   dbType             = DbType.MYSQL;
    // 驱动
    public static final  String   driverName         = "com.mysql.jdbc.Driver";
    // url
    public static final  String   url                = "jdbc:mysql://113.106.202.51:3306/gt_aliyuan?characterEncoding=utf8";
    // 数据库用户名
    public static final  String   username           = "root";
    // 密码
    public static final  String   password           = "gt123456";
    // 作者
    private static final String   author             = "pengjiangli";
    private static       Logger   logger             = LoggerFactory.getLogger( MpGenerator.class );

    /**
     * <p>
     * MySQL 生成演示
     * </p>
     */
    public static void main( String[] args ) {
	// 自定义需要填充的字段
	List< TableFill > tableFillList = new ArrayList<>();
	tableFillList.add( new TableFill( "ASDD_SS", FieldFill.INSERT_UPDATE ) );
	// 代码生成器
	AutoGenerator mpg = new AutoGenerator().setGlobalConfig(
			// 全局配置
			new GlobalConfig().setOutputDir( outPutJavaModel )//输出目录
					.setFileOverride( true )// 是否覆盖文件
					.setActiveRecord( true )// 开启 activeRecord 模式
					.setEnableCache( false )// XML 二级缓存
					.setBaseResultMap( true )// XML ResultMap
					.setBaseColumnList( true )// XML columList
					.setAuthor( author )
					// 自定义文件命名，注意 %s 会自动填充表实体属性！
					.setMapperName( setMapperName ).setXmlName( setXmlName ).setServiceName( setServiceName ).setServiceImplName( setServiceImplName )
			// .setControllerName("%sAction")
	).setDataSource(
			// 数据源配置
			new DataSourceConfig().setDbType( dbType )// 数据库类型
					.setTypeConvert( new MySqlTypeConvert() {
					    // 自定义数据库表字段类型转换【可选】
					    @Override
					    public DbColumnType processTypeConvert( String fieldType ) {
						logger.info( "转换类型：{}", fieldType );
						// if ( fieldType.toLowerCase().contains( "tinyint" ) ) {
						//    return DbColumnType.BOOLEAN;
						// }
						return super.processTypeConvert( fieldType );
					    }
					} ).setDriverName( driverName ).setUsername( username ).setPassword( password ).setUrl( url ) ).setStrategy(
			// 策略配置
			new StrategyConfig()
					// .setCapitalMode(true)// 全局大写命名
					.setDbColumnUnderline(true)//全局下划线命名
					.setTablePrefix( tablePrefix )// 此处可以修改为您的表前缀
					.setNaming( NamingStrategy.underline_to_camel )// 表名生成策略
					.setInclude( generatorTableName ) // 需要生成的表
					// .setExclude(new String[]{"test"}) // 排除生成的表
					// 自定义实体父类
					// .setSuperEntityClass("com.baomidou.demo.TestEntity")
					// 自定义实体，公共字段
					.setSuperEntityColumns( new String[] {} ).setTableFillList( tableFillList )
					// 自定义 mapper 父类
					.setSuperMapperClass( "com.baomidou.mybatisplus.mapper.BaseMapper" )
					// 自定义 controller 父类
					.setSuperServiceClass( "com.gt.member.base.BaseService" )
					// 自定义 controller 实现类父类
					.setSuperServiceImplClass( "com.gt.member.base.BaseServiceImpl" )
					// 自定义 controller 父类
					.setSuperControllerClass( "com.gt.member.base.BaseController" )
					// 【实体】是否生成字段常量（默认 false）
					// public static final String ID = "test_id";
					// .setEntityColumnConstant(true)
					// 【实体】是否为构建者模型（默认 false）
					// public User setName(String name) {this.name = name; return this;}
					//                        .setEntityBuilderModel(true)
					// 【实体】是否为lombok模型（默认 false）<a href="https://projectlombok.org/">document</a>
					.setEntityLombokModel( true )
			// Boolean类型字段是否移除is前缀处理
			// .setEntityBooleanColumnRemoveIsPrefix(true)
			// .setRestControllerStyle(true)
			// .setControllerMappingHyphenStyle(true)
	).setPackageInfo(
			// 包配置
			new PackageConfig().setModuleName( moduleName ).setParent( packagePath )// 自定义包路径
			//                        .setController("controller")// 这里是控制器包名，默认 web
	).setCfg(
			// 注入自定义配置，可以在 VM 中使用 cfg.abc 设置的值
			new InjectionConfig() {
			    @Override
			    public void initMap() {
				Map< String,Object > map = new HashMap<>();
				map.put( "abc", this.getConfig().getGlobalConfig().getAuthor() + "-mp" );
				this.setMap( map );
			    }
			}.setFileOutConfigList( Arrays.< FileOutConfig >asList( new FileOutConfig( "/templates/mapper.xml.vm" ) {
			    // 自定义输出文件目录
			    @Override
			    public String outputFile( TableInfo tableInfo ) {
				return generatorXmlPath + tableInfo.getXmlName() + ".xml";
			    }
			} ) ) ).setTemplate(
			// 关闭默认 xml 生成，调整生成 至 根目录
			new TemplateConfig().setXml( null )
			// 自定义模板配置，模板可以参考源码 /mybatis-plus/src/main/resources/template 使用 copy
			// 至您项目 src/main/resources/template 目录下，模板名称也可自定义如下配置：
			// .setController("...");
			// .setEntity("...");
			// .setMapper("...");
			// .setXml("...");
			// .setService("...");
			// .setServiceImpl("...");
	);

	// 执行生成
	mpg.execute();

	// 打印注入设置，这里演示模板里面怎么获取注入内容【可无】
	System.err.println( mpg.getCfg().getMap().get( "abc" ) );
    }
}
