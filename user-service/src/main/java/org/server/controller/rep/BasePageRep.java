package org.server.controller.rep;

import lombok.Data;

@Data
public abstract class BasePageRep {
    private Integer pageSize;
    private Integer page;
    private Integer totalPage;
    private Long total;
}
