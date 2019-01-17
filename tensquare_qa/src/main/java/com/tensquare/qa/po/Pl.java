package com.tensquare.qa.po;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 问题和标签的关系表
 */
@Entity
@Table(name="tb_pl")
public class Pl implements Serializable {
    @Id
    private String problemid;//问题ID
    @Id
    private String labelid;//标签ID

    public String getProblemid() {
        return problemid;
    }

    public void setProblemid(String problemid) {
        this.problemid = problemid;
    }

    public String getLabelid() {
        return labelid;
    }

    public void setLabelid(String labelid) {
        this.labelid = labelid;
    }
}
