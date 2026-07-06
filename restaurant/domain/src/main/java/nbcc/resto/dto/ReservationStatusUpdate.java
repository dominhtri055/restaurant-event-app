package nbcc.resto.dto;

public class ReservationStatusUpdate {

    private String status;

    private Long tableId;

    public  ReservationStatusUpdate() {
    }

    public String getStatus() {
        return status;
    }

    public ReservationStatusUpdate setStatus(String status) {
        this.status = status;
        return this;
    }

    public Long getTableId() {
        return tableId;
    }

    public ReservationStatusUpdate setTableId(Long tableId) {
        this.tableId = tableId;
        return this;
    }
}
