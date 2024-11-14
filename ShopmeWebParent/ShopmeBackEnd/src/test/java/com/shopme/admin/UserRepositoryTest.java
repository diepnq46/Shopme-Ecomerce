package com.shopme.admin;

import com.shopme.admin.repository.UserRepository;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTest {
    @Autowired
    private UserRepository repo;

    @Autowired
    private TestEntityManager entityManager;


    @Test
    public void testCreateNewUserWithOneRole() {
        Role roleAdmin = entityManager.find(Role.class, 1);
        User userDiepnq = new User("diepnq@gmail.com", "diep123", "Diep", "Ngo");

        userDiepnq.addRole(roleAdmin);

        User savedUser = repo.save(userDiepnq);

        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void createNewUserWithTwoRoles() {
        Role roleAssistant = new Role(5);
        Role roleEditor = new Role(3);
        User userHieupv = new User("hieupv@gmail.com", "hieu123", "Hieu", "Pham");

        userHieupv.addRole(roleAssistant);
        userHieupv.addRole(roleEditor);

        User savedUser = repo.save(userHieupv);

        assertThat(userHieupv.getId()).isGreaterThan(0);
    }

    @Test
    public void testListAllUsers() {
        Iterable<User> listUsers = repo.findAll();

        listUsers.forEach(user -> System.out.println(user));
    }

    @Test
    public void testGetUserById() {
        User userDiepnq = repo.findById(1).get();

        System.out.println(userDiepnq);
        assertThat(userDiepnq).isNotNull();
    }

    @Test
    public void testUpdateUserDetails() {
        User userDiepnq = repo.findById(1).get();

        userDiepnq.setEmail("diepjavaprogrammer@gmail.com");
        userDiepnq.setEnabled(true);

        repo.save(userDiepnq);
    }

    @Test
    public void testUpdateUserRoles() {
        User userHieupv = repo.findById(2).get();
        Role roleEditor = new Role(3);
        Role roleSaleperson = new Role(2);

        userHieupv.getRoles().remove(roleEditor);
        userHieupv.getRoles().add(roleSaleperson);

        repo.save(userHieupv);
    }

    @Test
    public void deleteUser() {
        Integer userId = 2;

        repo.deleteById(userId);
    }

    @Test
    public void testFindUserByEmail() {
        String email = "hangmy@gmail.com";
        User user = repo.findByEmail(email);

        assertThat(user).isNotNull();
    }

    @Test
    @Transactional
    public void testEnabled() {
        Integer id = 6;

        repo.updateEnabledStatus(id, false);
    }

    @Test
    public void testPagingUsers() {
        int pageNumber = 0;
        int pageSize = 4;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page = repo.findAll(pageable);

        List<User> userList = page.getContent();
        userList.forEach(user -> System.out.println(user));

        assertThat(userList.size()).isEqualTo(pageSize);
    }

    @Test
    public void testSearchUsers() {
        int pageNumber = 0;
        int pageSize = 4;
        String keyword = "bruce";

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<User> page = repo.findAll(keyword, pageable);
        List<User> userList = page.getContent();

        userList.forEach(user -> System.out.println(user));

    }
}
