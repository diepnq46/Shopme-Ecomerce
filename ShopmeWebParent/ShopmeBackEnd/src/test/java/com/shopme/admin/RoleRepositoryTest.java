package com.shopme.admin;

import com.shopme.admin.repository.RoleRepository;
import com.shopme.common.entity.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class RoleRepositoryTest {
    @Autowired
    private RoleRepository repo;

    @Test
    public void testCreateFirstRole() {
        Role roleAdmin = new Role("Admin", "quản lý tất cả mọi thứ");

        Role savedRole = repo.save(roleAdmin);
        Assertions.assertThat(savedRole.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateRestRoles() {
        Role roleSaleperson = new Role("Saleperson", "quản lý giá sản phẩm,"
                + " khách hàng, vận chuyển, đơn hàng và báo cáo bán hàng");

        Role roleEditor = new Role("Editor", "quản lý danh mục, nhãn hàng," +
                " sản phẩm, bài viết và menu");

        Role roleShiper = new Role("Shipper", "xem sản phẩm, xem đơn hàng" +
                " và cập nhật trạng thái đơn hàng");

        Role roleAssistant = new Role("Assitant", "quản lý câu hỏi và đánh giá");

        repo.saveAll(List.of(roleSaleperson, roleEditor, roleShiper, roleSaleperson, roleAssistant));
    }
}
