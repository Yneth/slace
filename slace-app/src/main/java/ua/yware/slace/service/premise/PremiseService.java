package ua.yware.slace.service.premise;

import java.math.BigInteger;

import ua.yware.slace.model.Premise;

public interface PremiseService {

    Premise getById(BigInteger id);

    void save(Premise premise);

}
