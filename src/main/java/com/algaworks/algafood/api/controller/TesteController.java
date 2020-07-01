package com.algaworks.algafood.api.controller;

import static com.algaworks.algafood.infrastructure.repository.specification.RestauranteSpecification.freteGratis;
import static com.algaworks.algafood.infrastructure.repository.specification.RestauranteSpecification.nomeSemelhante;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;

@RestController
@RequestMapping("/testes")
public class TesteController {
	
	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@GetMapping("/restaurantes/por-nome")
	public List<Restaurante> restaurantePorNome(String nome, Long cozinhaId){
		return restauranteRepository.consultarPorNome(nome, cozinhaId);
	}
	
	@GetMapping("/restaurantes/por-nome-frete-jpql")
	public List<Restaurante> restaurantePorNomeFreteJPQL(String nome,
			BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal){
		return restauranteRepository.findJPQL(nome, taxaFreteInicial, taxaFreteFinal);
	}
	
	@GetMapping("/restaurantes/por-nome-frete-criteria")
	public List<Restaurante> restaurantePorNomeFreteCriteria(String nome,
			BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal){
		return restauranteRepository.findCriteria(nome, taxaFreteInicial, taxaFreteFinal);
	}
	
	@GetMapping("/restaurantes/frete-gratis-specification")
	public List<Restaurante> restaurantePorNomeFreteCriteria(String nome){
		return restauranteRepository.findAll(freteGratis()
				.and(nomeSemelhante(nome)));
	}

}
