package com.shopme.admin.setting.state;

import com.shopme.admin.repository.StateRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class StateRepositoryTest {
    @Autowired
    private StateRepository repo;

    @Test
    public void testCreateStates() {
        Country country = new Country(4);

        List<State> states = List.of(
                new State("Hà Nội", country),
                new State("Nam Định", country),
                new State("Thành phố Hồ Chí Minh", country)
        );

        repo.saveAll(states);
    }
}
