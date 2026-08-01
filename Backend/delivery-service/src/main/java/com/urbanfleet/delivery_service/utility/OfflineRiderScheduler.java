package com.urbanfleet.delivery_service.utility;

import com.urbanfleet.delivery_service.entity.Rider;
import com.urbanfleet.delivery_service.repository.RiderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OfflineRiderScheduler {

    private final RiderRepository riderRepository;

    @Scheduled(fixedRate = 30000)
    public void checkOfflineRiders() {

        List<Rider> riders = riderRepository.findByOfflineFalse();

        for (Rider rider : riders) {

            // Skip riders who never sent a location
            if (rider.getLastLocationUpdate() == null) {
                continue;
            }

            long secondsSinceLastUpdate =
                    Duration.between(
                            rider.getLastLocationUpdate(),
                            LocalDateTime.now()
                    ).getSeconds();

            if (secondsSinceLastUpdate >= 30) {

                rider.setOffline(true);

                rider.setAvailable(false);

                riderRepository.save(rider);

                log.warn(
                        "Rider {} marked OFFLINE. Last location update {} seconds ago.",
                        rider.getName(),
                        secondsSinceLastUpdate
                );
            }
        }
    }
}