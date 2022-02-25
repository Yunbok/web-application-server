package db;

import model.User;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DataBaseTest {

    @Test
    public void db_findByUserId_테스트(){
        DataBase.addUser(new User("123","123","123","1@3"));

        User user1 = DataBase.findUserById("123");

        User user2 = DataBase.findUserById("12");


        assertThat("123", is(user1.getUserId()));
        assertThat(null, is(user2));

    }
}
