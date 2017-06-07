package ua.yware.slace.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ua.yware.slace.dao.PremiseRepository;
import ua.yware.slace.model.Premise;
import ua.yware.slace.search.PremiseSearchRepository;

@Service
@RequiredArgsConstructor
public class PremiseServiceImpl implements PremiseService {

    private final PremiseRepository premiseRepository;
    private final PremiseSearchRepository premiseSearchRepository;

    @Override
    public void save(Premise premise) {
        premiseRepository.save(premise);
        premiseSearchRepository.index(premise);
    }

}
