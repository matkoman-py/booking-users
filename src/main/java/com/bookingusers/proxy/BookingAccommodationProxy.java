package com.bookingusers.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "accommodation-service")
public interface BookingAccommodationProxy {
    @GetMapping("api/accommodation/booking/active-reservations-exist/host/{id}")
    Boolean doActiveBookingsExistForHost(@PathVariable String id);

    @GetMapping("api/accommodation/booking/active-reservations-exist/guest/{id}")
    Boolean doActiveBookingsExistForGuest(@PathVariable String id);

    @DeleteMapping("api/accomodation/host/{id}")
    String deleteAllForHost(@PathVariable String id);

}
