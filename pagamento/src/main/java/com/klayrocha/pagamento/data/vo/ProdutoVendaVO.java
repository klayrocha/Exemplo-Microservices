package com.klayrocha.pagamento.data.vo;

import java.io.Serializable;

import javax.persistence.Column;

import org.modelmapper.ModelMapper;
import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.klayrocha.pagamento.data.entity.ProdutoVenda;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@JsonPropertyOrder({ "id", "quantidade" })
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoVendaVO extends RepresentationModel<ProdutoVendaVO> implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("id")
	private Long id;

	@Column(name = "id_produto", nullable = false, length = 10)
	private Integer idProduto;

	@JsonProperty("quantidade")
	private Integer quantidade;

	public static ProdutoVendaVO create(ProdutoVenda produtoVenda) {
		return new ModelMapper().map(produtoVenda, ProdutoVendaVO.class);
	}
}
