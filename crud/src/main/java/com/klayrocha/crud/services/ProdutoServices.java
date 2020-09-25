package com.klayrocha.crud.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.klayrocha.crud.data.entity.Produto;
import com.klayrocha.crud.data.repository.ProdutoRepository;
import com.klayrocha.crud.data.vo.ProdutoVO;
import com.klayrocha.crud.exception.ResourceNotFoundException;

@Service
public class ProdutoServices {

	private final ProdutoRepository produtoRepository;

	@Autowired
	public ProdutoServices(ProdutoRepository produtoRepository) {
		this.produtoRepository = produtoRepository;
	}

	public ProdutoVO create(ProdutoVO produtoVO) {
		return ProdutoVO.create(produtoRepository.save(Produto.create(produtoVO)));
	}

	public Page<ProdutoVO> findAll(Pageable pageable) {
		var page = produtoRepository.findAll(pageable);
		return page.map(this::convertToProdutoVO);
	}

	private ProdutoVO convertToProdutoVO(Produto produto) {
		return ProdutoVO.create(produto);
	}

	public ProdutoVO findById(Long id) {
		var entity = produtoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		return ProdutoVO.create(entity);
	}

	public ProdutoVO update(Produto produto) {
		final Optional<Produto> optional = produtoRepository.findById(produto.getId());

		if (!optional.isPresent()) {
			new ResourceNotFoundException("No records found for this ID");
		}

		return ProdutoVO.create(produtoRepository.save(produto));
	}

	public void delete(Long id) {
		Produto entity = produtoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		produtoRepository.delete(entity);
	}

}