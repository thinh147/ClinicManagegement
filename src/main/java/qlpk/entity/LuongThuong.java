package qlpk.entity;

import lombok.Data;
import qlpk.entity.enums.Role;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Set;
@Data
@Entity
public class LuongThuong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Float luongThang;
    private Float luongThuong;
    @Enumerated(EnumType.ORDINAL)
    private Role role;
}
