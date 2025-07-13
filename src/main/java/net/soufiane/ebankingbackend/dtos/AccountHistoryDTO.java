package net.soufiane.ebankingbackend.dtos;

import lombok.Data;

import java.util.List;
@Data
public class AccountHistoryDTO {
    private String accountId;
    private String type;
    private double balance;
    private int currentPage;
    private int pageSize;
    private int totalPages;
    private List<AccountOperationDTO> accountOperationDTOs;

}
