package com.gt.common.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import com.baomidou.mybatisplus.enums.FieldFill;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author pengjiangli
 * @since 2017-10-18
 */
@Data
@Accessors(chain = true)
@TableName("t_wx_msg_template")
public class MsgTemplate extends Model<MsgTemplate> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 微信模板ID
     */
    @TableField("template_id")
	private String templateId;
    /**
     * 标题
     */
	private String title;
    /**
     * 模板所属行业的一级行业
     */
    @TableField("primary_industry")
	private String primaryIndustry;
    /**
     * 模板所属行业的二级行业
     */
    @TableField("deputy_industry")
	private String deputyIndustry;
    /**
     * 模板内容
     */
	private String content;
    /**
     * 模板示例
     */
	private String example;
    /**
     * -1:删除
     */
	private Integer status;
    /**
     * 商家ID
     */
    @TableField("bus_id")
	private Integer busId;
    /**
     * 字段
     */
	private String fileds;
    /**
     * 参数数量
     */
    @TableField("filed_size")
	private Integer filedSize;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "MsgTemplate{" +
			"id=" + id +
			", templateId=" + templateId +
			", title=" + title +
			", primaryIndustry=" + primaryIndustry +
			", deputyIndustry=" + deputyIndustry +
			", content=" + content +
			", example=" + example +
			", status=" + status +
			", busId=" + busId +
			", fileds=" + fileds +
			", filedSize=" + filedSize +
			"}";
	}
}
