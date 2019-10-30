package com.elasticsearch.entity.repository;

import com.elasticsearch.entity.OptLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptLogESRepository extends ElasticsearchRepository<OptLog, String> {
}