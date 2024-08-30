package com.springboot.yogijogii.data.dto.signDto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class AgreementDto {
    boolean allAgreement;

    boolean consentServiceUser;

    boolean consentPersonalInfo;

    boolean consentToThirdPartyOffers;

    boolean consentToReceivingMail;
}
