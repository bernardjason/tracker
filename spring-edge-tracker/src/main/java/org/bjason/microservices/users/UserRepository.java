package org.bjason.microservices.users;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepository extends CrudRepository<User, Integer> {


    public List<User> findByUserName(String userName);

    public List<User> findByUserNameAndUserPassword(String userName,String userPassword);


}
