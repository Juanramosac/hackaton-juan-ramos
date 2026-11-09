import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class VentanaAgenda extends JFrame {

    private Agenda agenda;

    private JTextField campoNombre = new JTextField(15);
    private JTextField campoApellido = new JTextField(15);
    private JTextField campoTelefono = new JTextField(15);

    private DefaultListModel<String> modeloLista = new DefaultListModel<>();
    private JList<String> listaContactos = new JList<>(modeloLista);
    private JLabel etiquetaEstado = new JLabel();

    public VentanaAgenda(Agenda agenda) {
        super("Agenda de contactos");
        this.agenda = agenda;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getRootPane().setBorder(new EmptyBorder(12, 12, 12, 12));

        add(crearFormulario(), BorderLayout.NORTH);
        add(crearPanelLista(), BorderLayout.CENTER);
        add(crearBotones(), BorderLayout.EAST);
        add(etiquetaEstado, BorderLayout.SOUTH);

        actualizarVista();
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel crearFormulario() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new TitledBorder("Datos del contacto"));

        JPanel campos = new JPanel(new GridLayout(3, 2, 5, 5));
        campos.add(new JLabel("Nombre:"));
        campos.add(campoNombre);
        campos.add(new JLabel("Apellido:"));
        campos.add(campoApellido);
        campos.add(new JLabel("Teléfono:"));
        campos.add(campoTelefono);

        JLabel ayuda = new JLabel("Teléfono: 10 dígitos, con o sin +57. Ej: 3001234567");

        panel.add(campos, BorderLayout.CENTER);
        panel.add(ayuda, BorderLayout.SOUTH);
        return panel;
    }

    private JScrollPane crearPanelLista() {
        listaContactos.setVisibleRowCount(12);
        JScrollPane scroll = new JScrollPane(listaContactos);
        scroll.setBorder(new TitledBorder("Contactos"));
        scroll.setPreferredSize(new Dimension(380, 260));
        return scroll;
    }

    private JPanel crearBotones() {
        JPanel panel = new JPanel(new GridLayout(9, 1, 0, 6));

        JButton btnAnadir = new JButton("Añadir contacto");
        btnAnadir.addActionListener(e -> añadir());
        panel.add(btnAnadir);

        JButton btnBuscar = new JButton("Buscar contacto");
        btnBuscar.addActionListener(e -> buscar());
        panel.add(btnBuscar);

        JButton btnModificar = new JButton("Modificar teléfono");
        btnModificar.addActionListener(e -> modificar());
        panel.add(btnModificar);

        JButton btnEliminar = new JButton("Eliminar contacto");
        btnEliminar.addActionListener(e -> eliminar());
        panel.add(btnEliminar);

        JButton btnExiste = new JButton("¿Existe el contacto?");
        btnExiste.addActionListener(e -> existe());
        panel.add(btnExiste);

        JButton btnListar = new JButton("Listar contactos");
        btnListar.addActionListener(e -> listar());
        panel.add(btnListar);

        JButton btnEspacio = new JButton("Espacio libre");
        btnEspacio.addActionListener(e -> espacioLibre());
        panel.add(btnEspacio);

        JButton btnLlena = new JButton("¿Agenda llena?");
        btnLlena.addActionListener(e -> llena());
        panel.add(btnLlena);

        JButton btnLimpiar = new JButton("Limpiar campos");
        btnLimpiar.addActionListener(e -> limpiarCampos());
        panel.add(btnLimpiar);

        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.add(panel, BorderLayout.NORTH);
        return contenedor;
    }

    private void añadir() {
        if (agenda.agendaLlena()) {
            error("La agenda está llena. No se pueden añadir más contactos.");
            return;
        }
        if (!nombreYApellidoValidos()) {
            return;
        }
        if (!Contacto.esTelefonoValido(campoTelefono.getText())) {
            error("Teléfono no válido. Debe tener 10 dígitos.");
            return;
        }

        Contacto nuevo = new Contacto(nombre(), apellido(), campoTelefono.getText());
        if (agenda.existeContacto(nuevo)) {
            error("Ya existe un contacto llamado " + nombre() + " " + apellido() + ".");
            return;
        }
        if (agenda.añadirContacto(nuevo)) {
            mensaje("Contacto añadido. Espacios libres: " + agenda.espacioLibres());
            limpiarCampos();
            actualizarVista();
        } else {
            error("No se pudo añadir el contacto.");
        }
    }

    private void buscar() {
        if (!nombreYApellidoValidos()) {
            return;
        }
        Contacto encontrado = agenda.buscaContacto(nombre(), apellido());
        if (encontrado == null) {
            error("No se encontró a " + nombre() + " " + apellido() + ".");
        } else {
            campoTelefono.setText(encontrado.getTelefono());
            mensaje("Teléfono de " + encontrado.getNombre() + " " + encontrado.getApellido()
                    + ": " + encontrado.getTelefono());
        }
    }

    private void modificar() {
        if (!nombreYApellidoValidos()) {
            return;
        }
        if (agenda.buscaContacto(nombre(), apellido()) == null) {
            error("No existe un contacto llamado " + nombre() + " " + apellido() + ".");
            return;
        }
        if (!Contacto.esTelefonoValido(campoTelefono.getText())) {
            error("Escribe el teléfono nuevo válido (10 dígitos).");
            return;
        }
        if (agenda.modificarTelefono(nombre(), apellido(), campoTelefono.getText())) {
            mensaje("Teléfono actualizado.");
            actualizarVista();
        } else {
            error("No se pudo actualizar el teléfono.");
        }
    }

    private void eliminar() {
        if (!nombreYApellidoValidos()) {
            return;
        }
        Contacto aEliminar = new Contacto(nombre(), apellido());
        if (!agenda.existeContacto(aEliminar)) {
            error("No existe un contacto llamado " + nombre() + " " + apellido() + ".");
            return;
        }
        int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Seguro que quieres eliminar a " + nombre() + " " + apellido() + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }
        if (agenda.eliminarContacto(aEliminar)) {
            mensaje("Contacto eliminado.");
            limpiarCampos();
            actualizarVista();
        } else {
            error("No se pudo eliminar el contacto.");
        }
    }

    private void existe() {
        if (!nombreYApellidoValidos()) {
            return;
        }
        if (agenda.existeContacto(new Contacto(nombre(), apellido()))) {
            mensaje(nombre() + " " + apellido() + " SÍ está en la agenda.");
        } else {
            mensaje(nombre() + " " + apellido() + " NO está en la agenda.");
        }
    }

    private void listar() {
        actualizarVista();
        JTextArea texto = new JTextArea(agenda.listarContactos());
        texto.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(texto),
                "Contactos (" + agenda.getCantidadContactos() + ")", JOptionPane.PLAIN_MESSAGE);
    }

    private void espacioLibre() {
        mensaje("Puedes agregar " + agenda.espacioLibres() + " contacto(s) más.");
    }

    private void llena() {
        if (agenda.agendaLlena()) {
            error("La agenda está llena: no hay espacio para nuevos contactos.");
        } else {
            mensaje("La agenda no está llena. Espacios libres: " + agenda.espacioLibres());
        }
    }

    private void actualizarVista() {
        modeloLista.clear();
        if (agenda.getCantidadContactos() > 0) {
            for (String linea : agenda.listarContactos().split("\n")) {
                modeloLista.addElement(linea);
            }
        }
        String estado = "Contactos: " + agenda.getCantidadContactos() + " de " + agenda.getTamanoMaximo()
                + "   |   Espacios libres: " + agenda.espacioLibres();
        if (agenda.agendaLlena()) {
            estado = estado + "   |   AGENDA LLENA";
        }
        etiquetaEstado.setText(estado);
    }

    private boolean nombreYApellidoValidos() {
        if (Contacto.esTextoVacio(campoNombre.getText())) {
            error("Escribe el nombre.");
            return false;
        }
        if (Contacto.esTextoVacio(campoApellido.getText())) {
            error("Escribe el apellido.");
            return false;
        }
        return true;
    }

    private String nombre() {
        return campoNombre.getText().trim();
    }

    private String apellido() {
        return campoApellido.getText().trim();
    }

    private void limpiarCampos() {
        campoNombre.setText("");
        campoApellido.setText("");
        campoTelefono.setText("");
    }

    private void mensaje(String texto) {
        JOptionPane.showMessageDialog(this, texto, "Agenda", JOptionPane.INFORMATION_MESSAGE);
    }

    private void error(String texto) {
        JOptionPane.showMessageDialog(this, texto, "Agenda", JOptionPane.WARNING_MESSAGE);
    }
}