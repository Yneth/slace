package ua.yware.slace.service.premise;

import java.util.List;

import ua.yware.slace.model.Premise;
import ua.yware.slace.web.rest.form.PremiseSearchForm;

public interface PremiseSearchService {

    List<Premise> findAllWildcard(String keyword);

    List<Premise> findAll(PremiseSearchForm searchParameters);

}
