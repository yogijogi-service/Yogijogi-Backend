package com.springboot.yogijogii.data.dao;

import com.springboot.yogijogii.data.entity.MemberRole;
import com.springboot.yogijogii.data.entity.ServiceRole;

public interface MemberRoleDao {
    void saveMemberRole(MemberRole memberRole);
    void saveServiceRole(ServiceRole serviceRole);
}
