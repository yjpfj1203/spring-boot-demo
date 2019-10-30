package com.elasticsearch.entity.promotion;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Location {
    /**
     * 纬度
     */
    private Double latitude;

    /**
     * 经度
     */
    private Double longitude;

    private String nation;

    private String province;

    private String district;

    private String famousArea;

    private String address;

    private String districtAndFamousArea;
}
