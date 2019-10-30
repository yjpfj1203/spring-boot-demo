package com.elasticsearch.entity.promotion;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "promotion", type = "promotion", shards = 5, replicas = 2, refreshInterval = "-1", createIndex = true)
public class Promotion {

    /**
     * 这里如果是long那么，保存后，id会为空值
     */
    @Id
    private String id;

    /**
     * 大业主id
     */
    private Long customerId;

    private String title;

    private BigDecimal price;
    /**
     * 是否包含佣金信息
     */
    private boolean commission;
    /**
     * 是否已经分享了
     */
    private boolean shared;
    /**
     * 显示房号
     */
    private boolean showResourceName;

    private ProjectSnapshot project;

    private Set<ResourceSnapshot> resources;

    private Long createdById;
    private String createdByName;

    @Field(type = FieldType.Date)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

}