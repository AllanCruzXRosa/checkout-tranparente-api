package com.pagseguro.checkout.api.converter;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.pagseguro.checkout.api.model.input.PagamentoModel;
import com.pagseguro.checkout.domain.exception.NegocioException;
import com.pagseguro.checkout.domain.model.dto.CartaoCreditoDTO;
import com.pagseguro.checkout.domain.model.dto.DocumentoTitularDTO;
import com.pagseguro.checkout.domain.model.dto.EnderecoDTO;
import com.pagseguro.checkout.domain.model.dto.PagamentoDTO;
import com.pagseguro.checkout.domain.model.dto.PrestacaoDTO;
import com.pagseguro.checkout.domain.model.dto.ProdutoDTO;
import com.pagseguro.checkout.domain.model.dto.RemessaDTO;
import com.pagseguro.checkout.domain.model.dto.RemetenteDTO;
import com.pagseguro.checkout.domain.model.dto.TelefoneDTO;
import com.pagseguro.checkout.domain.model.dto.TitularDTO;

import br.com.uol.pagseguro.api.common.domain.ShippingType;
import br.com.uol.pagseguro.api.common.domain.enums.Currency;
import br.com.uol.pagseguro.api.common.domain.enums.DocumentType;
import br.com.uol.pagseguro.api.common.domain.enums.State;

@Component
public class DadosConverter {
	
	public PagamentoDTO converter(PagamentoModel pagamentoModel) {
			
		PagamentoDTO pagamentoDTO = GerarBuilds(pagamentoModel);
		
		return pagamentoDTO;

	}
	

	private PagamentoDTO GerarBuilds(PagamentoModel pagamentoModel) {
		
		TelefoneDTO telefoneDTO = TelefoneDTO.builder()
				.codigoArea("71")
				.numero("992587319")
				.build();
		
		DocumentoTitularDTO documentoTitularDTO = DocumentoTitularDTO.builder()
				.tipo(DocumentType.CPF)
				.valor(pagamentoModel.getComprador().getCpf())
				.build();
		
			List<DocumentoTitularDTO> documentos = new ArrayList<DocumentoTitularDTO>();
			documentos.add(documentoTitularDTO);
		
		TitularDTO titularDTO;
		try {
			titularDTO = TitularDTO.builder()
					.nome(pagamentoModel.getComprador().getNome())
					.telefone(telefoneDTO)
					.dataAniversario(new SimpleDateFormat("dd/MM/yyyy").parse(pagamentoModel.getComprador().getNascimento()))
					.documentos(documentos)
					.build();
		} catch (ParseException e) {
			 throw new NegocioException("Erro ao converter data de nascimento!", e);
		}
			
		ProdutoDTO produtoDTO = ProdutoDTO.builder()
				.id(1L)
				.descricao("mouse")
				.peso(1)
				.preco(new BigDecimal("100.00"))
				.quantidade(1)
				.build();
		
		List<ProdutoDTO> produtos = new ArrayList<ProdutoDTO>();
		produtos.add(produtoDTO);
		
		EnderecoDTO enderecoDTO = EnderecoDTO.builder()
				.codigoPostal(pagamentoModel.getComprador().getEndereco().getCep())
				.pais("BRA")
				.cidade(pagamentoModel.getComprador().getEndereco().getCidade())
				.estado(State.BA)
				.rua(pagamentoModel.getComprador().getEndereco().getLogradouro())
				.distrito(pagamentoModel.getComprador().getEndereco().getBairro())
				.numero(pagamentoModel.getComprador().getEndereco().getNumero())
				.build();
		
		RemessaDTO remessaDTO = RemessaDTO.builder()
				.tipo(ShippingType.Type.PAC)
				.custo(new BigDecimal("10.0"))
				.endereco(enderecoDTO)
				.build();
		
		RemetenteDTO remetenteDTO = RemetenteDTO.builder()
				.cpf(pagamentoModel.getComprador().getCpf())
				.email(pagamentoModel.getComprador().getContato().getEmail())
				.nome(pagamentoModel.getComprador().getNome())
				.hash(pagamentoModel.getComprador().getHashComprador())
				.telefone(telefoneDTO)
				.build();
		
		PrestacaoDTO prestacaoDTO = PrestacaoDTO.builder()
				.quantidade(1)
				.valor(new BigDecimal("110.00"))
				.build();
		
		CartaoCreditoDTO cartaoCreditoDTO = CartaoCreditoDTO.builder()
				.token(pagamentoModel.getCartao().getHashCard())
				.enderecoCobranca(enderecoDTO)
				.prestacao(prestacaoDTO)
				.titular(titularDTO)
				.build();
		
		
		PagamentoDTO pagamentoDTO = PagamentoDTO.builder()
				.id(pagamentoModel.getId())
				.moeda(Currency.BRL)
				.remetente(remetenteDTO)
				.cartaoCredito(cartaoCreditoDTO)
				.produtos(produtos)
				.remessa(remessaDTO)
				.build();
		return pagamentoDTO;
	}

}
