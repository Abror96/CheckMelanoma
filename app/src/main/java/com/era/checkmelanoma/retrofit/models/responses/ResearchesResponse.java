package com.era.checkmelanoma.retrofit.models.responses;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResearchesResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("object")
    @Expose
    private List<Object> object = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<Object> getObject() {
        return object;
    }

    public void setObject(List<Object> object) {
        this.object = object;
    }

    public class Object {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("subjectStudy")
        @Expose
        private String subjectStudy;
        @SerializedName("dateAnalisys")
        @Expose
        private String dateAnalisys;
        @SerializedName("diagnosis")
        @Expose
        private String diagnosis;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("benign")
        @Expose
        private Double benign;
        @SerializedName("malignant")
        @Expose
        private Double malignant;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getSubjectStudy() {
            return subjectStudy;
        }

        public void setSubjectStudy(String subjectStudy) {
            this.subjectStudy = subjectStudy;
        }

        public String getDateAnalisys() {
            return dateAnalisys;
        }

        public void setDateAnalisys(String dateAnalisys) {
            this.dateAnalisys = dateAnalisys;
        }

        public String getDiagnosis() {
            return diagnosis;
        }

        public void setDiagnosis(String diagnosis) {
            this.diagnosis = diagnosis;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Double getBenign() {
            return benign;
        }

        public void setBenign(Double benign) {
            this.benign = benign;
        }

        public Double getMalignant() {
            return malignant;
        }

        public void setMalignant(Double malignant) {
            this.malignant = malignant;
        }

    }

}