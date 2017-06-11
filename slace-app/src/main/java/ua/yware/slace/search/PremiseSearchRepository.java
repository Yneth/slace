package ua.yware.slace.search;

import java.math.BigInteger;
import java.util.List;

import com.querydsl.core.types.Predicate;
import ua.yware.slace.model.Premise;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface PremiseSearchRepository extends MongoRepository<Premise, BigInteger>,
        QuerydslPredicateExecutor<Premise> {

    @Override
    List<Premise> findAll(Predicate predicate);

}
