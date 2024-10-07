package com.br.product_api.modules.sales.dto;

import com.br.product_api.modules.sales.enums.SalesStatus;

public record SalesConfirmationDTO(String salesId, SalesStatus status) {
}
