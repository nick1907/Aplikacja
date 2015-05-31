package destroyer.friendzone.com.fzdestroyer;

// klasa przedstawiajaca pojedynczy element listy
public class Element
{
    String imie;
    String nazwisko;
    String login;
    int obrazek;

    public Element(String imie, String nazwisko, String login, int zdjecie)
    {
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.login = login;
        obrazek = zdjecie;
    }

    public Element(String login, int zdjecie)
    {
        imie = "";
        nazwisko = "";
        this.login = login;
        obrazek = zdjecie;
    }

    public Element(String login)
    {
        imie = "";
        nazwisko = "";
        this.login = login;
    }
}
