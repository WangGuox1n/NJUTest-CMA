package com.example.cma.utils;

/**
 * Created by 王国新 on 2018/6/9.
 */

public class AddressUtil {

    public static final int Supervision_getAll = 0x50;
    public static final int Supervision_addOne = 0x51;
    public static final int Supervision_modifyOne = 0x52;
    public static final int Supervision_deleteOne = 0x53;
    public static final int Supervision_approveOne = 0x54;
    public static final int Supervision_executeOne = 0x55;
    public static final int SupervisionPlan_getAll = 0x56;
    public static final int SupervisionPlan_addOne = 0x57;
    public static final int SupervisionPlan_modifyOne = 0x58;
    public static final int SupervisionPlan_deleteOne = 0x59;
    public static final int SupervisionRecord_getAll = 0x5a;
    public static final int SupervisionRecord_addOne = 0x5b;
    public static final int SupervisionRecord_modifyOne = 0x5c;
    public static final int SupervisionRecord_deleteOne = 0x5d;

    public static final int Equipment_getAll = 0xa00;
    public static final int Equipment_getOne = 0xa01;
    public static final int Equipment_addOne = 0xa02;
    public static final int Equipment_modifyOne = 0xa03;
    public static final int Equipment_deleteOne = 0xa04;
    public static final int EquipmentReceive_getAll = 0xa05;
    public static final int EquipmentReceive_getOne = 0xa06;
    public static final int EquipmentReceive_addOne = 0xa07;
    public static final int EquipmentReceive_modifyOne = 0xa08;
    public static final int EquipmentReceive_deleteOne = 0xa09;
    public static final int EquipmentUse_getAll = 0xa0a;
    public static final int EquipmentUse_getOne = 0xa0b;
    public static final int EquipmentUse_addOne = 0xa0c;
    public static final int EquipmentUse_modifyOne = 0xa0d;
    public static final int EquipmentUse_deleteOne = 0xa0e;
    public static final int EquipmentUse_getAllByEquipmentId = 0xa0f;
    public static final int EquipmentMaintenance_getAll = 0xa10;
    public static final int EquipmentMaintenance_getOne = 0xa11;
    public static final int EquipmentMaintenance_addOne = 0xa12;
    public static final int EquipmentMaintenance_modifyOne = 0xa13;
    public static final int EquipmentMaintenance_deleteOne = 0xa14;
    public static final int EquipmentMaintenance_getAllByEquipmentId = 0xa15;
    public static final int EquipmentApplication_getAll = 0xa16;
    public static final int EquipmentApplication_getOne = 0xa17;
    public static final int EquipmentApplication_addOne = 0xa18;
    public static final int EquipmentApplication_modifyOne = 0xa19;
    public static final int EquipmentApplication_deleteOne = 0xa1a;

    public static final int SelfInspection_getAll = 0x40;
    public static final int SelfInspection_addOne = 0x41;
    public static final int SelfInspection_deleteOne = 0x42;
    public static final int SelfInspection_getAllFile = 0x43;
    public static final int SelfInspection_addOneFile = 0x44;
    public static final int SelfInspection_modifyOneFile = 0x45;
    public static final int SelfInspection_deleteOneFile = 0x46;
    public static final int SelfInspection_downloadFile = 0x47;

