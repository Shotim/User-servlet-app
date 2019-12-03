package com.leverx.cat.entity;

import com.leverx.user.entity.User;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Date;

@StaticMetamodel(Cat.class)
public class Cat_ {
    public static volatile SingularAttribute<Cat, Integer> id;
    public static volatile SingularAttribute<Cat, String> name;
    public static volatile SingularAttribute<Cat, Date> dateOfBirth;
    public static volatile SingularAttribute<Cat, User> owner;
}
