package hhu.propra2.javageddon.teils.model;

public class Aufladung {
    private double betrag;
    private ProPayUser proPayUser;

    public void setProPayUser(ProPayUser proPayUser) {
        this.proPayUser = proPayUser;
    }

    public ProPayUser getProPayUser(){
        return proPayUser;
    }

    public double getBetrag() {
        return betrag;
    }

    public void setBetrag(double betrag) {
        this.betrag = betrag;
    }
}


