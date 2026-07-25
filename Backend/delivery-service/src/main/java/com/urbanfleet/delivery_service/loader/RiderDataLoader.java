package com.urbanfleet.delivery_service.loader;

import com.urbanfleet.delivery_service.entity.Rider;
import com.urbanfleet.delivery_service.repository.RiderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 *  Whatever class implements CommandLinerRunner, runs first during application starts 
 */
@Component
@RequiredArgsConstructor
public class RiderDataLoader implements CommandLineRunner {

    private final RiderRepository repository;

    @Override
    public void run(String... args) {

        Rider rider1 = new Rider();
        rider1.setName("John");
        rider1.setLatitude(12.9716);
        rider1.setLongitude(80.2200);
        rider1.setAvailable(true);

        Rider rider2 = new Rider();
        rider2.setName("Alex");
        rider2.setLatitude(12.9780);
        rider2.setLongitude(80.2300);
        rider2.setAvailable(true);

        Rider rider3 = new Rider();
        rider3.setName("David");
        rider3.setLatitude(12.9900);
        rider3.setLongitude(80.2500);
        rider3.setAvailable(true);

        repository.saveAll(List.of(rider1, rider2, rider3));
    }
}
