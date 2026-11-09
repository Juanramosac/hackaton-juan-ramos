public class Main {

    public static void main(String[] args) {
        Agenda agenda = new Agenda();
        VentanaAgenda ventana = new VentanaAgenda(agenda);
        ventana.setVisible(true);
    }
}
