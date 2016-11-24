package prueba;

import com.trelloapp.dto.assembler.EjemploDTOMapper;

public class Pruebas {
    public static void main(String[] args) {
        Ejemplo e = new Ejemplo();
        e.setA1("a");
        e.setA2("b");

        EjemploDTOMapper ass = EjemploDTOMapper.INSTANCE;
        EjemploDTO a = ass.toDTO(e);
        System.out.println(a.getA1());
    }
}
