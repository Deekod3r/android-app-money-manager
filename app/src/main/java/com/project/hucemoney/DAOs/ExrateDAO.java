package com.project.hucemoney.DAOs;

import com.project.hucemoney.models.responses.ExrateList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ExrateDAO {
    @GET("Usercontrols/TVPortal.TyGia/pXML.aspx")
    Call<ExrateList> getExrateList();
}