    public static String getAddress(int param){
        String serverHead = "http://119.23.38.100:8080/cma/";
        switch (param){
            case Supervision_getAll:        return serverHead + "Supervision/getAll";
            case Supervision_addOne:        return serverHead + "Supervision/addOne";
            case Supervision_modifyOne:     return serverHead + "Supervision/modifyOne";
            case Supervision_deleteOne:     return serverHead + "Supervision/deleteOne";
            case Supervision_approveOne:    return serverHead + "Supervision/approveOne";
            case Supervision_executeOne:    return serverHead + "Supervision/executeOne";
            case SupervisionPlan_getAll:    return serverHead + "SupervisionPlan/getAll?id=";
            case SupervisionPlan_addOne:    return serverHead + "SupervisionPlan/addOne";
            case SupervisionPlan_modifyOne: return serverHead + "SupervisionPlan/modifyOne";
            case SupervisionPlan_deleteOne: return serverHead + "SupervisionPlan/deleteOne";
            case SupervisionRecord_getAll:  return serverHead + "SupervisionRecord/getAll?planId=";
            case SupervisionRecord_addOne:  return serverHead + "SupervisionRecord/addOne";
            case SupervisionRecord_modifyOne:return serverHead + "SupervisionRecord/modifyOne";
            case SupervisionRecord_deleteOne:return serverHead + "SupervisionRecord/deleteOne";

            case Equipment_getAll:          return serverHead + "Equipment/getAll";
            case Equipment_getOne:          return serverHead + "Equipment/getOne?id=";
            case Equipment_addOne:          return serverHead + "Equipment/addOne";
            case Equipment_modifyOne:       return serverHead + "Equipment/modifyOne";
            case Equipment_deleteOne:       return serverHead + "Equipment/deleteOne";
            case EquipmentReceive_getAll:   return serverHead + "EquipmentReceive/getAll";
            case EquipmentReceive_getOne:   return serverHead + "EquipmentReceive/getOne?id=";
            case EquipmentReceive_addOne:   return serverHead + "EquipmentReceive/addOne";
            case EquipmentReceive_modifyOne:return serverHead + "EquipmentReceive/modifyOne";
            case EquipmentReceive_deleteOne:return serverHead + "EquipmentReceive/deleteOne";
            case EquipmentUse_getAll:       return serverHead + "EquipmentUse/getAll";
            case EquipmentUse_getOne:       return serverHead + "EquipmentUse/getOne?id=";
            case EquipmentUse_addOne:       return serverHead + "EquipmentUse/addOne";
            case EquipmentUse_modifyOne:    return serverHead + "EquipmentUse/modifyOne";
            case EquipmentUse_deleteOne:    return serverHead + "EquipmentUse/deleteOne";
            case EquipmentUse_getAllByEquipmentId:return serverHead + "EquipmentUse/getAllByEquipmentId";
            case EquipmentMaintenance_getAll:   return serverHead + "EquipmentMaintenance/getAll";
            case EquipmentMaintenance_getOne:   return serverHead + "EquipmentMaintenance/getOne?id=";
            case EquipmentMaintenance_addOne:   return serverHead + "EquipmentMaintenance/addOne";
            case EquipmentMaintenance_modifyOne:return serverHead + "EquipmentMaintenance/modifyOne";
            case EquipmentMaintenance_deleteOne:return serverHead + "EquipmentMaintenance/deleteOne";
            case EquipmentMaintenance_getAllByEquipmentId:return serverHead + "EquipmentMaintenance/getAllByEquipmentId";
            case EquipmentApplication_getAll:   return serverHead + "EquipmentApplication/getAll";
            case EquipmentApplication_getOne:   return serverHead + "EquipmentApplication/getOne?id=";
            case EquipmentApplication_addOne:   return serverHead + "EquipmentApplication/addOne";
            case EquipmentApplication_modifyOne:return serverHead + "EquipmentApplication/modifyOne";
            case EquipmentApplication_deleteOne:return serverHead + "EquipmentApplication/deleteOne";

            case SelfInspection_getAll:         return serverHead + "SelfInspection/getAll";
            case SelfInspection_addOne:         return serverHead + "SelfInspection/addOne";
            case SelfInspection_deleteOne:      return serverHead + "SelfInspection/deleteOne";
            case SelfInspection_getAllFile:     return serverHead + "SelfInspection/getAllFile";
            case SelfInspection_addOneFile:     return serverHead + "SelfInspection/addOneFile";
            case SelfInspection_modifyOneFile:  return serverHead + "SelfInspection/modifyOneFile";
            case SelfInspection_deleteOneFile:  return serverHead + "SelfInspection/deleteOneFile";
            case SelfInspection_downloadFile:   return serverHead + "SelfInspection/downloadFile";
        }
        return "";
    }

    public static String getLocal(){
        return "http://10.0.2.2/get_data1.json";
    }
}
