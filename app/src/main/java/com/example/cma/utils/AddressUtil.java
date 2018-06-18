package com.example.cma.utils;

/**
 * Created by 王国新 on 2018/6/9.
 */

public class AddressUtil {

    public static String getLocal(){
        return "http://10.0.2.2/get_data.json";
    }

    public static String Supervision_getAll(){
        return "http://119.23.38.100:8080/cma/Supervision/getAll";
    }

    public static String Supervision_addOne(){
        return "http://119.23.38.100:8080/cma/Supervision/addOne";
    }

    public static String Supervision_modifyOne(){
        return "http://119.23.38.100:8080/cma/Supervision/modifyOne";
    }

    public static String Supervision_deleteOne(){
        return "http://119.23.38.100:8080/cma/Supervision/deleteOne";
    }

    public static String Supervision_approveOne(){
        return "http://119.23.38.100:8080/cma/Supervision/approveOne";
    }

    public static String Supervision_executeOne(){
        return "http://119.23.38.100:8080/cma/Supervision/executeOne";
    }

    public static String SupervisionPlan_getAll(Long id){
        return "http://119.23.38.100:8080/cma/SupervisionPlan/getAll?id="+id;
    }

    public static String SupervisionPlan_addOne(){
        return "http://119.23.38.100:8080/cma/SupervisionPlan/addOne";
    }

    public static String SupervisionPlan_modifyOne(){
        return "http://119.23.38.100:8080/cma/SupervisionPlan/modifyOne";
    }

    public static String SupervisionPlan_deleteOne(){
        return "http://119.23.38.100:8080/cma/SupervisionPlan/deleteOne";
    }

    public static String SupervisionRecord_getAll(Long planId){
        return "http://119.23.38.100:8080/cma/SupervisionRecord/getAll?planId="+planId;
    }

    public static String SupervisionRecord_addOne(){
        return "http://119.23.38.100:8080/cma/SupervisionRecord/addOne";
    }

    public static String SupervisionRecord_modifyOne(){
        return "http://119.23.38.100:8080/cma/SupervisionRecord/modifyOne";
    }

    public static String SupervisionRecord_deleteOne(){
        return "http://119.23.38.100:8080/cma/SupervisionRecord/deleteOne";
    }
}
