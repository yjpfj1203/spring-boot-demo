package com.elasticsearch.controller;


import com.elasticsearch.entity.OptLog;
import com.elasticsearch.entity.repository.OptLogESRepository;
import com.elasticsearch.util.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/es/logs")
@EnableSwagger2
public class OptLogEsController {
    @Autowired
    private OptLogESRepository optLogRepository;

    @GetMapping
    public List<OptLog> findAll() {
        return CollectionUtil.iterableToCollectionJava8(optLogRepository.findAll());
    }

    /**
     * 1、查  id
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public OptLog findById(@PathVariable String id) {
        return optLogRepository.findById(id).orElse(null);
    }

    /**
     * 2、查  ++:全文检索（根据整个实体的所有属性，可能结果为0个）
     * @param q
     * @return
     */
    @GetMapping("/select/{q}")
    public List<OptLog> testSearch(@PathVariable String q) {
        QueryStringQueryBuilder builder = new QueryStringQueryBuilder(q);
        Iterable<OptLog> searchResult = optLogRepository.search(builder);
        Iterator<OptLog> iterator = searchResult.iterator();
        List<OptLog> list = new ArrayList<OptLog>();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }

    /**
     * 3、查   +++：分页、分数、分域（结果一个也不少）
     * @return
     * @return
     */
    @GetMapping("/page")
    public Page<OptLog> findList(@RequestParam(value = "userid", required = false) Long userid,
                                 @RequestParam(value = "operatetype", required = false) String operatetype,
                                 @RequestParam(value = "useruniquecode", required = false) String useruniquecode,
                                 @RequestParam(value = "username", required = false) String username) {
        //创建builder
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        //builder下有must、should以及mustNot 相当于sql中的and、or以及not
        //设置模糊搜索,真实姓名中包含金的用户
//        builder.must(QueryBuilders.fuzzyQuery("operatetype", "日志"));
        //设置用户名为king
        if (StringUtils.isNotBlank(username)) {
            builder.must(QueryBuilders.matchQuery("username", username));
        }
        if (StringUtils.isNotBlank(useruniquecode)) {
            builder.must(QueryBuilders.matchPhraseQuery("useruniquecode", useruniquecode));
        }
        if (StringUtils.isNotBlank(operatetype)) {
            builder.must(QueryBuilders.matchPhraseQuery("operatetype", operatetype));
        }
        if (userid != null) {
            builder.must(QueryBuilders.matchQuery("userid", userid));
        }

        //排序
//        FieldSortBuilder sort = SortBuilders.fieldSort("operatetime").order(SortOrder.DESC);

        //设置分页
        //====注意!es的分页和Hibernate一样api是从第0页开始的=========
        PageRequest page = new PageRequest(0, 4);

        //构建查询
        //将搜索条件设置到构建中
        //生产NativeSearchQuery
        NativeSearchQuery query = new NativeSearchQueryBuilder().withQuery(builder)
                .withPageable(page)
//                .withSort(sort)
                .build();

        //执行,返回包装结果的分页
        Page<OptLog> resutlList = optLogRepository.search(query);

        return resutlList;
    }

    /**
     * 4、增
     * @param optLog
     * @return
     */
    @PostMapping
    public OptLog insert(@RequestBody OptLog optLog) {
        optLogRepository.save(optLog);
        return optLog;
    }

    /**
     * 5、删 id
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public OptLog delete(@PathVariable String id) {
        OptLog optLog = optLogRepository.findById(id).orElse(null);
        optLogRepository.deleteById(id);
        return optLog;
    }

    /**
     * 6、改
     * @param optLog
     * @return
     */
    @PutMapping("{id}")
    public OptLog update(OptLog optLog) {
        optLogRepository.save(optLog);
        return optLog;
    }
}
