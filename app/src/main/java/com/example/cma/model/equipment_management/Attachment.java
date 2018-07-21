package com.example.cma.model.equipment_management;

/**
 * Created by 王国新 on 2018/7/11.
 *
 */

public class Attachment {

    /**
     * attachmentId : 附件的id
     * name : 附件名
     * receiveId : 设备验收记录的id
     */

    private int attachmentId;
    private String name;
    private int receiveId;

    public int getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(int attachmentId) {
        this.attachmentId = attachmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(int receiveId) {
        this.receiveId = receiveId;
    }
}
