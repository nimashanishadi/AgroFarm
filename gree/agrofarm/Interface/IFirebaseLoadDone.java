package com.example.agrofarm.Interface;

import com.example.agrofarm.Models.Crop;

import java.util.List;

public interface IFirebaseLoadDone {

    void onFirebaseLoadSuccess(List<Crop> CropList);
    void onFirebaseLoadFailed(String message);

}
