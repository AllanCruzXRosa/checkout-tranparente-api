package com.pagseguro.checkout.domain.model.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProdutoDTO {

	private Long id;

	private String descricao;

	private BigDecimal preco;

	private Integer quantidade;

	private Integer peso;

}
