package com.app.traphoria.model;

import java.io.Serializable;
import java.util.List;

public class PassportVisaDetailsDTO implements Serializable {

    public List<PassportDTO> Passport;
    public List<VisaDTO> Visa;

    public List<PassportDTO> getPassport() {
        return Passport;
    }

    public void setPassport(List<PassportDTO> passport) {
        Passport = passport;
    }

    public List<VisaDTO> getVisa() {
        return Visa;
    }

    public void setVisa(List<VisaDTO> visa) {
        Visa = visa;
    }
}
