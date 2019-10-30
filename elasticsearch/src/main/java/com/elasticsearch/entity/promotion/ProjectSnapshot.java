package com.elasticsearch.entity.promotion;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectSnapshot {
    /**
     * 项目名称
     */
    private String name;
    /**
     * 主图
     */
    private String mainImageUrl;

    private Location location;
    /**
     * 图片列表(不包含主图)
     * 在elastics中这种查询，一直没找到正确的查询方式，建议换成对象结构
     * private Set<String> imageUrls;
     * 因为elasticsearch通过json结构查询的，而上面的结构 ，imageUrl没有keyw值，无法支持针对imageUrl的查询
     */
    private Set<ImageUrl> imageUrls;
    /**
     * 租金单价
     */
    private BigDecimal price;
    /**
     * 招商户型
     */
    private String layout;

    /**
     * 建筑面积
     */
    private BigDecimal grossArea;
    /**
     * 交付运营时间
     */
    private Date operationDate;
    /**
     * 物业公司
     */
    private String propertyCompany;
    /**
     * 物业费
     */
    private BigDecimal propertyFee;
    /**
     * 停车位数量
     */
    private Long parkingNum;
    /**
     * 交付标准
     */
    private String deliveryStandard;
    /**
     * 标准层面积
     */
    private BigDecimal storyArea;
    /**
     * 标准层层高
     */
    private BigDecimal storyHeight;
    /**
     * 佣金政策
     */
    private String commissionPolicy;
    /**
     * 客户确认规则
     */
    private String clientConfirmPolicy;
    /**
     * 核心卖点
     */
    private String sellingPoint;
    /**
     * 交通出行
     */
    private String traffic;
    /**
     * 费用信息
     */
    private String expenseInfo;
    /**
     * 园区配套
     */
    private String parkFacility;
    /**
     * 周边配套
     */
    private String surroundFacility;
}
