package ua.yware.slace.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ua.yware.slace.dao.PremiseRepository;
import ua.yware.slace.model.Premise;

@Service
@RequiredArgsConstructor
public class PremiseServiceImpl implements PremiseService {

    private final PremiseRepository premiseRepository;

    @Override
    public void save(Premise premise) {
        premiseRepository.save(premise);
    }

}
