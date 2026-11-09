import java.util.Objects;

public class Contacto {

    private String nombre;
    private String apellido;
    private String telefono;

    public Contacto(String nombre, String apellido, String telefono) {
        if (esTextoVacio(nombre) || esTextoVacio(apellido)) {
            throw new IllegalArgumentException("El nombre y el apellido no pueden estar vacíos");
        }
        if (!esTelefonoValido(telefono)) {
            throw new IllegalArgumentException("El teléfono no tiene un formato válido");
        }
        this.nombre = nombre.trim();
        this.apellido = apellido.trim();
        this.telefono = limpiarTelefono(telefono);
    }

    public Contacto(String nombre, String apellido) {
        if (esTextoVacio(nombre) || esTextoVacio(apellido)) {
            throw new IllegalArgumentException("El nombre y el apellido no pueden estar vacíos");
        }
        this.nombre = nombre.trim();
        this.apellido = apellido.trim();
        this.telefono = "";
    }

    public static boolean esTextoVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }

    public static boolean esTelefonoValido(String telefono) {
        if (telefono == null) {
            return false;
        }
        String limpio = limpiarTelefono(telefono);
        if (limpio.startsWith("+57")) {
            limpio = limpio.substring(3);
        }
        if (limpio.length() != 10) {
            return false;
        }
        for (int i = 0; i < limpio.length(); i++) {
            char letra = limpio.charAt(i);
            if (letra < '0' || letra > '9') {
                return false;
            }
        }
        return true;
    }

    private static String limpiarTelefono(String telefono) {
        return telefono.replace(" ", "");
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        if (!esTelefonoValido(telefono)) {
            throw new IllegalArgumentException("El teléfono no tiene un formato válido");
        }
        this.telefono = limpiarTelefono(telefono);
    }

    @Override
    public String toString() {
        return nombre + " " + apellido + " - " + telefono;
    }

    @Override
    public boolean equals(Object otro) {
        if (this == otro) {
            return true;
        }
        if (!(otro instanceof Contacto c)) {
            return false;
        }
        return nombre.equalsIgnoreCase(c.nombre) && apellido.equalsIgnoreCase(c.apellido);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre.toLowerCase(), apellido.toLowerCase());
    }
}