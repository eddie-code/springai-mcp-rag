package org.dromara.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @author lee
 * @description
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SearXNGResponse {

    private String query;
    private List<SearchResult> results;

}
