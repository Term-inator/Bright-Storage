package com.example.bright_storage.model.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
@Table(name = "operation_log")
public class OperationLog extends BaseEntity {

    @Column(name = "id", isId = true)
    private Long id;

    @Column(name = "operation_type")
    private Integer operationType;

    @Column(name = "data")
    private String data;

    /**
     * 服务器上StorageUnit的ID
     */
    @Column(name = "remote_id")
    private Long remoteId;

    /**
     * 客户端StorageUnit的ID
     */
    @Column(name = "local_id")
    private Long localId;

    @Column(name = "version")
    private Long version;

    @Override
    public void prePersist() {
        super.prePersist();
        if(data == null){
            data = "";
        }
        if(version == null){
            version = 0L;
        }
    }


}
