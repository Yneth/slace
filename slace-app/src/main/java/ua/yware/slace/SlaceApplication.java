package ua.yware.slace;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.RequiredArgsConstructor;
import ua.yware.slace.dao.PremiseRepository;
import ua.yware.slace.model.Premise;
import ua.yware.slace.model.enums.PremiseCategory;
import ua.yware.slace.service.storage.StorageProperties;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class SlaceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SlaceApplication.class, args);
    }

}

@Component
@RequiredArgsConstructor
class PremisePopulator implements CommandLineRunner {

    private final PremiseRepository premiseRepository;

    @Override
    public void run(String... args) throws Exception {
        if (premiseRepository.findById(new BigInteger("1")) == null) {
            premiseRepository.save(createPremise("1", "1", "Red Hook Warehouse", 200, new BigDecimal(10),
                    "Саксаганского, 24", 190, PremiseCategory.CONCERT));
        }
        if (premiseRepository.findById(new BigInteger("2")) == null) {
            premiseRepository.save(createPremise("2", "2", "Green Hook Warehouse", 200, new BigDecimal(10),
                    "Саксаганского, 24", 320, PremiseCategory.FESTIVITY));
        }
        if (premiseRepository.findById(new BigInteger("3")) == null) {
            premiseRepository.save(createPremise("3", "3", "Blue Hook Warehouse", 200, new BigDecimal(10),
                    "Саксаганского, 24", 500, PremiseCategory.PHOTO_SESSION));
        }
    }

    private Premise createPremise(String id, String imageId, String name, Integer space,
                                  BigDecimal priceRate, String address, Integer area, PremiseCategory category) {
        Premise premise = new Premise();
        premise.setImageUri("/" + imageId);
        premise.setName(name);
        premise.setAddress(address);
        premise.setSpace(space);
        premise.setArea(area);
        premise.setPriceRate(priceRate);
        premise.setId(new BigInteger(id));
        premise.setCategory(category);
        return premise;
    }

}