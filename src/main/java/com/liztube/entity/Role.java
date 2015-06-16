package com.liztube.entity;

import javax.persistence.*;

/**
 * Role class
 */
@Entity
@Table(name = "ROLE")
public class Role {

    //region attributes
    private long id;
    private String name;
    //endregion

    //region getter/setter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "roleId")
    @SequenceGenerator(name = "roleId", sequenceName = "ROLEID")
    @Column(name = "ID", nullable = false, insertable = true, updatable = true, length = 5, precision = 0)
    public long getId() {
        return id;
    }

    public Role setId(long id) {
        this.id = id; return this;
    }

    @Basic
    @Column(name = "NAME", nullable = false, insertable = true, updatable = true, length = 15, precision = 0)
    public String getName() {
        return name;
    }

    public Role setName(String name) {
        this.name = name; return this;
    }
    //endregion

    //region override methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;

        Role role = (Role) o;

        if (id != role.id) return false;
        if (name != null ? !name.equals(role.name) : role.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    //endregion
}

