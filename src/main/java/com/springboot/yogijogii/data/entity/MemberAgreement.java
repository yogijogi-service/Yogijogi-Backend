package com.springboot.yogijogii.data.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MemberAgreement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean consentServiceUser=false;

    private boolean consentPersonalInfo=false;

    private boolean consentToThirdPartyOffers=false;

    private boolean consentToReceivingMail=false;

    @OneToOne(mappedBy = "memberAgreement")
    private Member member;

}
