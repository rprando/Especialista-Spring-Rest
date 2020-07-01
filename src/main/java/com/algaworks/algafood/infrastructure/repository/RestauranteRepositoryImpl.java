package com.algaworks.algafood.infrastructure.repository;

import static com.algaworks.algafood.infrastructure.repository.specification.RestauranteSpecification.freteGratis;
import static com.algaworks.algafood.infrastructure.repository.specification.RestauranteSpecification.nomeSemelhante;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepositoryQueries;

@Repository
public class RestauranteRepositoryImpl implements RestauranteRepositoryQueries {

	@PersistenceContext
	private EntityManager manager;
	
	@Autowired @Lazy
	private RestauranteRepository restauranteRepository;
	
	@Override
	public List<Restaurante> findJPQL(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal){
		
		HashMap<String, Object> queryParams = new HashMap<>();
		StringBuilder query = new StringBuilder();
		
		query.append("from Restaurante where 0=0 ");
		
		if(StringUtils.hasLength(nome)) {
			query.append("and nome like :nome ");
			queryParams.put("nome", "%" + nome + "%");
		}
		
		if(taxaFreteInicial != null) {
			query.append("and taxaFrete >= :taxaInicial ");
			queryParams.put("taxaInicial", taxaFreteInicial);
		}
		
		if(taxaFreteFinal != null) {
			query.append("and taxaFrete <= :taxaFinal ");
			queryParams.put("taxaFinal", taxaFreteFinal);
		}
		
		TypedQuery<Restaurante> createQuery = manager.createQuery(query.toString(), Restaurante.class);
		
		queryParams.forEach((chave, valor) -> createQuery.setParameter(chave, valor));
		
		return createQuery.getResultList();
	}

	@Override
	public List<Restaurante> findCriteria(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal) {
		
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Restaurante> criteria = builder.createQuery(Restaurante.class);
		Root<Restaurante> root = criteria.from(Restaurante.class);
		
		List<Predicate> predicates = new ArrayList<>();
		
		if(StringUtils.hasText(nome)) {
			predicates.add(builder.like(root.get("nome"), "%" + nome + "%"));
		}
		
		if(taxaFreteInicial != null) {
			predicates.add(builder.greaterThanOrEqualTo(root.get("taxaFrete"), taxaFreteInicial));
		}
		
		if(taxaFreteFinal != null) {
			predicates.add(builder.lessThanOrEqualTo(root.get("taxaFrete"), taxaFreteFinal));
		}
		
		criteria.where(predicates.toArray(new Predicate[0]));
		
		return manager.createQuery(criteria)
				.getResultList();
	}

	@Override
	public List<Restaurante> findComFreteGratis(String nome) {
		return restauranteRepository.findAll(freteGratis()
				.and(nomeSemelhante(nome)));
	}
}
