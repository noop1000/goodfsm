package org.noop.goodfsm.fsm;


public class InvalidStateOpException
        extends RuntimeException {
    final private String stateName;


    public InvalidStateOpException(String p_StateName, Exception p_Reason) {
        super(p_Reason);
        this.stateName = p_StateName;

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InvalidStateOpException that = (InvalidStateOpException) o;

        return stateName != null ? stateName.equals(that.stateName) : that.stateName == null;

    }

    @Override
    public int hashCode() {
        return stateName != null ? stateName.hashCode() : 0;
    }


    @Override
    public String toString() {
        return "InvalidStateOpException{" +
                "stateName='" + stateName + '\'' +
                '}';
    }
}
