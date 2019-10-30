package com.elasticsearch.entity.repository;

import com.elasticsearch.entity.promotion.Promotion;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionESRepository extends ElasticsearchRepository<Promotion, String> {
}