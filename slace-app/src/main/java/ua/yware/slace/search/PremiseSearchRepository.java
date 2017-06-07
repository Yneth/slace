package ua.yware.slace.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.UUID;

import ua.yware.slace.model.Premise;

public interface PremiseSearchRepository extends ElasticsearchRepository<Premise, UUID> {

}
