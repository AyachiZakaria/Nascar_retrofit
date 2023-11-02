package tn.esprit.nascar.dao

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.POST
import tn.esprit.nascar.models.User

interface UserApiInterface {

    @POST("login")
    fun login(user:User):Call<User>
      companion object{


              var BASE_URL = "http//172.16.5.215:5000/"

              fun create() : UserApiInterface {

                  val retrofit = Retrofit   .Builder()
                      .addConverterFactory(GsonConverterFactory.create())
                      .baseUrl(BASE_URL)
                      .build()

                  return retrofit.create(UserApiInterface::class.java)
              }
          }
      }

