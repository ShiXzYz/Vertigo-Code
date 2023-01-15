package org.firstinspires.ftc.teamcode.drive;

import java.util.ArrayList;

class StateMachine {

    private ArrayList<StateObj> states = new ArrayList<StateObj>();

    private StateObj stateObj = null;

    public  int state = -1;

    public StateMachine() {

    }

    public void OnInitialize() {

        stateObj.OnInitialize();

    }

    public void OnLoop() {

        stateObj.OnLoop();

    }

    public void AddState(CallbackThing _stateObjCallbacks) {

        states.add(new StateObj(_stateObjCallbacks));

    }

    public void SwitchState(int _stateObj) {
        try {
            stateObj = states.get(_stateObj);
            state = _stateObj;
            stateObj.OnInitialize();
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
