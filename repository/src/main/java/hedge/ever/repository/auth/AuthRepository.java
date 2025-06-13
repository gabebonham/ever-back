package hedge.ever.repository.auth;

import hedge.ever.common.models.user.UserModel;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AuthRepository {
    @Insert("INSERT INTO users (username, email, password, created_at, role,id) " +
            "VALUES (#{username}, #{email}, #{password}, NOW(), #{role}, gen_random_uuid())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void create(UserModel userAuthEntity);

    @Select("SELECT * FROM users WHERE username = #{username}")
    @Results({@Result(property = "id", column = "id"),
            @Result(property = "username", column = "username"),
            @Result(property = "email", column = "email"),
            @Result(property = "password", column = "password"),
            @Result(property = "role", column = "role"),
            @Result(property = "createdAt", column = "created_at")})
    UserModel getByUsername(@Param("username") String username);

    @Select("SELECT * FROM users WHERE id = #{id}")
    @Results({@Result(property = "id", column = "id"),
            @Result(property = "username", column = "username"),
            @Result(property = "email", column = "email"),
            @Result(property = "password", column = "password"),
            @Result(property = "role", column = "role"),
            @Result(property = "createdAt", column = "created_at")})
    UserModel getById(@Param("id") String id);

    @Update("<script>" + "UPDATE users " + "<set>" + "  <if test='password != null'> password = #{password},</if>" + "</set>" + "WHERE email = #{email}" + "</script>")
    void resetPassword(@Param("password") String password, @Param("email") String email);

    @Update("<script>"
            + "UPDATE users "
            + "<set>"
            + "  <if test='username != null'>username = #{username},</if>"
            + "  <if test='email != null'>email = #{email},</if>"
            + "  <if test='role != null'>role = #{role}</if>"
            + "</set>"
            + "<if test='username != null or email != null or role != null or cnpj != null or cpf != null'>WHERE id = #{id}</if>"
            + "</script>")
    void update(UserModel updatedUser);

    @Delete("DELETE FROM users WHERE id = #{id}")
    void delete(@Param("id") String id);
}
