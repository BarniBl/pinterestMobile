package com.solar.pinterest.solarmobile.network.models.responses.bodies;

import com.solar.pinterest.solarmobile.network.models.Board;
import com.solar.pinterest.solarmobile.network.models.Pin;
import com.solar.pinterest.solarmobile.storage.DBSchema;

public class CreatePinResponseBody {
    public String info;
    public Pin pin;

    public CreatePinResponseBody(String info, Pin pin) {
        this.info = info;
        this.pin = pin;
    }

    public CreatePinResponseBody() {
        this.info = "";
        this.pin = new Pin();
    }
}
