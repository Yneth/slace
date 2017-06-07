package ua.yware.slace.web.rest;

import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ua.yware.slace.model.Premise;
import ua.yware.slace.search.PremiseSearchRepository;

@RequiredArgsConstructor
@RestController
public class PremiseSearchController {

    private final PremiseSearchRepository repository;

    @GetMapping("/search")
    public Iterable<Premise> findPremises(@RequestParam("q") String keyword) {
        return repository.search(QueryBuilders.wildcardQuery("*", String.format("*%s*", keyword)));
    }

}
