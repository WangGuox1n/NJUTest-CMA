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

    public static String Equipment_getAll(){
        return "http://119.23.38.100:8080/cma/Equipment/getAll";
    }

    public static String Equipment_getOne(Long id){
        return "http://119.23.38.100:8080/cma/Equipment/getOne?id="+id;
    }

    public static String Equipment_addOne(){
        return "http://119.23.38.100:8080/cma/Equipment/addOne";
    }

    public static String Equipment_modifyOne(){
        return "http://119.23.38.100:8080/cma/Equipment/modifyOne";
    }

    public static String Equipment_deleteOne(){
        return "http://119.23.38.100:8080/cma/Equipment/deleteOne";
    }

    public static String EquipmentReceive_getAll(){
        return "http://119.23.38.100:8080/cma/EquipmentReceive/getAll";
    }

    public static String EquipmentReceive_getOne(Long id){
        return "http://119.23.38.100:8080/cma/EquipmentReceive/getOne?id="+id;
    }

    public static String EquipmentReceive_addOne(){
        return "http://119.23.38.100:8080/cma/EquipmentReceive/addOne";
    }

    public static String EquipmentReceive_modifyOne(){
        return "http://119.23.38.100:8080/cma/EquipmentReceive/modifyOne";
    }

    public static String EquipmentReceive_deleteOne(){
        return "http://119.23.38.100:8080/cma/EquipmentReceive/deleteOne";
    }

    public static String EquipmentUse_getAll(){
        return "http://119.23.38.100:8080/cma/EquipmentUse/getAll";
    }

    public static String EquipmentUse_getOne(Long id){
        return "http://119.23.38.100:8080/cma/EquipmentUse/getOne?id="+id;
    }

    public static String EquipmentUse_addOne(){
        return "http://119.23.38.100:8080/cma/EquipmentUse/addOne";
    }

    public static String EquipmentUse_modifyOne(){
        return "http://119.23.38.100:8080/cma/EquipmentUse/modifyOne";
    }

    public static String EquipmentUse_deleteOne(){
        return "http://119.23.38.100:8080/cma/EquipmentUse/deleteOne";
    }

    public static String EquipmentUse_getAllByEquipmentId(String equipmentId){
        return "http://119.23.38.100:8080/cma/EquipmentUse/getAllByEquipmentId?equipmentId="+equipmentId;
    }

    public static String EquipmentMaintenance_getAll(){
        return "http://119.23.38.100:8080/cma/EquipmentMaintenance/getAll";
    }

    public static String EquipmentMaintenance_getOne(Long id){
        return "http://119.23.38.100:8080/cma/EquipmentMaintenance/getOne?id="+id;
    }

    public static String EquipmentMaintenance_addOne(){
        return "http://119.23.38.100:8080/cma/EquipmentMaintenance/addOne";
    }

    public static String EquipmentMaintenance_modifyOne(){
        return "http://119.23.38.100:8080/cma/EquipmentMaintenance/modifyOne";
    }

    public static String EquipmentMaintenance_deleteOne(){
        return "http://119.23.38.100:8080/cma/EquipmentMaintenance/deleteOne";
    }

    public static String EquipmentMaintenance_getAllByEquipmentId(String equipmentId){
        return "http://119.23.38.100:8080/cma/EquipmentMaintenance/getAllByEquipmentId?equipmentId="+equipmentId;
    }

    public static String EquipmentApplication_getAll(){
        return "http://119.23.38.100:8080/cma/EquipmentApplication/getAll";
    }

    public static String EquipmentApplication_getOne(Long id){
        return "http://119.23.38.100:8080/cma/EquipmentApplication/getOne?id="+id;
    }

    public static String EquipmentApplication_addOne(){
        return "http://119.23.38.100:8080/cma/EquipmentApplication/addOne";
    }

    public static String EquipmentApplication_modifyOne(){
        return "http://119.23.38.100:8080/cma/EquipmentApplication/modifyOne";
    }

    public static String EquipmentApplication_deleteOne(){
        return "http://119.23.38.100:8080/cma/EquipmentApplication/deleteOne";
    }
}
