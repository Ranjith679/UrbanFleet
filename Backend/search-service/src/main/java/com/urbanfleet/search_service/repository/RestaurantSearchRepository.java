package com.urbanfleet.search_service.repository;

import com.urbanfleet.search_service.model.RestaurantDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface RestaurantSearchRepository
        extends ElasticsearchRepository<RestaurantDocument, String> {
}
