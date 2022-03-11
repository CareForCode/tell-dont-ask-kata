package it.gabrieletondi.telldontaskkata.useCase.request;

import java.util.ArrayList;
import java.util.List;

public class SellItemsRequest {
    private final List<SellItemRequest> requests;

    public SellItemsRequest(List<SellItemRequest> requests) {
        this.requests = requests;
    }

    public List<SellItemRequest> getRequests() {
        return requests;
    }

    public void addRequest(SellItemRequest saladRequest) {
        getRequests().add(saladRequest);
    }
}
