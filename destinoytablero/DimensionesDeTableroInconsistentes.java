/** Indica que alguna dimensión del tablero está fuera del rango permitido. */
public class DimensionesDeTableroInconsistentes extends Exception {
    /** Identificador de versión para la serialización. */
    private static final long serialVersionUID = 1L;

    /**
     * Crea la excepción con el mensaje recibido.
     * <p><b>Precondiciones:</b> ninguna; el mensaje puede ser nulo.</p>
     * <p><b>Postcondiciones:</b> {@link #getMessage()} devuelve el mensaje recibido.</p>
     * @param mensaje descripción del motivo de la excepción
     */
    public DimensionesDeTableroInconsistentes(String mensaje) {
        super(mensaje);
    }
}
