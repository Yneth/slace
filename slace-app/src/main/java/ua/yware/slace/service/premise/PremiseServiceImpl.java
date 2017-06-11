package ua.yware.slace.service.premise;

import java.math.BigInteger;

import lombok.RequiredArgsConstructor;
import ua.yware.slace.dao.PremiseRepository;
import ua.yware.slace.model.Premise;
import ua.yware.slace.web.exception.ResourceNotFoundException;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PremiseServiceImpl implements PremiseService {

    private final PremiseRepository premiseRepository;

    @Override
    public Premise getById(BigInteger id) {
        return premiseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Premise not found!"));
    }

    @Override
    public void save(Premise premise) {
        premiseRepository.save(premise);
    }

}
