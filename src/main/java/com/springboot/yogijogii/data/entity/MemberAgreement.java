package com.springboot.yogijogii.data.entity;

import com.springboot.yogijogii.data.dto.signDto.AgreementDto;
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

    private boolean allAgreement=false;

    private boolean consentServiceUser=false;

    private boolean consentPersonalInfo=false;

    private boolean consentToThirdPartyOffers=false;

    private boolean consentToReceivingMail=false;

    @OneToOne(mappedBy = "memberAgreement")
    private Member member;

    public static MemberAgreement saveAgreement(AgreementDto agreementDto) {
        return MemberAgreement.builder()
                .allAgreement(agreementDto.isAllAgreement())
                .consentPersonalInfo(agreementDto.isConsentPersonalInfo())
                .consentServiceUser(agreementDto.isConsentServiceUser())
                .consentToReceivingMail(agreementDto.isConsentToReceivingMail())
                .consentToThirdPartyOffers(agreementDto.isConsentToThirdPartyOffers())
                .build();

    }
}
