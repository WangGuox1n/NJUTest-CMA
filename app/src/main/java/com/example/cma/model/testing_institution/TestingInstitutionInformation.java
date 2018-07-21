package com.example.cma.model.testing_institution;

import java.io.Serializable;

public class TestingInstitutionInformation implements Serializable {
    private String testingInstitutionName;     //检测检验机构名称
    private String testingInstitutionAddress;       //地址
    private String postcode;       //邮编
    private String fax;        //传真
    private String email;        //Email地址
    private String tiPeopelInCharge;         //负责人
    private String tiPicPosition;        //负责人职务
    private String tiPicTelephone;         //负责人固定电话
    private String tiPicMobilephone;         //负责人手机
    private String liaison;         //联络人
    private String liaisonPosition;        //联络人职务
    private String liaisonTelephone;         //联络人固定电话
    private String liaisonMobilephone;         //联络人手机
    private String socialCreditCode;       //社会信用代码

    private String legalEntityBelongedName;     //所属法人单位名称
    private String legalEntityBelongedAddress;       //地址
    private String lebPeopelInCharge;         //负责人
    private String lebPicPosition;        //负责人职务
    private String lebPicTelephone;         //负责人电话
    private String lebSocialCreditCode;       //社会信用代码

    private String competentDepartmentName;     //主管部门名称
    private String competentDepartmentAddress;       //地址
    private String cdPeopelInCharge;         //负责人
    private String cdPicPosition;        //负责人职务
    private String cdPicTelephone;         //负责人电话

    private Byte characteristic;        //检测机构设施特点(0固定，1临时，2可移动，3多场所）
    private Byte legalEntity;//法人类别，独立法人检验检测机构（0社团法人，1事业法人，2企业法人，3机关法人，4其他）检验检测机构所属法人（非独立法人检验检测机构填写此项）（5社团法人，6事业法人，7企业法人，8机关法人，9其他）

    public String getTestingInstitutionName() {
        return testingInstitutionName;
    }

    public void setTestingInstitutionName(String testingInstitutionName) {
        this.testingInstitutionName = testingInstitutionName;
    }

    public String getTestingInstitutionAddress() {
        return testingInstitutionAddress;
    }

    public void setTestingInstitutionAddress(String testingInstitutionAddress) {
        this.testingInstitutionAddress = testingInstitutionAddress;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLiaison() {
        return liaison;
    }

    public void setLiaison(String liaison) {
        this.liaison = liaison;
    }

    public String getLiaisonPosition() {
        return liaisonPosition;
    }

    public void setLiaisonPosition(String liaisonPosition) {
        this.liaisonPosition = liaisonPosition;
    }

    public String getLiaisonTelephone() {
        return liaisonTelephone;
    }

    public void setLiaisonTelephone(String liaisonTelephone) {
        this.liaisonTelephone = liaisonTelephone;
    }

    public String getLiaisonMobilephone() {
        return liaisonMobilephone;
    }

    public void setLiaisonMobilephone(String liaisonMobilephone) {
        this.liaisonMobilephone = liaisonMobilephone;
    }

    public String getSocialCreditCode() {
        return socialCreditCode;
    }

    public void setSocialCreditCode(String socialCreditCode) {
        this.socialCreditCode = socialCreditCode;
    }

    public String getLegalEntityBelongedName() {
        return legalEntityBelongedName;
    }

    public void setLegalEntityBelongedName(String legalEntityBelongedName) {
        this.legalEntityBelongedName = legalEntityBelongedName;
    }

    public String getLegalEntityBelongedAddress() {
        return legalEntityBelongedAddress;
    }

    public void setLegalEntityBelongedAddress(String legalEntityBelongedAddress) {
        this.legalEntityBelongedAddress = legalEntityBelongedAddress;
    }

    public String getCompetentDepartmentName() {
        return competentDepartmentName;
    }

    public void setCompetentDepartmentName(String competentDepartmentName) {
        this.competentDepartmentName = competentDepartmentName;
    }

    public String getCompetentDepartmentAddress() {
        return competentDepartmentAddress;
    }

    public void setCompetentDepartmentAddress(String competentDepartmentAddress) {
        this.competentDepartmentAddress = competentDepartmentAddress;
    }

    public Byte getCharacteristic() {
        return characteristic;
    }

    public void setCharacteristic(Byte characteristic) {
        this.characteristic = characteristic;
    }

    public Byte getLegalEntity() {
        return legalEntity;
    }

    public void setLegalEntity(Byte legalEntity) {
        this.legalEntity = legalEntity;
    }

    public String getTiPeopelInCharge() {
        return tiPeopelInCharge;
    }

    public void setTiPeopelInCharge(String tiPeopelInCharge) {
        this.tiPeopelInCharge = tiPeopelInCharge;
    }

    public String getTiPicPosition() {
        return tiPicPosition;
    }

    public void setTiPicPosition(String tiPicPosition) {
        this.tiPicPosition = tiPicPosition;
    }

    public String getTiPicTelephone() {
        return tiPicTelephone;
    }

    public void setTiPicTelephone(String tiPicTelephone) {
        this.tiPicTelephone = tiPicTelephone;
    }

    public String getTiPicMobilephone() {
        return tiPicMobilephone;
    }

    public void setTiPicMobilephone(String tiPicMobilephone) {
        this.tiPicMobilephone = tiPicMobilephone;
    }

    public String getLebPeopelInCharge() {
        return lebPeopelInCharge;
    }

    public void setLebPeopelInCharge(String lebPeopelInCharge) {
        this.lebPeopelInCharge = lebPeopelInCharge;
    }

    public String getLebPicPosition() {
        return lebPicPosition;
    }

    public void setLebPicPosition(String lebPicPosition) {
        this.lebPicPosition = lebPicPosition;
    }

    public String getLebPicTelephone() {
        return lebPicTelephone;
    }

    public void setLebPicTelephone(String lebPicTelephone) {
        this.lebPicTelephone = lebPicTelephone;
    }

    public String getLebSocialCreditCode() {
        return lebSocialCreditCode;
    }

    public void setLebSocialCreditCode(String lebSocialCreditCode) {
        this.lebSocialCreditCode = lebSocialCreditCode;
    }

    public String getCdPeopelInCharge() {
        return cdPeopelInCharge;
    }

    public void setCdPeopelInCharge(String cdPeopelInCharge) {
        this.cdPeopelInCharge = cdPeopelInCharge;
    }

    public String getCdPicPosition() {
        return cdPicPosition;
    }

    public void setCdPicPosition(String cdPicPosition) {
        this.cdPicPosition = cdPicPosition;
    }

    public String getCdPicTelephone() {
        return cdPicTelephone;
    }

    public void setCdPicTelephone(String cdPicTelephone) {
        this.cdPicTelephone = cdPicTelephone;
    }
}
