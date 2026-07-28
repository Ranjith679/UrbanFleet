package com.urbanfleet.delivery_service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RiderLocationRequest {

    private Double latitude;

    private Double longitude;

}