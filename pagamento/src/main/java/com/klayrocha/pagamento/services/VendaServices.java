package com.klayrocha.pagamento.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.klayrocha.pagamento.data.entity.ProdutoVenda;
import com.klayrocha.pagamento.data.entity.Venda;
import com.klayrocha.pagamento.data.vo.VendaVO;
import com.klayrocha.pagamento.exception.ResourceNotFoundException;
import com.klayrocha.pagamento.repository.ProdutoVendaRepository;
import com.klayrocha.pagamento.repository.VendaRepository;

@Service
public class VendaServices {

	private final VendaRepository vendaRepository;
	private final ProdutoVendaRepository produtoVendaRepository;

	@Autowired
	public VendaServices(VendaRepository vendaRepository, ProdutoVendaRepository produtoVendaRepository) {
		this.vendaRepository = vendaRepository;
		this.produtoVendaRepository = produtoVendaRepository;
	}

	public VendaVO create(VendaVO vendaVO) {

		Venda venda = vendaRepository.save(Venda.create(vendaVO));
		List<ProdutoVenda> produtosSalvos = new ArrayList<ProdutoVenda>();
		vendaVO.getProdutos().forEach(p -> {
			ProdutoVenda pv = ProdutoVenda.create(p);
			pv.setVenda(venda);
			produtosSalvos.add(produtoVendaRepository.save(pv));
		});
		venda.setProdutos(produtosSalvos);
		return VendaVO.create(venda);
	}

	public Page<VendaVO> findAll(Pageable pageable) {
		var page = vendaRepository.findAll(pageable);
		return page.map(this::convertToVendaVO);
	}

	private VendaVO convertToVendaVO(Venda venda) {
		return VendaVO.create(venda);
	}

	public VendaVO findById(Long id) {
		var entity = vendaRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		return VendaVO.create(entity);
	}

	public VendaVO update(Venda venda) {
		final Optional<Venda> optional = vendaRepository.findById(venda.getId());

		if (!optional.isPresent()) {
			new ResourceNotFoundException("No records found for this ID");
		}

		return VendaVO.create(vendaRepository.save(venda));
	}

	public void delete(Long id) {
		Venda entity = vendaRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		vendaRepository.delete(entity);
	}
}