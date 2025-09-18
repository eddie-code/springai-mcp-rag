package org.dromara.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author lee
 * @description
 */

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponseEntity {

    private String message;

    private String botMsgId;

}
