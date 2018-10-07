package com.drkeironbrown.lifecoach.api;


import com.drkeironbrown.lifecoach.model.BaseResponse;
import com.drkeironbrown.lifecoach.model.Category;
import com.drkeironbrown.lifecoach.model.CategoryReq;
import com.drkeironbrown.lifecoach.model.LoginReq;
import com.drkeironbrown.lifecoach.model.RegisterReq;
import com.drkeironbrown.lifecoach.model.Shop;
import com.drkeironbrown.lifecoach.model.SubCategory;
import com.drkeironbrown.lifecoach.model.SubCategoryReq;
import com.drkeironbrown.lifecoach.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AppApi {

    @POST("UserLogin.php")
    Call<BaseResponse<User>> getLogin(@Body LoginReq loginReq);

    @POST("UserRegister.php")
    Call<BaseResponse<User>> getRegister(@Body RegisterReq registerReq);

    @GET("GetCategories.php")
    Call<BaseResponse<List<Category>>> getCategories();


    @POST("GetCategories.php")
    Call<BaseResponse<List<Category>>> getCategoriesByName(@Body CategoryReq categoryReq);


    @POST("GetCategories.php")
    Call<BaseResponse<List<SubCategory>>> getSubCategories(@Body SubCategoryReq subCategoryReq);

    @GET("GetShop.php")
    Call<BaseResponse<List<Shop>>> getShop();
}
