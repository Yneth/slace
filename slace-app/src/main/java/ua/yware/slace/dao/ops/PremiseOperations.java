package ua.yware.slace.dao.ops;

import java.util.List;

import ua.yware.slace.model.Premise;
import ua.yware.slace.model.User;

public interface PremiseOperations {

    List<Premise> findPremisesReservedBy(User user);

}
