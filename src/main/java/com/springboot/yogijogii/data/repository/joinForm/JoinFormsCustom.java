package com.springboot.yogijogii.data.repository.joinForm;



import com.springboot.yogijogii.data.entity.JoinForms;

import java.util.List;

public interface JoinFormsCustom {
    List<JoinForms> findPendingJoinFormsByTeam_TeamId(Long teamId);

}
