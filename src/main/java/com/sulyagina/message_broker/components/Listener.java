package com.sulyagina.message_broker.components;

import java.io.Serializable;

/**
 * Created by anastasia on 27.05.16.
 */

public interface Listener extends Serializable {
    void onReceive(Message message);
}
