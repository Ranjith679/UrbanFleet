package com.urbanfleet.search_service.controller;

import com.urbanfleet.search_service.model.RestaurantDocument;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {

    private final ElasticsearchOperations operations;

    public SearchController(ElasticsearchOperations operations) {
        this.operations = operations;
    }

    @GetMapping("/restaurants")
    public List<RestaurantDocument> searchRestaurants(

            @RequestParam String q,

            @RequestParam(required = false) String city
    ) {

        NativeQuery query = NativeQuery.builder()

                .withQuery(qb -> qb
                        .bool(b -> b

                                .must(m -> m
                                        .multiMatch(mm -> mm
                                                .fields(
                                                        "name",
                                                        "description",
                                                        "menuItems.name"
                                                )
                                                .query(q)
                                        )
                                )

                                .filter(f -> {
                                    if(city != null) {
                                        return f.term(t -> t
                                                .field("city")
                                                .value(city)
                                        );
                                    }

                                    return f.matchAll(ma -> ma);
                                })
                        )
                )

                .build();

        SearchHits<RestaurantDocument> hits =
                operations.search(query, RestaurantDocument.class);

        return hits.getSearchHits()
                .stream()
                .map(hit -> hit.getContent())
                .toList();
    }

    // Menue Items

    @GetMapping("/menu")
    public List<RestaurantDocument> searchMenu(

            @RequestParam String q
    ) {

        NativeQuery query = NativeQuery.builder()

                .withQuery(qb -> qb
                        .nested(n -> n

                                .path("menuItems")

                                .query(nq -> nq
                                        .match(m -> m
                                                .field("menuItems.name")
                                                .query(q)
                                        )
                                )
                        )
                )

                .build();

        SearchHits<RestaurantDocument> hits =
                operations.search(query, RestaurantDocument.class);

        return hits.getSearchHits()
                .stream()
                .map(hit -> hit.getContent())
                .toList();
    }

    // auto complete
    @GetMapping("/autocomplete")
    public List<String> autocomplete(

            @RequestParam String q
    ) {

        NativeQuery query = NativeQuery.builder()

                .withQuery(qb -> qb
                        .prefix(p -> p
                                .field("name")
                                .value(q.toLowerCase())
                        )
                )

                .build();

        SearchHits<RestaurantDocument> hits =
                operations.search(query, RestaurantDocument.class);

        return hits.getSearchHits()
                .stream()
                .map(hit -> hit.getContent().getName())
                .distinct()
                .toList();
    }
}