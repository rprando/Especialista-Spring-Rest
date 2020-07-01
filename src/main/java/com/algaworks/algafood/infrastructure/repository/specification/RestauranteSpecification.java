package com.algaworks.algafood.infrastructure.repository.specification;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import com.algaworks.algafood.domain.model.Restaurante;

public class RestauranteSpecification {
	
	public static Specification<Restaurante> freteGratis(){
		return (root, query, builder) -> 
			builder.equal(root.get("taxaFrete"), BigDecimal.ZERO);
	}

	public static Specification<Restaurante> nomeSemelhante(String nome){
		return (root, query, builder) -> 
			builder.like(root.get("nome"), "%" + nome + "%");
	}
}
