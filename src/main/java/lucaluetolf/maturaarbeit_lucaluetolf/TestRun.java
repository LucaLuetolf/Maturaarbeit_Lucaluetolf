package lucaluetolf.maturaarbeit_lucaluetolf;


public class TestRun {
    public static void main(String[] args) {
        System.out.println(Setup.getGroesseArrayKunden());
        Setup.setGroesseArrayKunden(Setup.getGroesseArrayKunden()+1);
        System.out.println(Setup.getGroesseArrayKunden());
        Setup.setupKunden();




        /*System.out.println(Setup.arrayKunde[0].getNachname());
        System.out.println(Setup.arrayKunde[1].getNachname());
        System.out.println(Setup.arrayKunde[2].getNachname());
        */

    }

}
