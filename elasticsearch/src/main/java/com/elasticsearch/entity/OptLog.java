package com.elasticsearch.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.util.Date;

/**
 * 加上了@Document注解之后，默认情况下这个实体中所有的属性都会被建立索引、并且分词
 * 说明：
 * indexName：可类比于mysql的数据库
 * type：类比与mysql的表
 * shards：分片
 * replicas：副本
 * refreshInterval:
 * createIndex: 是否创建索引
 * @author yjpfj1203
 */
@Getter
@Setter
@NoArgsConstructor
@Document(indexName = "log", type = "operate_log", shards = 5, replicas = 1, refreshInterval = "-1", createIndex = true)
public class OptLog {
    @Id
    private String id;
    /**
     * 用户id
     */
    private Long userid;
    /**
     * 用户唯一标识
     */
    private String useruniquecode;
    /**
     * 用户名
     */
    private String username;
    /**
     * 操作类型
     */
    private String operatetype;
    /**
     * 操作时间
     */
    private String operatetime;
    /**
     * 操作备注
     */
    private String operatenote;
    /**
     * 对象类型
     */
    private String domaintype;
    /**
     * 对象id
     */
    private String domainid;
    /**
     * 请求表单
     */
    private String requestmodel;
    @Field
    private Date createddate;
}