package com.project.hucemoney.viewmodels;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.hucemoney.DAOs.ExrateDAO;
import com.project.hucemoney.entities.Exrate;
import com.project.hucemoney.models.responses.ExrateList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import org.simpleframework.xml.core.Persister;

public class ExrateViewModel extends ViewModel {
    private MutableLiveData<String> dateTimeLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Exrate>> exrateListLiveData = new MutableLiveData<>();

    public MutableLiveData<String> getDateTimeLiveData() {
        return dateTimeLiveData;
    }

    public MutableLiveData<List<Exrate>> getExrateListLiveData() {
        return exrateListLiveData;
    }

    public void fetchData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://portal.vietcombank.com.vn/")
                .addConverterFactory(SimpleXmlConverterFactory.createNonStrict(new Persister()))
                .build();

        ExrateDAO exrateDAO = retrofit.create(ExrateDAO.class);

        Call<ExrateList> call = exrateDAO.getExrateList();
        call.enqueue(new Callback<ExrateList>() {
            @Override
            public void onResponse(Call<ExrateList> call, Response<ExrateList> response) {
                if (response.isSuccessful()) {
                    ExrateList exrateList = response.body();
                    if (exrateList != null) {
                        dateTimeLiveData.setValue("Cập nhật gần nhất: " + exrateList.getDateTime());
                        List<Exrate> exrateItems = exrateList.getExrateList();
                        exrateListLiveData.setValue(exrateList.getExrateList());
                    }
                } else {
                    Log.e("ExrateViewModel", "onResponse: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ExrateList> call, Throwable t) {
                dateTimeLiveData.setValue("Error: " + t.getMessage());
            }
        });
    }


}
