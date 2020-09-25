package com.klayrocha.pagamento.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
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

import com.klayrocha.pagamento.data.entity.Venda;
import com.klayrocha.pagamento.data.vo.VendaVO;
import com.klayrocha.pagamento.services.VendaServices;

@RestController
@RequestMapping("/venda")
public class VendaController {

	private final VendaServices vendaServices;
	private final PagedResourcesAssembler<VendaVO> assembler;

	@Autowired
	public VendaController(VendaServices vendaServices, PagedResourcesAssembler<VendaVO> assembler) {
		this.vendaServices = vendaServices;
		this.assembler = assembler;
	}

	@GetMapping(value = "/{id}", produces = { "application/json", "application/xml", "application/x-yaml" })
	public VendaVO findById(@PathVariable("id") Long id) {
		VendaVO vendaVO = vendaServices.findById(id);
		vendaVO.add(linkTo(methodOn(VendaController.class).findById(id)).withSelfRel());
		return vendaVO;
	}

	@GetMapping(produces = { "application/json", "application/xml", "application/x-yaml" })
	public ResponseEntity<?> findAll(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limit", defaultValue = "12") int limit,
			@RequestParam(value = "direction", defaultValue = "asc") String direction) {

		var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;

		Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "data"));

		Page<VendaVO> vendas = vendaServices.findAll(pageable);

		vendas.stream().forEach(p -> p.add(linkTo(methodOn(VendaController.class).findById(p.getId())).withSelfRel()));

		PagedModel<EntityModel<VendaVO>> pagedModel = assembler.toModel(vendas);

		return new ResponseEntity<>(pagedModel, HttpStatus.OK);
	}

	@PostMapping(produces = { "application/json", "application/xml", "application/x-yaml" }, consumes = {
			"application/json", "application/xml", "application/x-yaml" })
	public VendaVO create(@RequestBody VendaVO venda) {
		VendaVO vendaVO = vendaServices.create(venda);
		vendaVO.add(linkTo(methodOn(VendaController.class).findById(vendaVO.getId())).withSelfRel());
		return vendaVO;
	}

	@PutMapping(produces = { "application/json", "application/xml", "application/x-yaml" }, consumes = {
			"application/json", "application/xml", "application/x-yaml" })
	public VendaVO update(@RequestBody VendaVO venda) {
		VendaVO personVO = vendaServices.update(Venda.create(venda));
		personVO.add(linkTo(methodOn(VendaController.class).findById(personVO.getId())).withSelfRel());
		return personVO;
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {
		vendaServices.delete(id);
		return ResponseEntity.ok().build();
	}

}
