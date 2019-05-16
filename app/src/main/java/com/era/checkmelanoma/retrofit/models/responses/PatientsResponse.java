package com.era.checkmelanoma.retrofit.models.responses;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PatientsResponse {

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
        @SerializedName("family")
        @Expose
        private String family;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("patronymic")
        @Expose
        private String patronymic;
        @SerializedName("sex")
        @Expose
        private String sex;
        @SerializedName("dateBirth")
        @Expose
        private String dateBirth;

        public Object(Integer id, String family, String name, String patronymic, String sex, String dateBirth) {
            this.id = id;
            this.family = family;
            this.name = name;
            this.patronymic = patronymic;
            this.sex = sex;
            this.dateBirth = dateBirth;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getFamily() {
            return family;
        }

        public void setFamily(String family) {
            this.family = family;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPatronymic() {
            return patronymic;
        }

        public void setPatronymic(String patronymic) {
            this.patronymic = patronymic;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getDateBirth() {
            return dateBirth;
        }

        public void setDateBirth(String dateBirth) {
            this.dateBirth = dateBirth;
        }

    }

}
