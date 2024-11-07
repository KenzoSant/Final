package com.fatec.smart_parking.client;


import com.fatec.smart_parking.core.Role;
import com.fatec.smart_parking.user.User;
import com.fatec.smart_parking.user.UserRepository;

import java.util.List;


public interface ClientRepository extends UserRepository {
    List<User> findByRole(Role role);

}
