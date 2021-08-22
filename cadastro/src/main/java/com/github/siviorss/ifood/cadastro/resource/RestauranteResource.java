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

import com.github.siviorss.ifood.cadastro.entity.Restaurante;


@Path("/restaurantes")
public class RestauranteResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
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
}
