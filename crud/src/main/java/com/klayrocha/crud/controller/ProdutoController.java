package com.klayrocha.crud.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.klayrocha.crud.data.entity.Produto;
import com.klayrocha.crud.data.vo.ProdutoVO;
import com.klayrocha.crud.services.ProdutoServices;

@RestController
@RequestMapping("/produto")
public class ProdutoController {

	private final ProdutoServices produtoServices;

	@Autowired
	public ProdutoController(ProdutoServices produtoServices) {
		this.produtoServices = produtoServices;
	}

	@GetMapping(value = "/{id}", produces = { "application/json", "application/xml", "application/x-yaml" })
	public ProdutoVO findById(@PathVariable("id") Long id) {
		ProdutoVO produtoVO = produtoServices.findById(id);
		produtoVO.add(linkTo(methodOn(ProdutoController.class).findById(id)).withSelfRel());
		return produtoVO;
	}

	@GetMapping(produces = { "application/json", "application/xml", "application/x-yaml" })
	public ResponseEntity<?> findAll(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limit", defaultValue = "12") int limit,
			@RequestParam(value = "direction", defaultValue = "asc") String direction) {

		var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;

		Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "nome"));

		Page<ProdutoVO> produtos = produtoServices.findAll(pageable);

		produtos.stream()
				.forEach(p -> p.add(linkTo(methodOn(ProdutoController.class).findById(p.getId())).withSelfRel()));

		return new ResponseEntity<>(produtos, HttpStatus.OK);
	}

	@PostMapping(produces = { "application/json", "application/xml", "application/x-yaml" }, consumes = {
			"application/json", "application/xml", "application/x-yaml" })
	public ProdutoVO create(@RequestBody ProdutoVO produto) {
		ProdutoVO produtoVO = produtoServices.create(produto);
		produtoVO.add(linkTo(methodOn(ProdutoController.class).findById(produtoVO.getId())).withSelfRel());
		return produtoVO;
	}

	@PutMapping(produces = { "application/json", "application/xml", "application/x-yaml" }, consumes = {
			"application/json", "application/xml", "application/x-yaml" })
	public ProdutoVO update(@RequestBody ProdutoVO produto) {
		ProdutoVO personVO = produtoServices.update(Produto.create(produto));
		personVO.add(linkTo(methodOn(ProdutoController.class).findById(personVO.getId())).withSelfRel());
		return personVO;
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {
		produtoServices.delete(id);
		return ResponseEntity.ok().build();
	}

}
