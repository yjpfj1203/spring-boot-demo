package com.elasticsearch.entity.promotion;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.Set;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResourceSnapshot {
    /**
     * 楼宇id
     */
    private Long buildingId;

    private String buildingName;
    /**
     * 楼层信息
     */
    private Long floorId;
    /**
     * 资源名称
     */
    private String name;
    /**
     * 资源数量
     */
    private BigDecimal quantity;
    /**
     * 主图的url
     */
    private String mainImageUrl;
    /**
     * 楼宇图片列表
     */
    private Set<String> imageUrls;

    private String floorName;

    private BigDecimal price;

    private String priceUnit;

}
