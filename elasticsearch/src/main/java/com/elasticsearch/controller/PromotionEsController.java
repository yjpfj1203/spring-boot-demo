package com.elasticsearch.controller;


import com.elasticsearch.entity.OptLog;
import com.elasticsearch.entity.promotion.Promotion;
import com.elasticsearch.entity.repository.PromotionESRepository;
import com.elasticsearch.util.CollectionUtil;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.join.query.JoinQueryBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/es/promotions")
@EnableSwagger2
@AllArgsConstructor
public class PromotionEsController {
    private final PromotionESRepository promotionESRepository;

    @GetMapping
    public List<Promotion> findAll() {
        return CollectionUtil.iterableToCollectionJava8(promotionESRepository.findAll());
    }

    /**
     * 1、查  id
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Promotion findById(@PathVariable String id) {
        return promotionESRepository.findById(id).orElseThrow(() -> new RuntimeException("未找到项目信息"));
    }

    /**
     * 4、增
     * @param promotion
     * @return
     */
    @PostMapping
    public Promotion insert(@RequestBody Promotion promotion) {
        promotionESRepository.save(promotion);
        return promotion;
    }

    /**
     * 5、删 id
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Promotion delete(@PathVariable String id) {
        Promotion promotion = promotionESRepository.findById(id).orElseThrow(() -> new RuntimeException("未找到项目信息"));
        promotionESRepository.deleteById(id);
        return promotion;
    }

    /**
     * 6、改
     * @param promotion
     * @return
     */
    @PutMapping("{id}")
    public Promotion update(@RequestBody Promotion promotion) {
        promotionESRepository.save(promotion);
        return promotion;
    }

    @GetMapping("/page")
    public Page<Promotion> findList(@RequestParam(value = "title", required = false) String title,
                                    @RequestParam(value = "customerId", required = false) String customerId,
                                    @RequestParam(value = "createdDate", required = false) LocalDateTime createdDate,
                                    @RequestParam(value = "resourceName", required = false) String resourceName,
                                    @RequestParam(value = "keyword", required = false) String keyword,
                                    @RequestParam(value = "ids", required = false) String[] ids,
                                    @RequestParam(value = "minPrice", required = false) BigDecimal minPrice,
                                    @RequestParam(value = "maxPrice", required = false) BigDecimal maxPrice,
                                    @RequestParam(value = "imageUrls", required = false) String[] imageUrls) {
        //创建builder
        BoolQueryBuilder builder = QueryBuilders.boolQuery();

        /**
         * 组合查询
         * must(QueryBuilders)    : AND
         * mustNot(QueryBuilders) : NOT
         * should:                : OR
         *
         * 使用QueryBuilder
         * termQuery("key", value)             完全匹配，不分词(区分大小写)
         * termsQuery("key", value1, value2..)   一次匹配多个值
         * matchQuery("key", value)            单个匹配, field不支持通配符, 会对value进行分词（不区分大小写）
         * matchPhraseQuery                    单个匹配, 会分词（不区分大小写），all the terms must appear in the field, they must have the same order as the input value
         * queryStringQuery                    单个匹配, 会分词（不区分大小写），all the terms must appear in the field
         * multiMatchQuery("value", "field1", "field2"..);  匹配多个字段, field有通配符也行
         * matchAllQuery();                  匹配所有文件，统计类用处会多些。
         * prefixQuery                       前缀查询
         * idsQuery().addIds(String... ids)  ids查询
         * wildcardQuery                     通配符搜索查询。支持通配符*,?
         * rangeQuery()                      范围查询，示例如下
         *
         * 这两个还没搞懂用法
         * JoinQueryBuilders.hasChildQuery()
         * JoinQueryBuilders.hasParentQuery()
         */
        /**
         * 这个没太搞懂是做什么用的
         * QueryBuilders.moreLikeThisQuery("user", "tel").like("kimchy").
         */
//        QueryBuilders.rangeQuery("name")
//                .from("葫芦1000娃")
//                .to("葫芦3000娃")
//                .includeLower(true)     //包括下界
//                .includeUpper(true);   //包括上界
//        QueryBuilders.spanNearQuery()
//                .addClause(QueryBuilders.spanTermQuery("field", "value1"))
//                .addClause(QueryBuilders.spanTermQuery("field", "value2"))
//                .addClause(QueryBuilders.spanTermQuery("field", "value3"))
//                .slop(12)                                               // Slop factor
//                .inOrder(false)
//                .collectPayloads(false);
        //设置用户名为king
        if (customerId != null) {
            builder.must(QueryBuilders.termQuery("customerId", customerId));
        }
        if (StringUtils.isNotBlank(title)) {
            builder.must(QueryBuilders.fuzzyQuery("title", title));
        }
        if (StringUtils.isNotBlank(resourceName)) {
            builder.must(QueryBuilders.matchPhraseQuery("resources.name", resourceName));
        }
        if (ids != null && ids.length != 0){
            builder.must(QueryBuilders.idsQuery(ids));
        }
        if (StringUtils.isNotBlank(keyword)){
            builder.must(QueryBuilders.multiMatchQuery(keyword, "title", "*Name", "project.location.province", "project.location.city", "project.location.area"));
        }
        if (minPrice != null && maxPrice != null){
            builder.must(
                    QueryBuilders.rangeQuery("price")
                            .gte(minPrice)
                            .lte(maxPrice)
                            .includeLower(true)     //包括下界
                            .includeUpper(true));    //包括上界
        }
        if (imageUrls != null && imageUrls.length != 0){
            builder.must(QueryBuilders.termsQuery("project.imageUrls.imageUrl", imageUrls));
        }

        //排序
        FieldSortBuilder sort = SortBuilders.fieldSort("price").order(SortOrder.DESC);

        //设置分页
        //====注意!es的分页和Hibernate一样api是从第0页开始的=========
        PageRequest page = new PageRequest(0, 100);

        NativeSearchQuery query = new NativeSearchQueryBuilder().withQuery(builder)
                .withPageable(page)
                .withSort(sort)
                .build();

        //执行,返回包装结果的分页
        Page<Promotion> resutlList = promotionESRepository.search(query);

        return resutlList;
    }
}
