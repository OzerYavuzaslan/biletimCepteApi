package com.biletimcepte.service;

import com.biletimcepte.dto.request.VoyageAdminUserRequest;
import com.biletimcepte.dto.request.VoyageRequest;
import com.biletimcepte.dto.response.VoyageResponse;
import com.biletimcepte.dto.response.VoyageTotalTicketsResponse;
import com.biletimcepte.model.Voyage;

import java.util.List;

public interface IVoyageService {
    VoyageResponse createVoyage(VoyageRequest voyageRequest);
    VoyageResponse setVoyagePassive(int id, VoyageAdminUserRequest voyageAdminUserRequest);
    VoyageResponse deleteVoyage(int id, VoyageAdminUserRequest voyageAdminUserRequest);
    VoyageResponse getTotalTicketPriceFromVoyage(int id);
    VoyageResponse updateVoyage(int id, VoyageRequest voyageRequest);
    List<VoyageResponse> getVoyageList();
    VoyageTotalTicketsResponse getTotalSoldTickets(int id, VoyageAdminUserRequest voyageAdminUserRequest);
    List<VoyageResponse> findVoyageList(String startDateTime, String endDateTime, String travelType,
                                        String fromCity, String toCity);
    Voyage getVoyageById(int id);
}
