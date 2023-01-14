package org.firstinspires.ftc.teamcode.drive;

import java.util.ArrayList;

class StateMachine {

    private ArrayList<StateObj> states = new ArrayList<StateObj>();

    private StateObj state = null;

    public StateMachine() {

    }

    public void OnInitialize() {

        state.OnInitialize();

    }

    public void OnLoop() {

        state.OnLoop();

    }

    public void AddState(CallbackThing _stateCallbacks) {

        states.add(new StateObj(_stateCallbacks));

    }

    public void SwitchState(int _state) {
        try {
            state = states.get(_state);
            state.OnInitialize();
        } catch (Exception e) {
            //  Can't access telemetry cause its not static
//            telemetry.addData("Exception:", e.toString());
        }
    }
}

interface CallbackThing{
    void OnInit();
    void OnLoop();
}


class StateObj {

    public CallbackThing functions = null;

    public StateObj(CallbackThing _callbacks) {

        functions = _callbacks;

    }

    public void OnInitialize() {
        functions.OnInit();
    }

    public void OnLoop() {
        functions.OnLoop();
    }
}
