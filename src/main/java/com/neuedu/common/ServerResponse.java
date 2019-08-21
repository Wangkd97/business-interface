package com.neuedu.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/***
 *服务器响应对象
 *
 *
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerResponse<T> {

    private int status;
    private String msg;
    private T data;

    private ServerResponse(){}
    private ServerResponse(int status){
        this.status=status;
    }
    private ServerResponse(int status,String msg){
        this.status=status;
        this.msg=msg;
    }
    private ServerResponse(int status,T data){
        this.status=status;
        this.data=data;
    }
    private ServerResponse(int status,String msg,T data){
        this.status=status;
        this.data=data;
        this.msg=msg;
    }
    /**
     * 判断接口是否成功调用
     * */
    @JsonIgnore
    public boolean isSuccess(){
        return this.status==0;
    }
    @JsonIgnore
    public  boolean isSucess(){
        return this.status==0;
    }
    public  static <T> ServerResponse<T> createServerResponseBySucess(){
        return new ServerResponse<>(0);
    }
       public  static <T> ServerResponse<T> createServerResponseBySucess(T data){
        return new ServerResponse<>(0,data);
    }
    public  static <T> ServerResponse<T> createServerResponseBySucess(String msg){
        return new ServerResponse<>(0,msg);
    }
    public  static <T> ServerResponse<T> createServerResponseBySucess(String msg,T data){
        return new ServerResponse<>(0,msg,data);
    }
    public  static <T> ServerResponse<T> createServerResponseBySucess(int status,String msg){
        return new ServerResponse<>(status,msg);
    }
    public  static <T> ServerResponse<T> createServerResponseByFail(int status,String msg){
        return new ServerResponse<>(status,msg);
    }

    public  static<T>  ServerResponse<T> createServerResponseBySucces(){
        return new ServerResponse<>(0);
    }

    public  static<T>  ServerResponse<T> createServerResponseBySucces(T data){
        return new ServerResponse<>(0,data);
    }

    public  static<T>  ServerResponse<T> createServerResponseBySuccesas(String msg){
        return new ServerResponse<>(0,msg);
    }

    public  static<T>  ServerResponse<T> createServerResponseBySucces(String msg,T data){
        return new ServerResponse<>(0,msg,data);
    }


    public  static<T>  ServerResponse<T> createServerResponseByFail(String msg){
        return new ServerResponse<>(1,msg);
    }


    @Override
    public String toString() {
        return "ServerResponse{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }



    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(T data) {
        this.data = data;
    }
}
