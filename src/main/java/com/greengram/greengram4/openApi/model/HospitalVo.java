package com.greengram.greengram4.openApi.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class HospitalVo {
    private String sigunNm;
    private String sigunCd;
    private String facltNm;
    private String telNo;
    private String appontDate;
    private String dataStdDate;
    private String refineLotnoAddr;
    private String refineRoadNmAddr;
    private String refineZipCd;
    private double refineLng;//경도
    private double refineLat;//위도


    @JsonGetter("sigunNm")  //받고싶은 이름
    public String getSigunNm() {
        return sigunNm;
    }
    @JsonGetter("sigunCd")
    public String getSigunCd() {
        return sigunCd;
    }
    @JsonGetter("facltNm")
    public String getFacltNm() {
        return facltNm;
    }
    @JsonGetter("telNo")
    public String getTelNo() {
        return telNo;
    }
    @JsonGetter("appontDate")
    public String getAppontDate() {
        return appontDate;
    }
    @JsonGetter("dataStdDate")
    public String getDataStdDate() {
        return dataStdDate;
    }
    @JsonGetter("refineLotNoAddr")
    public String getRefineLotNoAddr() {
        return refineLotnoAddr;
    }
    @JsonGetter("refineRoadnmAddr")
    public String getRefineRoadnmAddr() {
        return refineRoadNmAddr;
    }
    @JsonGetter("refineZipCd")
    public String getRefineZipCd() {
        return refineZipCd;
    }
    @JsonGetter("refineWgs84Logt")
    public double getRefineWgs84Logt() {
        return refineLng;
    }
    @JsonGetter("refineWgs84Lat")
    public double getRefineWgs84Lat() {
        return refineLat;
    }

    @JsonSetter("SIGUN_NM") //데이터 상의 이름
    public void setSigunNm(String sigunNm) {
        this.sigunNm = sigunNm;
    }
    @JsonSetter("SIGUN_CD")
    public void setSigunCd(String sigunCd) {
        this.sigunCd = sigunCd;
    }
    @JsonSetter("FACLT_NM")
    public void setFacltNm(String facltNm) {
        this.facltNm = facltNm;
    }
    @JsonSetter("TELNO")
    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }
    @JsonSetter("APPONT_DE")
    public void setAppontDate(String appontDate) {
        this.appontDate = appontDate;
    }
    @JsonSetter("DATA_STD_DE")
    public void setDataStdDate(String dataStdDate) {
        this.dataStdDate = dataStdDate;
    }
    @JsonSetter("REFINE_LOTNO_ADDR")
    public void setRefineLotNoAddr(String refineLotNoAddr) {
        this.refineLotnoAddr = refineLotNoAddr;
    }
    @JsonSetter("REFINE_ROADNM_ADDR")
    public void setRefineRoadnmAddr(String refineRoadnmAddr) {
        this.refineRoadNmAddr = refineRoadnmAddr;
    }
    @JsonSetter("REFINE_ZIP_CD")
    public void setRefineZipCd(String refineZipCd) {
        this.refineZipCd = refineZipCd;
    }
    @JsonSetter("REFINE_WGS84_LOGT")
    public void setRefineWgs84Logt(double refineWgs84Logt) {
        this.refineLng = refineWgs84Logt;
    }
    @JsonSetter("REFINE_WGS84_LAT")
    public void setRefineWgs84Lat(double refineWgs84Lat) {
        this.refineLat = refineWgs84Lat;
    }








}
