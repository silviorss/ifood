package com.github.siviorss.ifood.cadastro.resource;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import com.github.siviorss.ifood.cadastro.entity.Prato;
import com.github.siviorss.ifood.cadastro.entity.Restaurante;


@Path("/restaurantes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "restaurante")
public class RestauranteResource {

	@GET
	public List<Restaurante> getAll() {
		return Restaurante.listAll();
	}
	
	@POST
	@Transactional
	public Response postRestaurante(Restaurante dto) {
		dto.persist();
		return Response.status(Status.CREATED).build();
	}
	
	@PUT
	@Path("{id}")
	@Transactional
	public void putRestaurante(@PathParam("id") Long id, Restaurante dto) {
		Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(id);
		if(restauranteOp.isEmpty()) {
			throw new NotFoundException();
		}
		
		Restaurante restaurante = restauranteOp.get();
		restaurante.nome = dto.nome;
		restaurante.persist();
	}
	
	@DELETE
	@Path("{id}")
	@Transactional
	public void deleteRestaurante(@PathParam("id") Long id) {
		Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(id);
		restauranteOp.ifPresentOrElse(Restaurante::delete, () -> {
			throw new NotFoundException();
		});
	}
	
	/* PRATOS */
	
	@GET
	@Path("{idRestaurante}/pratos")
	@Tag(name = "prato")
	public List<Restaurante> getPratos(@PathParam("idRestaurante") Long idRestaurante) {
		Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(idRestaurante);
		if(restauranteOp.isEmpty()) {
			throw new NotFoundException("Restaurante n??o existe");
		}
		return Prato.list("restaurante", restauranteOp.get());
	}
	
	@POST
	@Path("{idRestaurante}/pratos")
	@Transactional
	@Tag(name = "prato")
	public Response postPrato(@PathParam("idRestaurante") Long idRestaurante, Prato dto) {
		Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(idRestaurante);
		if(restauranteOp.isEmpty()) {
			throw new NotFoundException("Restaurante n??o existe");
		}
		
		Prato prato = new Prato();
		prato.nome = dto.nome;
		prato.descricao = dto.descricao;
		
		prato.preco = dto.preco;
		prato.restaurante = restauranteOp.get();
		prato.persist();
		
		return Response.status(Status.CREATED).build();
	}
	
	@PUT
	@Path("{idRestaurante}/pratos/{id}")
	@Transactional
	@Tag(name = "prato")
	public void putPrato(@PathParam("idRestaurante") Long idRestaurante, @PathParam("id") Long id, Prato dto) {
		Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(idRestaurante);
		if(restauranteOp.isEmpty()) {
			throw new NotFoundException("Restaurante n??o existe");
		}
		
		Optional<Prato> pratoOp = Prato.findByIdOptional(id);
		if(pratoOp.isEmpty()) {
			throw new NotFoundException("Prato n??o existe");
		}
		
		Prato prato = pratoOp.get();
		prato.preco = dto.preco;
		prato.persist();
	}
	
	@DELETE
	@Path("{idRestaurante}/pratos/{id}")
	@Transactional
	@Tag(name = "prato")
	public void deletePrato(@PathParam("idRestaurante") Long idRestaurante, @PathParam("id") Long id) {
		Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(idRestaurante);
		if(restauranteOp.isEmpty()) {
			throw new NotFoundException("Restaurante n??o existe");
		}
		
		Optional<Prato> pratoOp = Prato.findByIdOptional(id);
		
		pratoOp.ifPresentOrElse(Prato::delete, () -> {
			throw new NotFoundException("Prato n??o existe");
		});
	}
	
}
