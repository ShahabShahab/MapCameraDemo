package com.example.user.mapapplication.Model;

/**
 * Created by User on 6/21/2017.
 */
    import java.io.Serializable;
    import java.util.List;
    import com.google.gson.annotations.Expose;
    import com.google.gson.annotations.SerializedName;

    public class ClinicResponse implements Serializable
    {

        @SerializedName("success")
        @Expose
        private Boolean success;
        @SerializedName("clinics")
        @Expose
        private List<Clinic> clinics = null;
        private final static long serialVersionUID = 9022694491651648828L;

        public Boolean getSuccess() {
            return success;
        }

        public void setSuccess(Boolean success) {
            this.success = success;
        }

        public List<Clinic> getClinics() {
            return clinics;
        }

        public void setClinics(List<Clinic> clinics) {
            this.clinics = clinics;
        }

    }

