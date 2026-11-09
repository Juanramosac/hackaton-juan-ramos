import java.util.ArrayList;
import java.util.List;

public class Agenda {

    private List<Contacto> contactos;
    private int tamanoMaximo = 10;

    public Agenda() {
        this.contactos = new ArrayList<>();
    }

    public boolean añadirContacto(Contacto c) {
        if (c == null || agendaLlena() || existeContacto(c)) {
            return false;
        }
        contactos.add(c);
        return true;
    }

    public boolean existeContacto(Contacto c) {
        return contactos.contains(c);
    }

    public String listarContactos() {
        if (contactos.isEmpty()) {
            return "La agenda está vacía.";
        }

        List<Contacto> ordenados = new ArrayList<>(contactos);
        ordenados.sort((c1, c2) -> {
            int comparacionNombre = c1.getNombre().compareToIgnoreCase(c2.getNombre());
            if (comparacionNombre != 0) {
                return comparacionNombre;
            }
            return c1.getApellido().compareToIgnoreCase(c2.getApellido());
        });

        String texto = "";
        for (Contacto c : ordenados) {
            texto = texto + c.toString() + "\n";
        }
        return texto;
    }

    public Contacto buscaContacto(String nombre, String apellido) {
        Contacto buscado = new Contacto(nombre, apellido);
        for (Contacto c : contactos) {
            if (c.equals(buscado)) {
                return c;
            }
        }
        return null;
    }

    public boolean eliminarContacto(Contacto c) {
        return contactos.remove(c);
    }

    public boolean modificarTelefono(String nombre, String apellido, String nuevoTelefono) {
        Contacto c = buscaContacto(nombre, apellido);
        if (c == null) {
            return false;
        }
        c.setTelefono(nuevoTelefono);
        return true;
    }

    public boolean agendaLlena() {
        return contactos.size() >= tamanoMaximo;
    }

    public int espacioLibres() {
        return tamanoMaximo - contactos.size();
    }

    public int getTamanoMaximo() {
        return tamanoMaximo;
    }

    public int getCantidadContactos() {
        return contactos.size();
    }
}