package com.ss.order.responseDTO;

import java.util.List;



public class ReportResponseDTO {
    private Long restaurantId;
    private List<RevenueDTO> revenues;

    public ReportResponseDTO(Long restaurantId, List<RevenueDTO> revenues) {
        this.restaurantId = restaurantId;
        this.revenues = revenues;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }
    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }
    public List<RevenueDTO> getRevenues() {
        return revenues;
    }
    public void setRevenues(List<RevenueDTO> revenues) {
        this.revenues = revenues;
    }
}

